package com.example.mealplan.service;

import com.example.mealplan.common.MealType;
import com.example.mealplan.dto.request.MealPublishRequest;
import com.example.mealplan.dto.response.MealPlanResponse;

import java.util.List;

public interface MealPlanService {
    MealPlanResponse getTodayMeal(MealType mealType);
    MealPlanResponse publishMeal(MealPublishRequest request);
    List<MealPlanResponse> getRecentMeals(int limit);
}