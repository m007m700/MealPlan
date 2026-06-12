package com.example.mealplan.service.impl;

import com.example.mealplan.common.ResultCode;
import com.example.mealplan.dto.request.LoginRequest;
import com.example.mealplan.dto.response.LoginResponse;
import com.example.mealplan.entity.User;
import com.example.mealplan.exception.BusinessException;
import com.example.mealplan.mapper.UserMapper;
import com.example.mealplan.service.UserService;
import com.example.mealplan.utils.JwtUtil;
import com.example.mealplan.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        log.info("用户 {} 尝试登录", request.getUsername());
        log.info("数据库密码: {}", user.getPassword());
        log.info("输入密码: {}", request.getPassword());
        boolean matches = PasswordUtil.matches(request.getPassword(), user.getPassword());
        log.info("密码匹配结果: {}", matches);
        
        if (!matches) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        return new LoginResponse(token, user.getRole(), user.getNickname(), user.getId());
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}