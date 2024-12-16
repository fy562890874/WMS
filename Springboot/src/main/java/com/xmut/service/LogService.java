package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Log;

import java.util.List;

public interface LogService extends IService<Log> {

    boolean addLog(Log log);

    List<Log> getAllLogs();

    List<Log> getLogsByUserId(Integer userId);

    List<String> getOperationTypes();

    List<Log> getLogsByConditions(Integer userId, String operationType, String startTime, String endTime);
}