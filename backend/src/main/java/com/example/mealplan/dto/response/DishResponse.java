package com.example.mealplan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
}