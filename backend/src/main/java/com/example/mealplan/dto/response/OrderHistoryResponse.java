package com.example.mealplan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResponse {
    private Long id;
    private Long mealPlanId;
    private String mealDate;
    private String mealType;
    private List<DishInfo> dishes;
    private String status;
    private String createTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DishInfo {
        private Long id;
        private String name;
        private String description;
    }
}
