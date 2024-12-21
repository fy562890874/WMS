package com.xmut.controller;

import com.xmut.entity.User;
import com.xmut.service.UserService;
import com.xmut.util.JwtUtils;
import com.xmut.util.PasswordUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordUtils passwordUtils;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginForm) {
        String username = loginForm.get("username");
        String encodedPassword = loginForm.get("password");
        System.out.println("收到的登录请求: 用户名=" + username + ", 密码=" + encodedPassword);

        // 验证用户名和密码
        User user = userService.findByUsername(username);
        if (user != null) {
            System.out.println("找到用户: " + user);
            System.out.println("用户密码: " + user.getPassword());
            // 解码前端传来的 Base64 编码后的密码
            String decodedPassword = new String(Base64.getDecoder().decode(encodedPassword), StandardCharsets.UTF_8);
            System.out.println("解码后的密码: " + decodedPassword);
            boolean passwordMatches = passwordUtils.matches(decodedPassword, user.getPassword());
            System.out.println("密码匹配结果: " + passwordMatches);
            if (passwordMatches) {
                String token = jwtUtils.generateToken(user.getUserId());
                
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("username", user.getUsername());
                userInfo.put("realName", user.getRealName());
                userInfo.put("status", user.getStatus());
                userInfo.put("role", user.getRole());
                userInfo.put("isSuperAdmin", user.getRole() == 1);
                
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                data.put("userInfo", userInfo);
                
                return Map.of(
                    "code", 200,
                    "message", "登录成功",
                    "data", data
                );
            } else {
                System.out.println("密码不匹配或Base64解码失败");
                Map<String, Object> response = new HashMap<>();
                response.put("code", 401);
                response.put("message", "用户名或密码错误");
                return response;
            }
        } else {
            System.out.println("用户不存在");
            Map<String, Object> response = new HashMap<>();
            response.put("code", 401);
            response.put("message", "用户名或密码错误");
            return response;
        }
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        // 清除token等登录信息
        return Map.of("code", 200, "message", "退出成功");
    }

    @ApiOperation("刷新token")
    @PostMapping("/refresh-token")
    public Map<String, Object> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String newToken = jwtUtils.refreshToken(token);
            return Map.of("code", 200, "message", "刷新成功", "data", Map.of("token", newToken));
        } catch (RuntimeException e) {
            return Map.of("code", 400, "message", e.getMessage());
        }
    }
}