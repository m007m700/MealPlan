package com.example.mealplan.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MealStatus {
    DRAFT("DRAFT", "草稿"),
    PUBLISHED("PUBLISHED", "已发布"),
    FINISHED("FINISHED", "已完成");

    @EnumValue
    @JsonValue
    private final String code;
    private final String description;

    MealStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}