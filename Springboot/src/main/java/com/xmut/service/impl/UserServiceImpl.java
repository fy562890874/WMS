package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.User;
import com.xmut.entity.Warehouse;
import com.xmut.mapper.UserMapper;
import com.xmut.mapper.UserRoleMapper;
import com.xmut.mapper.UserWarehouseMapper;
import com.xmut.mapper.WarehouseMapper;
import com.xmut.service.UserService;
import com.xmut.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired 
    private UserWarehouseMapper userWarehouseMapper;
    
    @Autowired
    private WarehouseMapper warehouseMapper;
    
    @Autowired
    private PasswordUtils passwordUtils;

    @Override
    public User getUserByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    @Override
    public List<User> getAllUsers() {
        return baseMapper.getAllUsers();
    }

    @Override
    public boolean addUser(User user) {
        int result = baseMapper.addUser(user);
        return result > 0;
    }

    @Override
    public boolean updateUser(User user) {
        int result = baseMapper.updateUser(user);
        return result > 0;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        int result = baseMapper.deleteUser(userId);
        return result > 0;
    }

    @Override
    public boolean resetPassword(Integer userId, String password) {
        String encodedPassword = passwordUtils.encode(password);
        // 修改返回值判断
        return baseMapper.resetPassword(userId, encodedPassword) > 0;
    }

    @Override
    public boolean assignRoles(Integer userId, List<Integer> roleIds) {
        userRoleMapper.deleteRolesByUserId(userId);
        for (Integer roleId : roleIds) {
            userRoleMapper.addUserRole(userId, roleId);
        }
        return true;
    }

    @Override
    public boolean enableUser(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(true);
        return updateUser(user);
    }

    @Override
    public boolean disableUser(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(false);
        return updateUser(user);
    }

    @Override
    public boolean register(User user) {
        // 加密密码
        user.setPassword(passwordUtils.encode(user.getPassword()));
        // 设置默认状态为启用
        user.setStatus(true);
        return addUser(user);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return getUserByUsername(username) != null;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // 验证旧密码
        if (!passwordUtils.matches(oldPassword, currentUser.getPassword())) {
            return false;
        }
        
        // 加密新密码并更新
        String encodedPassword = passwordUtils.encode(newPassword);
        return baseMapper.resetPassword(currentUser.getUserId(), encodedPassword) > 0;
    }

    private User getCurrentUser() {
        // Implement the logic to get the current user
        // This is just a placeholder implementation
        return null;
    }

    @Override
    public List<Warehouse> getUserWarehouses(Integer userId) {
        List<Integer> warehouseIds = userWarehouseMapper.getWarehouseIdsByUserId(userId);
        return warehouseIds.stream()
                .map(warehouseMapper::getWarehouseById)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean setUserWarehouses(Integer userId, List<Integer> warehouseIds) {
        // 先删除用户现有的仓库权限
        userWarehouseMapper.deleteWarehousesByUserId(userId);
        
        // 添加新的仓库权限
        for (Integer warehouseId : warehouseIds) {
            userWarehouseMapper.addUserWarehouse(userId, warehouseId);
        }
        return true;
    }

    @Override
    public User findByUsername(String username) {
        return getUserByUsername(username);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return passwordUtils.matches(password, user.getPassword());
    }
    
    @Override
    public List<Integer> getUserRoles(Integer userId) {
        return userRoleMapper.getRoleIdsByUserId(userId);
    }

    @Override
    public boolean isAdmin(Integer userId) {
        List<Integer> roleIds = getUserRoles(userId);
        // 假设角色ID为1是管理员角色（你需要根据实际情况修改这个判断条件）
        return roleIds.contains(1);
    }

}