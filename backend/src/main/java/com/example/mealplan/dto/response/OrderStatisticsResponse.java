package com.example.mealplan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsResponse {
    private Long dishId;
    private String dishName;
    private Integer count;
}