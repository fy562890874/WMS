package com.xmut.controller;

import com.xmut.entity.Log;
import com.xmut.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "System Log Management")
@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @ApiOperation("Get all logs")
    @GetMapping
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }

    @ApiOperation("Get logs by user ID")
    @GetMapping("/user/{userId}")
    public List<Log> getLogsByUserId(@PathVariable Integer userId) {
        return logService.getLogsByUserId(userId);
    }

    @ApiOperation("条件查询日志")
    @GetMapping("/conditions")
    public List<Log> getLogsByConditions(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return logService.getLogsByConditions(userId, operationType, startTime, endTime);
    }

    @ApiOperation("获取操作类型列表")
    @GetMapping("/operationTypes")
    public List<String> getOperationTypes() {
        return logService.getOperationTypes();
    }
}