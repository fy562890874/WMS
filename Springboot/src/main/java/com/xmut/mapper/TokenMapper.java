package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Token;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TokenMapper extends BaseMapper<Token> {

    // 添加或更新 Token
    @Insert("INSERT INTO xmut_token(user_id, token, expire_time, update_time) " +
            "VALUES(#{userId}, #{token}, #{expireTime}, NOW()) " +
            "ON DUPLICATE KEY UPDATE token = #{token}, expire_time = #{expireTime}, update_time = NOW()")
    int insertOrUpdateToken(Token token);

    // 根据用户 ID 查询 Token
    @Select("SELECT * FROM xmut_token WHERE user_id = #{userId}")
    Token getTokenByUserId(@Param("userId") Integer userId);

    // 删除 Token
    @Delete("DELETE FROM xmut_token WHERE user_id = #{userId}")
    int deleteToken(@Param("userId") Integer userId);
}