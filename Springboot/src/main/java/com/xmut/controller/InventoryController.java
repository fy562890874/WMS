package com.xmut.controller;

import com.xmut.entity.InventoryRecord;
import com.xmut.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "出入库管理")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @ApiOperation("Stock in")
    @PostMapping("/stockIn")
    public String stockIn(@RequestBody InventoryRecord record) {
        boolean result = inventoryService.stockIn(record);
        return result ? "Stock in successful" : "Stock in failed";
    }

    @ApiOperation("Stock out")
    @PostMapping("/stockOut")
    public String stockOut(@RequestBody InventoryRecord record) {
        boolean result = inventoryService.stockOut(record);
        return result ? "Stock out successful" : "Stock out failed";
    }

    @ApiOperation("Transfer stock")
    @PostMapping("/transfer")
    public String transferStock(@RequestBody InventoryRecord outRecord, @RequestBody InventoryRecord inRecord) {
        boolean result = inventoryService.transferStock(outRecord, inRecord);
        return result ? "Transfer successful" : "Transfer failed";
    }

    @ApiOperation("Get all inventory records")
    @GetMapping("/records")
    public List<InventoryRecord> getAllInventoryRecords() {
        return inventoryService.getAllInventoryRecords();
    }

    @ApiOperation("Get inventory records by conditions")
    @GetMapping("/records/conditions")
    public List<InventoryRecord> getInventoryRecordsByConditions(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer warehouseId,
            @RequestParam(required = false) Byte operationType) {
        return inventoryService.getInventoryRecordsByConditions(startTime, endTime, productId, warehouseId, operationType);
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
}