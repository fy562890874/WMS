package com.xmut.controller;

import com.xmut.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "系统管理")
@RestController
@RequestMapping("/system")
public class SystemController {
    
    @Autowired
    private SystemService systemService;

    @ApiOperation("获取系统概览数据")
    @GetMapping("/dashboard/overview")
    public Map<String, Object> getDashboardOverview() {
        return systemService.getDashboardOverview();
    }

    @ApiOperation("获取最新系统消息")
    @GetMapping("/messages/latest")
    public List<Map<String, Object>> getLatestMessages() {
        return systemService.getLatestMessages();
    }

    @ApiOperation("获取系统信息")
    @GetMapping("/info")
    public Map<String, Object> getSystemInfo() {
        return systemService.getSystemInfo();
    }
}
