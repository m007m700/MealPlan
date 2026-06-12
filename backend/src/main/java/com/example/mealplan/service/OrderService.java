package com.example.mealplan.service;

import com.example.mealplan.dto.request.OrderSubmitRequest;
import com.example.mealplan.dto.response.OrderHistoryResponse;
import com.example.mealplan.dto.response.OrderStatisticsResponse;

import java.util.List;

public interface OrderService {
    void submitOrder(Long userId, OrderSubmitRequest request);

    List<OrderStatisticsResponse> getStatistics(Long mealPlanId);

    void confirmMenu(Long mealPlanId, List<Long> dishIds, String adminReply);

    List<OrderHistoryResponse> getOrderHistory(Long userId);
}