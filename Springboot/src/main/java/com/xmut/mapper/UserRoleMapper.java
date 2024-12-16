package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.UserRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    // 根据用户ID查询角色ID列表
    @Select("SELECT role_id FROM xmut_user_role WHERE user_id = #{userId}")
    List<Integer> getRoleIdsByUserId(@Param("userId") Integer userId);

    // 为用户分配角色
    @Insert("INSERT INTO xmut_user_role(user_id, role_id, create_time) VALUES(#{userId}, #{roleId}, NOW())")
    int addUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    // 删除用户的角色
    @Delete("DELETE FROM xmut_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int deleteUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    // 删除用户的所有角色
    @Delete("DELETE FROM xmut_user_role WHERE user_id = #{userId}")
    int deleteRolesByUserId(@Param("userId") Integer userId);
}