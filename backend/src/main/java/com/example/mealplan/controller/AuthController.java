package com.example.mealplan.controller;

import com.example.mealplan.common.Result;
import com.example.mealplan.dto.request.LoginRequest;
import com.example.mealplan.dto.response.LoginResponse;
import com.example.mealplan.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }
}