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

@Api(tags = "用户管理")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("Get all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation("Add user")
    @PostMapping
    public String addUser(@RequestBody User user) {
        boolean result = userService.addUser(user);
        return result ? "Add successful" : "Add failed";
    }

    @ApiOperation("Update user information")
    @PutMapping
    public String updateUser(@RequestBody User user) {
        boolean result = userService.updateUser(user);
        return result ? "Update successful" : "Update failed";
    }

    @ApiOperation("Delete user")
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        boolean result = userService.deleteUser(userId);
        return result ? "Delete successful" : "Delete failed";
    }

    @ApiOperation("Reset password")
    @PostMapping("/resetPassword")
    public Map<String, Object> resetPassword(@RequestParam Integer userId, @RequestParam String newPassword) {
        boolean result = userService.resetPassword(userId, newPassword);
        return result ?
            Map.of("code", 200, "message", "密码重置成功") :
            Map.of("code", 500, "message", "密码重置失败");
    }

    @ApiOperation("Assign roles")
    @PostMapping("/assignRoles")
    public String assignRoles(@RequestParam Integer userId, @RequestBody List<Integer> roleIds) {
        boolean result = userService.assignRoles(userId, roleIds);
        return result ? "Roles assigned successfully" : "Roles assignment failed";
    }

    @ApiOperation("Enable user")
    @PostMapping("/enable/{userId}")
    public String enableUser(@PathVariable Integer userId) {
        boolean result = userService.enableUser(userId);
        return result ? "User enabled" : "Operation failed";
    }

    @ApiOperation("Disable user")
    @PostMapping("/disable/{userId}")
    public String disableUser(@PathVariable Integer userId) {
        boolean result = userService.disableUser(userId);
        return result ? "User disabled" : "Operation failed";
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.isUsernameExists(user.getUsername())) {
            return Map.of("code", 400, "message", "用户名已被占用");
        }
        
        boolean result = userService.register(user);
        return result ? 
            Map.of("code", 200, "message", "注册成功") :
            Map.of("code", 500, "message", "注册失败");
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/current")
    public Map<String, Object> getCurrentUser() {
        User user = SecurityUtils.getCurrentUser();
        assert user != null;
        user.setPassword(null); // 清除敏感信息
        return Map.of("code", 200, "data", user);
    }

    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public Map<String, Object> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        boolean result = userService.changePassword(oldPassword, newPassword);
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
}