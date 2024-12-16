package com.xmut.controller;

import com.xmut.entity.Warehouse;
import com.xmut.service.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "仓库管理")
@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @ApiOperation("Get all warehouses")
    @GetMapping
    public List<Warehouse> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }

    @ApiOperation("Add warehouse")
    @PostMapping
    public String addWarehouse(@RequestBody Warehouse warehouse) {
        boolean result = warehouseService.addWarehouse(warehouse);
        return result ? "Add successful" : "Add failed";
    }

    @ApiOperation("Update warehouse information")
    @PutMapping
    public String updateWarehouse(@RequestBody Warehouse warehouse) {
        boolean result = warehouseService.updateWarehouse(warehouse);
        return result ? "Update successful" : "Update failed";
    }

    @ApiOperation("Delete warehouse")
    @DeleteMapping("/{warehouseId}")
    public String deleteWarehouse(@PathVariable Integer warehouseId) {
        boolean result = warehouseService.deleteWarehouse(warehouseId);
        return result ? "Delete successful" : "Delete failed";
    }
}