package com.example.mealplan.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MealType {
    BREAKFAST("BREAKFAST", "早餐"),
    LUNCH("LUNCH", "午餐"),
    DINNER("DINNER", "晚餐");

    @EnumValue
    @JsonValue
    private final String code;
    private final String description;

    MealType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}