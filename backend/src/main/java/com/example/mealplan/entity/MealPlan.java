package com.example.mealplan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.mealplan.common.MealStatus;
import com.example.mealplan.common.MealType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("meal_plan")
public class MealPlan {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("meal_date")
    private LocalDate mealDate;

    @TableField("meal_type")
    private MealType mealType;

    @TableField("status")
    private MealStatus status;

    @TableField("admin_reply")
    private String adminReply;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<Dish> dishes;
}