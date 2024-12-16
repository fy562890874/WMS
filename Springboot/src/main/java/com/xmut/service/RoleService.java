package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> getAllRoles();

    Role getRoleById(Integer roleId);

    boolean addRole(Role role);

    boolean updateRole(Role role);

    boolean deleteRole(Integer roleId);
}