package com.example.mealplan.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderSubmitRequest {
    @NotNull(message = "菜单ID不能为空")
    private Long mealPlanId;

    @NotNull(message = "菜品列表不能为空")
    private List<Long> dishIds;
}