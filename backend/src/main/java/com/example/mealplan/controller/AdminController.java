package com.example.mealplan.controller;

import com.example.mealplan.common.Result;
import com.example.mealplan.dto.response.OrderStatisticsResponse;
import com.example.mealplan.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderService orderService;

    @GetMapping("/statistics")
    public Result<List<OrderStatisticsResponse>> getStatistics(@RequestParam Long mealPlanId) {
        List<OrderStatisticsResponse> statistics = orderService.getStatistics(mealPlanId);
        return Result.success(statistics);
    }

    @PostMapping("/confirm")
    public Result<Void> confirmMenu(@RequestParam Long mealPlanId,
                                     @RequestBody ConfirmMenuRequest request) {
        orderService.confirmMenu(mealPlanId, request.getDishIds(), request.getReply());
        return Result.success();
    }

    static class ConfirmMenuRequest {
        private List<Long> dishIds;
        private String reply;

        public List<Long> getDishIds() { return dishIds; }
        public void setDishIds(List<Long> dishIds) { this.dishIds = dishIds; }
        public String getReply() { return reply; }
        public void setReply(String reply) { this.reply = reply; }
    }
}
