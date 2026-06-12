package com.example.mealplan.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketServer {

    private final WebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public void broadcastOrderSubmit(Long userId, java.util.List<Long> dishIds) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("dishIds", dishIds);

        WebSocketMessage message = new WebSocketMessage("ORDER_SUBMIT", data);
        broadcast(message);
    }

    public void broadcastMenuConfirm(Long mealPlanId, java.util.List<Long> dishIds) {
        Map<String, Object> data = new HashMap<>();
        data.put("mealPlanId", mealPlanId);
        data.put("dishIds", dishIds);

        WebSocketMessage message = new WebSocketMessage("MENU_CONFIRM", data);
        broadcast(message);
    }

    public void broadcastNotice(String content) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);

        WebSocketMessage message = new WebSocketMessage("NOTICE", data);
        broadcast(message);
    }

    private void broadcast(WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            webSocketHandler.broadcast(json);
            log.info("广播消息: {}", json);
        } catch (JsonProcessingException e) {
            log.error("消息序列化失败: ", e);
        }
    }
}