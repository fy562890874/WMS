package com.xmut.controller;

import com.xmut.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "数据统计分析")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation("获取企业层面统计数据")
    @GetMapping("/company/overview")
    public Map<String, Object> getCompanyOverview(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        return statisticsService.getCompanyOverview(startTime, endTime);
    }

    @ApiOperation("获取企业货物排行")
    @GetMapping("/company/topProducts")
    public List<Map<String, Object>> getCompanyTopProducts(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        return statisticsService.getCompanyTopProducts(startTime, endTime);
    }

    @ApiOperation("获取仓库统计数据")
    @GetMapping("/warehouse/{warehouseId}/overview")
    public Map<String, Object> getWarehouseOverview(
            @PathVariable Integer warehouseId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        return statisticsService.getWarehouseOverview(warehouseId, startTime, endTime);
    }

    @ApiOperation("获取仓库货物排行")
    @GetMapping("/warehouse/{warehouseId}/topProducts")
    public List<Map<String, Object>> getWarehouseTopProducts(
            @PathVariable Integer warehouseId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        return statisticsService.getWarehouseTopProducts(warehouseId, startTime, endTime);
    }
}
