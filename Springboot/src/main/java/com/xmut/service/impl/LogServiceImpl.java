package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Log;
import com.xmut.mapper.LogMapper;
import com.xmut.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Override
    public boolean addLog(Log log) {
        int result = baseMapper.addLog(log);
        return result > 0;
    }

    @Override
    public List<Log> getAllLogs() {
        return baseMapper.getAllLogs();
    }

    @Override
    public List<Log> getLogsByUserId(Integer userId) {
        return baseMapper.getLogsByUserId(userId);
    }

    @Override
    public List<Log> getLogsByConditions(Integer userId, String operationType, 
                                   String startTime, String endTime) {
        return baseMapper.getLogsByConditions(userId, operationType, startTime, endTime);
    }

    @Override
    public List<String> getOperationTypes() {
        return baseMapper.getOperationTypes();
    }
}