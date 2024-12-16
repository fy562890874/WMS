package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    List<Permission> getAllPermissions();

    List<Permission> getPermissionsByRoleId(Integer roleId);

    boolean addPermission(Permission permission);

    boolean updatePermission(Permission permission);

    boolean deletePermission(Integer permissionId);
}