package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    // 查询所有角色
    @Select("SELECT * FROM xmut_role")
    List<Role> getAllRoles();

    // 根据角色ID查询角色
    @Select("SELECT * FROM xmut_role WHERE role_id = #{roleId}")
    Role getRoleById(@Param("roleId") Integer roleId);

    // 添加角色
    @Insert("INSERT INTO xmut_role(role_name, description, create_time) " +
            "VALUES(#{roleName}, #{description}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "roleId")
    int addRole(Role role);

    // 更新角色信息
    @Update("UPDATE xmut_role SET role_name = #{roleName}, description = #{description}, update_time = NOW() " +
            "WHERE role_id = #{roleId}")
    int updateRole(Role role);

    // 删除角色
    @Delete("DELETE FROM xmut_role WHERE role_id = #{roleId}")
    int deleteRole(@Param("roleId") Integer roleId);
}