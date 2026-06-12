package com.example.mealplan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dish")
public class Dish {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("image_url")
    private String imageUrl;

    @TableField("description")
    private String description;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;
}