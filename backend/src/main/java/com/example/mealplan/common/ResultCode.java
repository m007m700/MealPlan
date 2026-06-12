package com.example.mealplan.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "参数错误"),
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    TOKEN_INVALID(1004, "Token无效"),
    TOKEN_EXPIRED(1005, "Token已过期"),
    DISH_NOT_FOUND(2001, "菜品不存在"),
    MEAL_NOT_FOUND(3001, "菜单不存在"),
    MEAL_ALREADY_PUBLISHED(3002, "菜单已发布"),
    ORDER_ALREADY_SUBMITTED(4001, "已提交点餐"),
    NOTICE_NOT_FOUND(5001, "通知不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}