package com.xmut.controller;

import com.xmut.entity.Warehouse;
import com.xmut.service.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Api(tags = "仓库管理")
@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @ApiOperation("Get all warehouses")
    @GetMapping
    public Map<String, Object> getAllWarehouses() {
        try {
            List<Warehouse> warehouses = warehouseService.getAllWarehouses();
            return Map.of(
                "code", 200,
                "data", warehouses,
                "message", "获取成功"
            );
        } catch (Exception e) {
            return Map.of(
                "code", 500,
                "message", "获取仓库列表失败"
            );
        }
    }

    @ApiOperation("Add warehouse")
    @PostMapping
    public Map<String, Object> addWarehouse(@RequestBody Warehouse warehouse) {
        try {
            warehouse.setCreateTime(LocalDateTime.now());
            boolean result = warehouseService.addWarehouse(warehouse);
            return result ? 
                Map.of("code", 200, "message", "添加成功") :
                Map.of("code", 500, "message", "添加失败");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "添加失败：" + e.getMessage());
        }
    }

    @ApiOperation("Update warehouse information")
    @PutMapping("/{warehouseId}")
    public Map<String, Object> updateWarehouse(@PathVariable Integer warehouseId, @RequestBody Warehouse warehouse) {
        try {
            warehouse.setWarehouseId(warehouseId);
            warehouse.setUpdateTime(LocalDateTime.now());
            boolean result = warehouseService.updateWarehouse(warehouse);
            return result ? 
                Map.of("code", 200, "message", "更新成功") :
                Map.of("code", 500, "message", "更新失败");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "更新失败：" + e.getMessage());
        }
    }

    @ApiOperation("Delete warehouse")
    @DeleteMapping("/{warehouseId}")
    public Map<String, Object> deleteWarehouse(@PathVariable Integer warehouseId) {
        try {
            boolean result = warehouseService.deleteWarehouse(warehouseId);
            return result ? 
                Map.of("code", 200, "message", "删除成功") :
                Map.of("code", 500, "message", "删除失败");
        } catch (Exception e) {
            return Map.of("code", 500, "message", "删除失败：" + e.getMessage());
        }
    }
}