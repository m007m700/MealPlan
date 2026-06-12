package com.example.mealplan.dto.response;

import com.example.mealplan.common.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserRole role;
    private String nickname;
    private Long userId;
}