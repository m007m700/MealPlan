package com.example.mealplan.service;

import com.example.mealplan.dto.request.DishAddRequest;
import com.example.mealplan.dto.response.DishResponse;

import java.util.List;

public interface DishService {
    List<DishResponse> getAllDishes();
    DishResponse getDishById(Long id);
    DishResponse addDish(DishAddRequest request);
    DishResponse updateDish(Long id, DishAddRequest request);
    void deleteDish(Long id);
}