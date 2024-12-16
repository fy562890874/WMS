package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Role;
import com.xmut.mapper.RoleMapper;
import com.xmut.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> getAllRoles() {
        return baseMapper.getAllRoles();
    }

    @Override
    public Role getRoleById(Integer roleId) {
        return baseMapper.getRoleById(roleId);
    }

    @Override
    public boolean addRole(Role role) {
        int result = baseMapper.addRole(role);
        return result > 0;
    }

    @Override
    public boolean updateRole(Role role) {
        int result = baseMapper.updateRole(role);
        return result > 0;
    }

    @Override
    public boolean deleteRole(Integer roleId) {
        int result = baseMapper.deleteRole(roleId);
        return result > 0;
    }
}