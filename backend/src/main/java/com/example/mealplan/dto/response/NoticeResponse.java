package com.example.mealplan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private String senderName;
    private LocalDateTime createTime;
}