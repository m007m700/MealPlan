package com.example.mealplan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DishAddRequest {
    @NotBlank(message = "菜品名称不能为空")
    private String name;

    private String imageUrl;

    private String description;
}