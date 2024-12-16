package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.RolePermission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    // 根据角色ID查询权限ID列表
    @Select("SELECT permission_id FROM xmut_role_permission WHERE role_id = #{roleId}")
    List<Integer> getPermissionIdsByRoleId(@Param("roleId") Integer roleId);

    // 为角色分配权限
    @Insert("INSERT INTO xmut_role_permission(role_id, permission_id, create_time) " +
            "VALUES(#{roleId}, #{permissionId}, NOW())")
    int addRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    // 删除角色的权限
    @Delete("DELETE FROM xmut_role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    int deleteRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    // 删除角色的所有权限
    @Delete("DELETE FROM xmut_role_permission WHERE role_id = #{roleId}")
    int deletePermissionsByRoleId(@Param("roleId") Integer roleId);
}