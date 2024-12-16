package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    @Select("SELECT * FROM xmut_user WHERE username = #{username}")
    User getUserByUsername(@Param("username") String username);

    // 查询所有用户
    @Select("SELECT * FROM xmut_user")
    List<User> getAllUsers();

    // 添加用户
    @Insert("INSERT INTO xmut_user(username, password, real_name, status, create_time) " +
            "VALUES(#{username}, #{password}, #{realName}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int addUser(User user);

    // 更新用户信息
    @Update("UPDATE xmut_user SET real_name = #{realName}, status = #{status}, update_time = NOW() " +
            "WHERE user_id = #{userId}")
    int updateUser(User user);

    // 删除用户
    @Delete("DELETE FROM xmut_user WHERE user_id = #{userId}")
    int deleteUser(@Param("userId") Integer userId);

    // 重置密码
    @Update("UPDATE xmut_user SET password = #{password}, update_time = NOW() WHERE user_id = #{userId}")
    int resetPassword(@Param("userId") Integer userId, @Param("password") String password);
}