package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Log;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<Log> {

    // 添加日志
    @Insert("INSERT INTO xmut_log(user_id, action, ip_address, create_time) " +
            "VALUES(#{userId}, #{action}, #{ipAddress}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    int addLog(Log log);

    // 查询所有日志
    @Select("SELECT * FROM xmut_log")
    List<Log> getAllLogs();

    // 根据用户ID查询日志
    @Select("SELECT * FROM xmut_log WHERE user_id = #{userId}")
    List<Log> getLogsByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM xmut_log WHERE 1=1 " +
            "${userId != null ? 'AND user_id = #{userId}' : ''} " +
            "${operationType != null ? 'AND action = #{operationType}' : ''} " +
            "${startTime != null ? 'AND create_time >= #{startTime}' : ''} " +
            "${endTime != null ? 'AND create_time <= #{endTime}' : ''} " +
            "ORDER BY create_time DESC")
    List<Log> getLogsByConditions(@Param("userId") Integer userId,
                                 @Param("operationType") String operationType,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime);

    @Select("SELECT DISTINCT action FROM xmut_log")
    List<String> getOperationTypes();
}