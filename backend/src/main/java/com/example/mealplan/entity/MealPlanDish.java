package com.example.mealplan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("meal_plan_dish")
public class MealPlanDish {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("meal_plan_id")
    private Long mealPlanId;

    @TableField("dish_id")
    private Long dishId;
}