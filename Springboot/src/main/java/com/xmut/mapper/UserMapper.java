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

    // 修改添加用户的SQL，确保所有必需字段都有值
    @Insert("INSERT INTO xmut_user(username, password, real_name, role, status, create_time) " +
            "VALUES(#{username}, #{password}, #{realName}, #{role}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int addUser(User user);

    // 更新用户信息
    @Update("<script>"
        + "UPDATE xmut_user SET "
        + "<if test='realName != null'>real_name = #{realName}, </if>"
        + "<if test='role != null'>role = #{role}, </if>"
        + "<if test='status != null'>status = #{status}, </if>"
        + "update_time = NOW() "
        + "WHERE user_id = #{userId}"
        + "</script>")
    int updateUser(User user);

    // 删除用户
    @Delete("DELETE FROM xmut_user WHERE user_id = #{userId}")
    int deleteUser(@Param("userId") Integer userId);

    // 重置密码
    @Update("UPDATE xmut_user SET password = #{password}, update_time = NOW() WHERE user_id = #{userId}")
    int resetPassword(@Param("userId") Integer userId, @Param("password") String password);
    
    // 可以启用MyBatis的日志功能，通过配置文件进行详细日志记录
}