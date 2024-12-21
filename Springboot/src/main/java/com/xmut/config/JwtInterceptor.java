package com.xmut.config;

import com.xmut.entity.User;
import com.xmut.service.UserService;
import com.xmut.util.JwtUtils;
import com.xmut.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        logger.debug("当前请求URI: {}", requestURI);
        
        if (requestURI.equals("/auth/login") || requestURI.equals("/auth/logout") || requestURI.equals("/auth/refresh-token")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            sendUnauthorizedResponse(response, "未登录");
            return false;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Integer userId = jwtUtils.getUserIdFromToken(token);
            logger.debug("从token中解析出的userId: {}", userId);
            
            User user = userService.getById(userId);
            if (user == null) {
                sendUnauthorizedResponse(response, "用户不存在");
                return false;
            }
            
            if (!user.getStatus()) {
                sendForbiddenResponse(response, "用户已被禁用");
                return false;
            }

            // 获取用户名并存入上下文
            String username = jwtUtils.getUsernameFromToken(token);
            UserContext.setUserIdAndName(userId, username);
            logger.debug("已将用户ID: {} 和用户名: {} 设置到上下文中", userId, username);
            return true;
            
        } catch (Exception e) {
            logger.error("Token验证失败", e);
            sendUnauthorizedResponse(response, "token无效");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
        logger.debug("已清除用户上下文");
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        sendJsonResponse(response, 401, message);
    }

    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        sendJsonResponse(response, 403, message);
    }

    private void sendJsonResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"code\":%d,\"message\":\"%s\"}", code, message));
    }
}