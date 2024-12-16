package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.InventoryRecord;

import java.util.List;

public interface InventoryService extends IService<InventoryRecord> {

    boolean stockIn(InventoryRecord record);

    boolean stockOut(InventoryRecord record);

    boolean transferStock(InventoryRecord outRecord, InventoryRecord inRecord);

    List<InventoryRecord> getAllInventoryRecords();

    InventoryRecord getInventoryRecordById(Integer recordId);

    List<InventoryRecord> getInventoryRecordsByConditions(String startTime, String endTime,
                                                          Integer productId, Integer warehouseId, Byte operationType);

    boolean checkStock(Integer productId, Integer warehouseId, Integer quantity);

    Integer getStock(Integer productId, Integer warehouseId);
}