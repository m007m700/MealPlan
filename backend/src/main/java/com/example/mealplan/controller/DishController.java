package com.example.mealplan.controller;

import com.example.mealplan.common.Result;
import com.example.mealplan.dto.request.DishAddRequest;
import com.example.mealplan.dto.response.DishResponse;
import com.example.mealplan.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping("/list")
    public Result<List<DishResponse>> getAllDishes() {
        List<DishResponse> dishes = dishService.getAllDishes();
        return Result.success(dishes);
    }

    @GetMapping("/{id}")
    public Result<DishResponse> getDishById(@PathVariable Long id) {
        DishResponse dish = dishService.getDishById(id);
        return Result.success(dish);
    }

    @PostMapping("/add")
    public Result<DishResponse> addDish(@Valid @RequestBody DishAddRequest request) {
        DishResponse dish = dishService.addDish(request);
        return Result.success(dish);
    }

    @PutMapping("/update/{id}")
    public Result<DishResponse> updateDish(@PathVariable Long id, @Valid @RequestBody DishAddRequest request) {
        DishResponse dish = dishService.updateDish(id, request);
        return Result.success(dish);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return Result.success();
    }
}