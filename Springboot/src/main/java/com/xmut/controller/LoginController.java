package com.xmut.controller;

import com.xmut.entity.User;
import com.xmut.service.UserService;
import com.xmut.util.JwtUtils;
import com.xmut.util.PasswordUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordUtils passwordUtils; // 添加这行

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userService.getUserByUsername(username);
        if (user == null || !passwordUtils.matches(password, user.getPassword())) {
            return Map.of("code", 401, "message", "用户名或密码错误");
        }

        if (!user.getStatus()) {
            return Map.of("code", 403, "message", "账户已被禁用");
        }

        // 生成token
        String token = jwtUtils.generateToken(user.getUserId());

        // 清理敏感信息
        user.setPassword(null);

        return Map.of(
            "code", 200,
            "message", "登录成功",
            "data", Map.of(
                "token", token,
                "userInfo", user
            )
        );
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