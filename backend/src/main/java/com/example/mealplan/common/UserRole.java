package com.example.mealplan.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN", "管理员"),
    USER("USER", "普通用户");

    @EnumValue
    @JsonValue
    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }
}