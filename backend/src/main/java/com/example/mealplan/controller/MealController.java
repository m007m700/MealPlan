package com.example.mealplan.controller;

import com.example.mealplan.common.MealType;
import com.example.mealplan.common.Result;
import com.example.mealplan.dto.request.MealPublishRequest;
import com.example.mealplan.dto.response.MealPlanResponse;
import com.example.mealplan.service.MealPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealPlanService mealPlanService;

    @GetMapping("/today")
    public Result<MealPlanResponse> getTodayMeal(@RequestParam MealType mealType) {
        MealPlanResponse meal = mealPlanService.getTodayMeal(mealType);
        return Result.success(meal);
    }

    @GetMapping("/list")
    public Result<List<MealPlanResponse>> getRecentMeals(@RequestParam(defaultValue = "10") int limit) {
        List<MealPlanResponse> meals = mealPlanService.getRecentMeals(limit);
        return Result.success(meals);
    }

    @PostMapping("/publish")
    public Result<MealPlanResponse> publishMeal(@Valid @RequestBody MealPublishRequest request) {
        MealPlanResponse meal = mealPlanService.publishMeal(request);
        return Result.success(meal);
    }
}