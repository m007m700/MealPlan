package com.example.mealplan.service;

import com.example.mealplan.dto.request.NoticeAddRequest;
import com.example.mealplan.dto.response.NoticeResponse;

import java.util.List;

public interface NoticeService {
    List<NoticeResponse> getRecentNotices(int limit);
    NoticeResponse addNotice(Long senderId, NoticeAddRequest request);
}