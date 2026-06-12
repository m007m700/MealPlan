package com.example.mealplan.service;

import com.example.mealplan.dto.request.LoginRequest;
import com.example.mealplan.dto.response.LoginResponse;
import com.example.mealplan.entity.User;

public interface UserService {
    LoginResponse login(LoginRequest request);
    User getUserById(Long id);
    User getUserByUsername(String username);
}