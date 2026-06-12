package com.example.mealplan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_record")
public class OrderRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("meal_plan_id")
    private Long mealPlanId;

    @TableField("user_id")
    private Long userId;

    @TableField("dish_id")
    private Long dishId;

    @TableField("create_time")
    private LocalDateTime createTime;
}