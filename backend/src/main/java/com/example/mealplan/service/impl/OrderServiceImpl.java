package com.example.mealplan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.mealplan.common.MealStatus;
import com.example.mealplan.common.ResultCode;
import com.example.mealplan.dto.request.OrderSubmitRequest;
import com.example.mealplan.dto.response.OrderHistoryResponse;
import com.example.mealplan.dto.response.OrderStatisticsResponse;
import com.example.mealplan.entity.Dish;
import com.example.mealplan.entity.MealPlan;
import com.example.mealplan.entity.OrderRecord;
import com.example.mealplan.exception.BusinessException;
import com.example.mealplan.mapper.DishMapper;
import com.example.mealplan.mapper.MealPlanMapper;
import com.example.mealplan.mapper.OrderRecordMapper;
import com.example.mealplan.mapper.OrderStatistics;
import com.example.mealplan.service.OrderService;
import com.example.mealplan.websocket.WebSocketServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRecordMapper orderRecordMapper;
    private final DishMapper dishMapper;
    private final MealPlanMapper mealPlanMapper;
    private final WebSocketServer webSocketServer;

    public OrderServiceImpl(OrderRecordMapper orderRecordMapper, DishMapper dishMapper,
            MealPlanMapper mealPlanMapper, WebSocketServer webSocketServer) {
        this.orderRecordMapper = orderRecordMapper;
        this.dishMapper = dishMapper;
        this.mealPlanMapper = mealPlanMapper;
        this.webSocketServer = webSocketServer;
    }

    @Override
    @Transactional
    public void submitOrder(Long userId, OrderSubmitRequest request) {
        for (Long dishId : request.getDishIds()) {
            LambdaQueryWrapper<OrderRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderRecord::getUserId, userId)
                    .eq(OrderRecord::getMealPlanId, request.getMealPlanId())
                    .eq(OrderRecord::getDishId, dishId);

            if (orderRecordMapper.selectCount(wrapper) > 0) {
                throw new BusinessException(ResultCode.ORDER_ALREADY_SUBMITTED);
            }

            OrderRecord orderRecord = new OrderRecord();
            orderRecord.setUserId(userId);
            orderRecord.setMealPlanId(request.getMealPlanId());
            orderRecord.setDishId(dishId);
            orderRecordMapper.insert(orderRecord);
        }

        webSocketServer.broadcastOrderSubmit(userId, request.getDishIds());
    }

    @Override
    public List<OrderStatisticsResponse> getStatistics(Long mealPlanId) {
        List<OrderStatistics> statistics = orderRecordMapper.selectStatisticsByMealPlanId(mealPlanId);
        return statistics.stream()
                .map(stat -> {
                    Dish dish = dishMapper.selectById(stat.getDishId());
                    return new OrderStatisticsResponse(stat.getDishId(), dish.getName(), stat.getCount());
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void confirmMenu(Long mealPlanId, List<Long> dishIds, String adminReply) {
        MealPlan mealPlan = mealPlanMapper.selectById(mealPlanId);
        if (mealPlan == null) {
            throw new BusinessException(ResultCode.MEAL_NOT_FOUND);
        }

        mealPlan.setStatus(MealStatus.FINISHED);
        mealPlan.setAdminReply(adminReply);
        mealPlanMapper.updateById(mealPlan);

        webSocketServer.broadcastMenuConfirm(mealPlanId, dishIds);
    }

    @Override
    public List<OrderHistoryResponse> getOrderHistory(Long userId) {
        List<OrderRecord> records = orderRecordMapper.selectList(
                new LambdaQueryWrapper<OrderRecord>()
                        .eq(OrderRecord::getUserId, userId)
                        .orderByDesc(OrderRecord::getCreateTime));

        return records.stream()
                .map(record -> {
                    Dish dish = dishMapper.selectById(record.getDishId());
                    OrderHistoryResponse.DishInfo dishInfo = null;
                    if (dish != null) {
                        dishInfo = new OrderHistoryResponse.DishInfo(
                                dish.getId(), dish.getName(), dish.getDescription());
                    }

                    MealPlan mealPlan = mealPlanMapper.selectById(record.getMealPlanId());

                    OrderHistoryResponse response = new OrderHistoryResponse();
                    response.setId(record.getId());
                    response.setMealPlanId(record.getMealPlanId());
                    if (mealPlan != null) {
                        response.setMealDate(mealPlan.getMealDate().toString());
                        response.setMealType(mealPlan.getMealType().getCode());
                        response.setStatus(mealPlan.getStatus().getCode());
                    }
                    response.setDishes(dishInfo != null ? List.of(dishInfo) : List.of());
                    response.setCreateTime(record.getCreateTime().toString());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
