package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.InventoryRecord;
import com.xmut.entity.Stock;
import com.xmut.mapper.InventoryRecordMapper;
import com.xmut.mapper.StockMapper;
import com.xmut.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryRecordMapper, InventoryRecord> implements InventoryService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public boolean stockIn(InventoryRecord record) {
        // 更新库存
        Stock stock = stockMapper.getStock(record.getProductId(), record.getWarehouseId());
        if (stock == null) {
            stock = new Stock();
            stock.setProductId(record.getProductId());
            stock.setWarehouseId(record.getWarehouseId());
            stock.setStockQuantity(record.getQuantity());
            stockMapper.addStock(stock);
        } else {
            stock.setStockQuantity(stock.getStockQuantity() + record.getQuantity());
            stockMapper.updateStock(stock);
        }
        // 添加入库记录
        record.setOperationType((byte) 0);
        int result = baseMapper.addInventoryRecord(record);
        return result > 0;
    }

    @Override
    public boolean stockOut(InventoryRecord record) {
        // 更新库存
        Stock stock = stockMapper.getStock(record.getProductId(), record.getWarehouseId());
        if (stock == null || stock.getStockQuantity() < record.getQuantity()) {
            return false;
        } else {
            stock.setStockQuantity(stock.getStockQuantity() - record.getQuantity());
            stockMapper.updateStock(stock);
        }
        // 添加出库记录
        record.setOperationType((byte) 1);
        int result = baseMapper.addInventoryRecord(record);
        return result > 0;
    }

    @Override
    public boolean transferStock(InventoryRecord outRecord, InventoryRecord inRecord) {
        // 出库
        boolean outResult = stockOut(outRecord);
        if (!outResult) {
            return false;
        }
        // 入库
        boolean inResult = stockIn(inRecord);
        if (!inResult) {
            return false;
        }
        // 设置关联记录ID
        outRecord.setRelatedRecordId(inRecord.getRecordId());
        inRecord.setRelatedRecordId(outRecord.getRecordId());
        baseMapper.updateInventoryRecord(outRecord);
        baseMapper.updateInventoryRecord(inRecord);
        return true;
    }

    @Override
    @Transactional
    public boolean transferStock(Integer productId, Integer fromWarehouseId, 
                               Integer toWarehouseId, Integer quantity,
                               Integer operatorId, String remark) {
        // 检查库存是否足够
        if (!checkStock(productId, fromWarehouseId, quantity)) {
            return false;
        }
        
        // 执行出库操作
        InventoryRecord outRecord = new InventoryRecord();
        outRecord.setProductId(productId);
        outRecord.setWarehouseId(fromWarehouseId);
        outRecord.setQuantity(quantity);
        outRecord.setOperationType((byte)1); // 出库
        outRecord.setOperatorId(operatorId);
        outRecord.setRemark("调拨出库：" + remark);
        
        boolean outResult = stockOut(outRecord);
        if (!outResult) {
            return false;
        }
        
        // 执行入库操作
        InventoryRecord inRecord = new InventoryRecord();
        inRecord.setProductId(productId);
        inRecord.setWarehouseId(toWarehouseId);
        inRecord.setQuantity(quantity);
        inRecord.setOperationType((byte)0); // 入库
        inRecord.setOperatorId(operatorId);
        inRecord.setRemark("调拨入库：" + remark);
        
        boolean inResult = stockIn(inRecord);
        if (!inResult) {
            throw new RuntimeException("调拨入库失败");
        }
        
        // 关联出入库记录
        outRecord.setRelatedRecordId(inRecord.getRecordId());
        inRecord.setRelatedRecordId(outRecord.getRecordId());
        baseMapper.updateInventoryRecord(outRecord);
        baseMapper.updateInventoryRecord(inRecord);
        
        return true;
    }

    @Override
    public List<Map<String, Object>> getStockChanges(Integer productId, 
                                                    String startTime, 
                                                    String endTime) {
        List<InventoryRecord> records = baseMapper.getInventoryRecordsByConditions(
            startTime, endTime, productId, null, null);
            
        List<Map<String, Object>> changes = new ArrayList<>();
        for (InventoryRecord record : records) {
            Map<String, Object> change = new HashMap<>();
            change.put("recordId", record.getRecordId());
            change.put("quantity", record.getQuantity());
            change.put("operationType", record.getOperationType());
            change.put("warehouseId", record.getWarehouseId());
            change.put("operatorId", record.getOperatorId());
            change.put("recordTime", record.getRecordTime());
            change.put("remark", record.getRemark());
            changes.add(change);
        }
        
        return changes;
    }

    @Override
    public List<InventoryRecord> getAllInventoryRecords() {
        return baseMapper.getAllInventoryRecords();
    }

    @Override
    public InventoryRecord getInventoryRecordById(Integer recordId) {
        return baseMapper.getInventoryRecordById(recordId);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordsByConditions(String startTime, String endTime, Integer productId, Integer warehouseId, Byte operationType) {
        return baseMapper.getInventoryRecordsByConditions(startTime, endTime, productId, warehouseId, operationType);
    }

    @Override
    public boolean checkStock(Integer productId, Integer warehouseId, Integer quantity) {
        Stock stock = stockMapper.getStock(productId, warehouseId);
        return stock != null && stock.getStockQuantity() >= quantity;
    }

    @Override
    public Integer getStock(Integer productId, Integer warehouseId) {
        Stock stock = stockMapper.getStock(productId, warehouseId);
        return stock != null ? stock.getStockQuantity() : 0;
    }
}