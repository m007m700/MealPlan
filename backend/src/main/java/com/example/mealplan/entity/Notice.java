package com.example.mealplan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notice")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("sender_id")
    private Long senderId;

    @TableField("create_time")
    private LocalDateTime createTime;
}