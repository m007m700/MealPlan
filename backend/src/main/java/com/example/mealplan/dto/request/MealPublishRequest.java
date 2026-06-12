package com.example.mealplan.dto.request;

import com.example.mealplan.common.MealType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MealPublishRequest {
    @NotNull(message = "日期不能为空")
    private LocalDate mealDate;

    @NotNull(message = "餐型不能为空")
    private MealType mealType;

    @NotNull(message = "菜品列表不能为空")
    private List<Long> dishIds;
}