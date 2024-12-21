package com.xmut.controller;

import com.xmut.entity.InventoryRecord;
import com.xmut.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "出入库管理")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @ApiOperation("入库操作")
    @PostMapping("/stockIn")
    public Map<String, Object> stockIn(@RequestBody InventoryRecord record) {
        Map<String, Object> response = new HashMap<>();
        boolean result = inventoryService.stockIn(record);
        response.put("success", result);
        response.put("message", result ? "入库成功" : "入库失败");
        return response;
    }

    @ApiOperation("出库操作")
    @PostMapping("/stockOut")
    public Map<String, Object> stockOut(@RequestBody InventoryRecord record) {
        Map<String, Object> response = new HashMap<>();
        boolean result = inventoryService.stockOut(record);
        response.put("success", result);
        response.put("message", result ? "出库成功" : "出库失败");
        return response;
    }

    @ApiOperation("调拨操作")
    @PostMapping("/transfer")
    public Map<String, Object> transferStock(@RequestBody Map<String, Object> params) {
        Integer productId = (Integer) params.get("productId");
        Integer fromWarehouseId = (Integer) params.get("fromWarehouseId");
        Integer toWarehouseId = (Integer) params.get("toWarehouseId");
        Integer quantity = (Integer) params.get("quantity");
        Integer operatorId = (Integer) params.get("operatorId");
        String remark = (String) params.get("remark");

        Map<String, Object> response = new HashMap<>();
        boolean result = inventoryService.transferStock(
            productId, fromWarehouseId, toWarehouseId, quantity, operatorId, remark);
        response.put("success", result);
        response.put("message", result ? "调拨成功" : "调拨失败");
        return response;
    }

    @ApiOperation("获取出入库记录")
    @GetMapping("/records")
    public Map<String, Object> getInventoryRecords(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer warehouseId,
            @RequestParam(required = false) Byte operationType) {
        Map<String, Object> response = new HashMap<>();
        List<InventoryRecord> records = inventoryService.getInventoryRecordsByConditions(
                startTime, endTime, productId, warehouseId, operationType);
        response.put("success", true);
        response.put("data", records);
        return response;
    }

    @ApiOperation("检查库存")
    @GetMapping("/check")
    public Map<String, Object> checkStock(
            @RequestParam Integer productId,
            @RequestParam Integer warehouseId,
            @RequestParam Integer quantity) {
        boolean isEnough = inventoryService.checkStock(productId, warehouseId, quantity);
        return Map.of(
            "isEnough", isEnough,
            "currentStock", inventoryService.getStock(productId, warehouseId)
        );
    }

    @ApiOperation("获取库存详情")
    @GetMapping("/stock")
    public Integer getStock(
            @RequestParam Integer productId,
            @RequestParam Integer warehouseId) {
        return inventoryService.getStock(productId, warehouseId);
    }

    @ApiOperation("获取库存变动记录")
    @GetMapping("/changes")
    public Map<String, Object> getStockChanges(
            @RequestParam Integer productId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> changes = inventoryService.getStockChanges(
            productId, startTime, endTime);
        response.put("success", true);
        response.put("data", changes);
        return response;
    }
}