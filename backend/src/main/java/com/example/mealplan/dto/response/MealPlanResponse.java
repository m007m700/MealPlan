package com.example.mealplan.dto.response;

import com.example.mealplan.common.MealStatus;
import com.example.mealplan.common.MealType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanResponse {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate mealDate;

    private MealType mealType;
    private MealStatus status;
    private String adminReply;
    private List<DishResponse> dishes;
}