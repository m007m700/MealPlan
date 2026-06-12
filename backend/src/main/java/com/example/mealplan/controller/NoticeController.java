package com.example.mealplan.controller;

import com.example.mealplan.common.Result;
import com.example.mealplan.dto.request.NoticeAddRequest;
import com.example.mealplan.dto.response.NoticeResponse;
import com.example.mealplan.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public Result<List<NoticeResponse>> getRecentNotices(@RequestParam(defaultValue = "10") int limit) {
        List<NoticeResponse> notices = noticeService.getRecentNotices(limit);
        return Result.success(notices);
    }

    @PostMapping("/add")
    public Result<NoticeResponse> addNotice(@Valid @RequestBody NoticeAddRequest request, Authentication authentication) {
        Long senderId = (Long) authentication.getPrincipal();
        NoticeResponse notice = noticeService.addNotice(senderId, request);
        return Result.success(notice);
    }
}