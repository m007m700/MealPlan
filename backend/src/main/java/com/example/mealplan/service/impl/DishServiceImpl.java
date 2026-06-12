package com.example.mealplan.service.impl;

import com.example.mealplan.common.ResultCode;
import com.example.mealplan.dto.request.DishAddRequest;
import com.example.mealplan.dto.response.DishResponse;
import com.example.mealplan.entity.Dish;
import com.example.mealplan.exception.BusinessException;
import com.example.mealplan.mapper.DishMapper;
import com.example.mealplan.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;

    @Override
    public List<DishResponse> getAllDishes() {
        List<Dish> dishes = dishMapper.selectAllEnabled();
        return dishes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DishResponse getDishById(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BusinessException(ResultCode.DISH_NOT_FOUND);
        }
        return convertToResponse(dish);
    }

    @Override
    public DishResponse addDish(DishAddRequest request) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(request, dish);
        dish.setStatus(1);
        dishMapper.insert(dish);
        return convertToResponse(dish);
    }

    @Override
    public DishResponse updateDish(Long id, DishAddRequest request) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BusinessException(ResultCode.DISH_NOT_FOUND);
        }
        BeanUtils.copyProperties(request, dish);
        dishMapper.updateById(dish);
        return convertToResponse(dish);
    }

    @Override
    public void deleteDish(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BusinessException(ResultCode.DISH_NOT_FOUND);
        }
        dish.setStatus(0);
        dishMapper.updateById(dish);
    }

    private DishResponse convertToResponse(Dish dish) {
        return new DishResponse(dish.getId(), dish.getName(), dish.getImageUrl(), dish.getDescription());
    }
}