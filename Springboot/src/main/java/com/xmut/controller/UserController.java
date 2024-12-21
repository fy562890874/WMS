package com.xmut.controller;

import com.xmut.entity.User;
import com.xmut.entity.Warehouse;
import com.xmut.service.UserService;
import com.xmut.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiOperation("Get all users")
    @GetMapping
    public Map<String, Object> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            // 处理用户角色信息
            List<Map<String, Object>> userList = users.stream().map(user -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userId", user.getUserId());
                userMap.put("username", user.getUsername());
                userMap.put("realName", user.getRealName());
                userMap.put("status", user.getStatus());
                userMap.put("createTime", user.getCreateTime());
                userMap.put("role", user.getRole());
                userMap.put("isSuperAdmin", user.getRole() == 1);
                return userMap;
            }).collect(Collectors.toList());
            
            return Map.of(
                "code", 200,
                "data", userList
            );
        } catch (Exception e) {
            return Map.of(
                "code", 500,
                "message", "获取用户列表失败"
            );
        }
    }

    @ApiOperation("Add user")
    @PostMapping
    public Map<String, Object> addUser(@RequestBody Map<String, Object> userData) {
        try {
            // 检查用户名是否已存在
            String username = (String) userData.get("username");
            if (userService.isUsernameExists(username)) {
                return Map.of(
                    "code", 400,
                    "message", "用户名已存在"
                );
            }

            // 创建用户对象
            User user = new User();
            user.setUsername(username);
            user.setPassword((String) userData.get("password"));
            user.setRealName((String) userData.get("realName"));
            user.setRole(2); // 默认设置为信息管理员
            user.setStatus(true);

            // 保存用户
            if (userService.addUser(user)) {
                // 返回创建的用户信息
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("username", user.getUsername());
                userInfo.put("realName", user.getRealName());
                userInfo.put("role", user.getRole());
                userInfo.put("status", user.getStatus());
                userInfo.put("createTime", user.getCreateTime());
                
                return Map.of(
                    "code", 200,
                    "message", "用户添加成功",
                    "data", userInfo
                );
            } else {
                throw new RuntimeException("添加用户失败");
            }
        } catch (Exception e) {
            return Map.of(
                "code", 500,
                "message", e.getMessage()
            );
        }
    }

    @ApiOperation("Update user information")
    @PutMapping("/{userId}")
    public Map<String, Object> updateUser(@PathVariable Integer userId, @RequestBody User user) {
        user.setUserId(userId);
        boolean result = userService.updateUser(user);
        return result ? 
            Map.of("code", 200, "message", "更新成功") :
            Map.of("code", 500, "message", "更新失败");
    }

    @ApiOperation("Delete user")
    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Integer userId) {
        boolean result = userService.deleteUser(userId);
        return result ? 
            Map.of("code", 200, "message", "删除成功") :
            Map.of("code", 500, "message", "删除失败");
    }

    @ApiOperation("Reset password")
    @PostMapping("/resetPassword")
    public Map<String, Object> resetPassword(@RequestParam Integer userId, @RequestParam String newPassword) {
        boolean result = userService.resetPassword(userId, newPassword);
        return result ?
            Map.of("code", 200, "message", "密码重置成功") :
            Map.of("code", 500, "message", "密码重置失败");
    }

//    @ApiOperation("Assign roles")
//    @PostMapping("/assignRoles")
//    public String assignRoles(@RequestParam Integer userId, @RequestBody List<Integer> roleIds) {
//        boolean result = userService.(userId, roleIds);
//        return result ? "Roles assigned successfully" : "Roles assignment failed";
//    }

    @ApiOperation("Enable user")
    @PostMapping("/enable/{userId}")
    public Map<String, Object> enableUser(@PathVariable Integer userId) {
        boolean result = userService.enableUser(userId);
        return result ? 
            Map.of("code", 200, "message", "启用成功") :
            Map.of("code", 500, "message", "操作失败");
    }

    @ApiOperation("Disable user")
    @PostMapping("/disable/{userId}")
    public Map<String, Object> disableUser(@PathVariable Integer userId) {
        boolean result = userService.disableUser(userId);
        return result ? 
            Map.of("code", 200, "message", "禁用成功") :
            Map.of("code", 500, "message", "操作失败");
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> userData) {
        try {
            String username = (String) userData.get("username");
            if (userService.isUsernameExists(username)) {
                return Map.of("code", 400, "message", "用户名已被占用");
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword((String) userData.get("password"));
            user.setRealName((String) userData.get("realName"));
            user.setRole((Integer) userData.get("role"));
            user.setStatus(true);

            boolean result = userService.register(user);
            return result ? 
                Map.of("code", 200, "message", "注册成功") :
                Map.of("code", 500, "message", "注册失败");
        } catch (Exception e) {
            return Map.of("code", 500, "message", e.getMessage());
        }
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/current")
    public Map<String, Object> getCurrentUser() {
        try {
            User user = SecurityUtils.getCurrentUser();
            if (user == null) {
                return Map.of("code", 401, "message", "用户未登录或登录已过期");
            }
            
            // 直接使用 user 的 role 字段判断是否为超级管理员
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("username", user.getUsername());
            userInfo.put("realName", user.getRealName());
            userInfo.put("status", user.getStatus());
            userInfo.put("role", user.getRole());
            userInfo.put("isSuperAdmin", user.getRole() == 1);
            
            return Map.of("code", 200, "data", userInfo);
        } catch (Exception e) {
            return Map.of("code", 500, "message", "获取用户信息失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        logger.debug("收到修改密码请求 - 原密码: {}, 新密码: {}", oldPassword, newPassword);
        
        if (oldPassword == null || newPassword == null) {
            logger.warn("参数错误: 原密码或新密码为空");
            return Map.of("code", 400, "message", "参数错误");
        }
        
        boolean result = userService.changePassword(oldPassword, newPassword);
        logger.debug("修改密码结果: {}", result);
        
        return result ? 
            Map.of("code", 200, "message", "密码修改成功") :
            Map.of("code", 400, "message", "原密码错误");
    }

    @ApiOperation("获取用户仓库权限")
    @GetMapping("/{userId}/warehouses")
    public List<Warehouse> getUserWarehouses(@PathVariable Integer userId) {
        return userService.getUserWarehouses(userId);
    }

    @ApiOperation("设置用户仓库权限")
    @PostMapping("/{userId}/warehouses")
    public String setUserWarehouses(
            @PathVariable Integer userId,
            @RequestBody List<Integer> warehouseIds) {
        boolean result = userService.setUserWarehouses(userId, warehouseIds);
        return result ? "权限设置成功" : "权限设置失败";
    }

    @ApiOperation("获取用户详情")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserById(@PathVariable Integer userId) {
        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword(null); // 清除敏感信息
            return Map.of("code", 200, "data", user);
        }
        return Map.of("code", 404, "message", "用户不存在");
    }

    @ApiOperation("更新个人信息")
    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestBody User user) {
        try {
            User currentUser = SecurityUtils.getCurrentUser();
            if (currentUser == null) {
                return Map.of("code", 401, "message", "未登录");
            }

            // 只允许更新 realName
            currentUser.setRealName(user.getRealName());
            
            boolean result = userService.updateUser(currentUser);
            if (result) {
                // 更新成功后，重新获取完整的用户信息返回
                User updatedUser = userService.getById(currentUser.getUserId());
                User userInfo = new User();
                userInfo.setUserId(updatedUser.getUserId());
                userInfo.setUsername(updatedUser.getUsername());
                userInfo.setRealName(updatedUser.getRealName());
                userInfo.setStatus(updatedUser.getStatus());
                
                return Map.of(
                    "code", 200, 
                    "message", "更新成功",
                    "data", userInfo
                );
            }
            return Map.of("code", 500, "message", "更新失败");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "更新失败: " + e.getMessage());
        }
    }
}