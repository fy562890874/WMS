package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Permission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    // 查询所有权限
    @Select("SELECT * FROM xmut_permission")
    List<Permission> getAllPermissions();

    // 根据角色ID查询权限列表
    @Select("SELECT p.* FROM xmut_permission p " +
            "INNER JOIN xmut_role_permission rp ON p.permission_id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<Permission> getPermissionsByRoleId(@Param("roleId") Integer roleId);

    // 添加权限
    @Insert("INSERT INTO xmut_permission(permission_name, permission_code, description, create_time) " +
            "VALUES(#{permissionName}, #{permissionCode}, #{description}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "permissionId")
    int addPermission(Permission permission);

    // 更新权限信息
    @Update("UPDATE xmut_permission SET permission_name = #{permissionName}, " +
            "permission_code = #{permissionCode}, description = #{description}, update_time = NOW() " +
            "WHERE permission_id = #{permissionId}")
    int updatePermission(Permission permission);

    // 删除权限
    @Delete("DELETE FROM xmut_permission WHERE permission_id = #{permissionId}")
    int deletePermission(@Param("permissionId") Integer permissionId);
}