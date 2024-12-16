package com.xmut.config;

import com.xmut.entity.User;
import com.xmut.service.UserService;
import com.xmut.util.JwtUtils;
import com.xmut.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;  // 添加UserService注入

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("未登录");
        }

        // 验证token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Integer userId = jwtUtils.getUserIdFromToken(token);
            // 验证用户是否存在且状态正常
            User user = userService.getById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            if (!user.getStatus()) {
                throw new RuntimeException("用户已被禁用");
            }
            // 将用户ID存入上下文
            UserContext.setUserId(userId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("token无效");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理用户上下文
        UserContext.clear();
    }
}