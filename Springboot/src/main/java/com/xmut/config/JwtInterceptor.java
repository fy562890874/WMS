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
import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/auth/login") || requestURI.equals("/auth/logout") || requestURI.equals("/auth/refresh-token")) {
            return true; // 不拦截登录、登出和刷新 token 请求
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录\"}");
            return false;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Integer userId = jwtUtils.getUserIdFromToken(token);
            User user = userService.getById(userId);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"用户不存在\"}");
                return false;
            }
            if (!user.getStatus()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"用户已被禁用\"}");
                return false;
            }
            UserContext.setUserId(userId);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token无效\"}");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}