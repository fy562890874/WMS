package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.User;
import com.xmut.entity.Warehouse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService extends IService<User> {
    // 移除所有方法声明中的public修饰符
    User getUserByUsername(String username);
    List<User> getAllUsers();
    @Transactional
    boolean addUser(User user);
    @Transactional
    boolean updateUser(User user);
    @Transactional
    boolean deleteUser(Integer userId);
    @Transactional
    boolean resetPassword(Integer userId, String password);
    @Transactional
    boolean assignRoles(Integer userId, List<Integer> roleIds);
    @Transactional
    boolean enableUser(Integer userId);
    @Transactional
    boolean disableUser(Integer userId);
    @Transactional
    boolean register(User user);
    boolean isUsernameExists(String username);
    @Transactional
    boolean changePassword(String oldPassword, String newPassword);
    List<Warehouse> getUserWarehouses(Integer userId);
    @Transactional
    boolean setUserWarehouses(Integer userId, List<Integer> warehouseIds);

    // 新增方法
    User findByUsername(String username);
    boolean checkPassword(User user, String password);
    
    List<Integer> getUserRoles(Integer userId);
    boolean isAdmin(Integer userId);
}