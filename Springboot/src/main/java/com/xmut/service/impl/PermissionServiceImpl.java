package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Permission;
import com.xmut.mapper.PermissionMapper;
import com.xmut.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public List<Permission> getAllPermissions() {
        return baseMapper.getAllPermissions();
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        return baseMapper.getPermissionsByRoleId(roleId);
    }

    @Override
    public boolean addPermission(Permission permission) {
        int result = baseMapper.addPermission(permission);
        return result > 0;
    }

    @Override
    public boolean updatePermission(Permission permission) {
        int result = baseMapper.updatePermission(permission);
        return result > 0;
    }

    @Override
    public boolean deletePermission(Integer permissionId) {
        int result = baseMapper.deletePermission(permissionId);
        return result > 0;
    }
}