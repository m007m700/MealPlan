package com.example.mealplan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.mealplan.common.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("nickname")
    private String nickname;

    @TableField("avatar")
    private String avatar;

    @TableField("role")
    private UserRole role;

    @TableField("create_time")
    private LocalDateTime createTime;
}