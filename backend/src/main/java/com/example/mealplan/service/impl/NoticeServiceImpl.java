package com.example.mealplan.service.impl;

import com.example.mealplan.dto.request.NoticeAddRequest;
import com.example.mealplan.dto.response.NoticeResponse;
import com.example.mealplan.entity.Notice;
import com.example.mealplan.entity.User;
import com.example.mealplan.mapper.NoticeMapper;
import com.example.mealplan.mapper.UserMapper;
import com.example.mealplan.service.NoticeService;
import com.example.mealplan.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private final UserMapper userMapper;
    private final WebSocketServer webSocketServer;

    @Override
    public List<NoticeResponse> getRecentNotices(int limit) {
        List<Notice> notices = noticeMapper.selectRecentNotices(limit);
        return notices.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NoticeResponse addNotice(Long senderId, NoticeAddRequest request) {
        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setSenderId(senderId);
        noticeMapper.insert(notice);

        webSocketServer.broadcastNotice(request.getContent());

        return convertToResponse(notice);
    }

    private NoticeResponse convertToResponse(Notice notice) {
        User sender = userMapper.selectById(notice.getSenderId());
        String senderName = sender != null ? sender.getNickname() : "系统";
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(), senderName, notice.getCreateTime());
    }
}