package com.example.mealplan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoticeAddRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;
}