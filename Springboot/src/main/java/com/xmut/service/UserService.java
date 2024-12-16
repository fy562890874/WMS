package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.User;
import com.xmut.entity.Warehouse;

import java.util.List;

public interface UserService extends IService<User> {
    // 移除所有方法声明中的public修饰符
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(Integer userId);
    boolean resetPassword(Integer userId, String password);
    boolean assignRoles(Integer userId, List<Integer> roleIds);
    boolean enableUser(Integer userId);
    boolean disableUser(Integer userId);
    boolean register(User user);
    boolean isUsernameExists(String username);
    boolean changePassword(String oldPassword, String newPassword);
    List<Warehouse> getUserWarehouses(Integer userId);
    boolean setUserWarehouses(Integer userId, List<Integer> warehouseIds);
}