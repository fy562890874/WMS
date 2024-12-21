package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.User;
import com.xmut.entity.Warehouse;
import com.xmut.mapper.UserMapper;
import com.xmut.mapper.UserWarehouseMapper;
import com.xmut.mapper.WarehouseMapper;
import com.xmut.service.UserService;
import com.xmut.util.PasswordUtils;
import com.xmut.util.Base64Utils;
import com.xmut.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
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
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(User user) {
        // 添加校验
        if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            throw new IllegalArgumentException("用户姓名不能为空");
        }
        
        // 检查用户名是否已存在
        if (isUsernameExists(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 设置默认值
        user.setStatus(true);
        user.setRole(2); // 默认为信息管理员
        
        return baseMapper.addUser(user) > 0;
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
    public boolean resetPassword(Integer userId, String newPassword) {
        // 解码Base64加密的新密码
        String decodedNewPassword = new String(Base64Utils.decode(newPassword), StandardCharsets.UTF_8);
        return baseMapper.resetPassword(userId, decodedNewPassword) > 0;
    }

    @Override
    public boolean enableUser(Integer userId) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        user.setStatus(true);
        return updateUser(user);
    }

    @Override
    public boolean disableUser(Integer userId) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        user.setStatus(false);
        return updateUser(user);
    }

    @Override
    public boolean register(User user) {
        // 解码Base64加密的密码
        String decodedPassword = new String(Base64Utils.decode(user.getPassword()), StandardCharsets.UTF_8);
        // 设置密码为解码后的明文密码
        user.setPassword(decodedPassword);
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
        // 解码Base64加密的密码
        String decodedOldPassword = new String(Base64Utils.decode(oldPassword), StandardCharsets.UTF_8);
        String decodedNewPassword = new String(Base64Utils.decode(newPassword), StandardCharsets.UTF_8);
        
        logger.debug("解码后的原密码: {}", decodedOldPassword);
        logger.debug("解码后的新密码: {}", decodedNewPassword);
        
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            logger.error("当前用户为空，无法修改密码");
            return false;
        }
        
        logger.debug("当前用户: {}", currentUser);
        logger.debug("数据库中的密码: {}", currentUser.getPassword());
        
        // 验证旧密码
        if (!decodedOldPassword.equals(currentUser.getPassword())) {
            logger.warn("原密码验证失败");
            return false;
        }
        
        logger.debug("原密码验证成功");
        
        // 更新密码为解码后的新密码
        boolean updateResult = baseMapper.resetPassword(currentUser.getUserId(), decodedNewPassword) > 0;
        logger.debug("密码更新结果: {}", updateResult);
        
        return updateResult;
    }

    private User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
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
    public boolean isSuperAdmin(User user) {
        return user != null && user.getRole() != null && user.getRole() == 1;
    }

    @Override
    public boolean isAdmin(Integer userId) {        User user = getById(userId);
        return user != null && user.getRole() == 1;
    }

}