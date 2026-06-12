package com.example.mealplan.service.impl;

import com.example.mealplan.common.MealStatus;
import com.example.mealplan.common.MealType;
import com.example.mealplan.common.ResultCode;
import com.example.mealplan.dto.request.MealPublishRequest;
import com.example.mealplan.dto.response.DishResponse;
import com.example.mealplan.dto.response.MealPlanResponse;
import com.example.mealplan.entity.Dish;
import com.example.mealplan.entity.MealPlan;
import com.example.mealplan.entity.MealPlanDish;
import com.example.mealplan.exception.BusinessException;
import com.example.mealplan.mapper.DishMapper;
import com.example.mealplan.mapper.MealPlanDishMapper;
import com.example.mealplan.mapper.MealPlanMapper;
import com.example.mealplan.service.MealPlanService;
import com.example.mealplan.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealPlanServiceImpl implements MealPlanService {

    private final MealPlanMapper mealPlanMapper;
    private final MealPlanDishMapper mealPlanDishMapper;
    private final DishMapper dishMapper;
    private final WebSocketServer webSocketServer;

    @Override
    public MealPlanResponse getTodayMeal(MealType mealType) {
        MealPlan mealPlan = mealPlanMapper.selectTodayMeal(LocalDate.now(), mealType);
        if (mealPlan == null) {
            return null;
        }
        return convertToResponse(mealPlan);
    }

    @Override
    @Transactional
    public MealPlanResponse publishMeal(MealPublishRequest request) {
        MealPlan existingMeal = mealPlanMapper.selectTodayMeal(request.getMealDate(), request.getMealType());
        if (existingMeal != null && existingMeal.getStatus() == MealStatus.PUBLISHED) {
            throw new BusinessException(ResultCode.MEAL_ALREADY_PUBLISHED);
        }

        MealPlan mealPlan;
        if (existingMeal != null) {
            mealPlan = existingMeal;
            mealPlanDishMapper.deleteByMealPlanId(mealPlan.getId());
        } else {
            mealPlan = new MealPlan();
            mealPlan.setMealDate(request.getMealDate());
            mealPlan.setMealType(request.getMealType());
            mealPlan.setStatus(MealStatus.PUBLISHED);
            mealPlanMapper.insert(mealPlan);
        }

        for (Long dishId : request.getDishIds()) {
            MealPlanDish mealPlanDish = new MealPlanDish();
            mealPlanDish.setMealPlanId(mealPlan.getId());
            mealPlanDish.setDishId(dishId);
            mealPlanDishMapper.insert(mealPlanDish);
        }

        webSocketServer.broadcastMenuConfirm(mealPlan.getId(), request.getDishIds());

        return convertToResponse(mealPlan);
    }

    @Override
    public List<MealPlanResponse> getRecentMeals(int limit) {
        List<MealPlan> mealPlans = mealPlanMapper.selectRecentMeals(limit);
        return mealPlans.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MealPlanResponse convertToResponse(MealPlan mealPlan) {
        List<Long> dishIds = mealPlanDishMapper.selectDishIdsByMealPlanId(mealPlan.getId());
        List<Dish> dishes = dishMapper.selectBatchIds(dishIds);
        List<DishResponse> dishResponses = dishes.stream()
                .map(dish -> new DishResponse(dish.getId(), dish.getName(), dish.getImageUrl(), dish.getDescription()))
                .collect(Collectors.toList());

        return new MealPlanResponse(
                mealPlan.getId(),
                mealPlan.getMealDate(),
                mealPlan.getMealType(),
                mealPlan.getStatus(),
                mealPlan.getAdminReply(),
                dishResponses);
    }
}