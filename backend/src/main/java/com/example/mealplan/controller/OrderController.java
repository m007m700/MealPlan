package com.example.mealplan.controller;

import com.example.mealplan.common.Result;
import com.example.mealplan.dto.request.OrderSubmitRequest;
import com.example.mealplan.dto.response.OrderHistoryResponse;
import com.example.mealplan.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/submit")
    public Result<Void> submitOrder(@Valid @RequestBody OrderSubmitRequest request, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        orderService.submitOrder(userId, request);
        return Result.success();
    }

    @GetMapping("/history")
    public Result<List<OrderHistoryResponse>> getOrderHistory(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<OrderHistoryResponse> history = orderService.getOrderHistory(userId);
        return Result.success(history);
    }
}