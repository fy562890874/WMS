package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.InventoryRecord;
import com.xmut.entity.Product;

import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

public interface ProductManagerService extends IService<Product> {

    // ProductService的方法
    List<Product> getAllProducts();

    Product getProductById(Integer productId);

    // 修改addProduct方法，移除Product对象中的warehouseId依赖
    boolean addProduct(Product product, Integer initialWarehouseId, Integer initialQuantity);

    boolean updateProduct(Product product);

    boolean deleteProduct(Integer productId);

    boolean adjustStock(Integer productId, Integer warehouseId, Integer quantity);

    boolean uploadImage(Integer productId, String imageUrl);

    List<Map<String, Object>> getProductsByCondition(String keyword, Integer categoryId, Integer warehouseId);

    // InventoryService的方法
    boolean stockIn(InventoryRecord record);

    boolean stockOut(InventoryRecord record);

    boolean transferStock(InventoryRecord outRecord, InventoryRecord inRecord);

    List<InventoryRecord> getAllInventoryRecords();

    InventoryRecord getInventoryRecordById(Integer recordId);

    List<InventoryRecord> getInventoryRecordsByConditions(String startTime, String endTime,
                                                          Integer productId, Integer warehouseId, Byte operationType);

    boolean checkStock(Integer productId, Integer warehouseId, Integer quantity);

    Integer getStock(Integer productId, Integer warehouseId);

    @Transactional
    boolean transferStock(Integer productId, Integer fromWarehouseId,
                          Integer toWarehouseId, Integer quantity,
                          Integer operatorId, String contactPerson, String remark);

    List<Map<String, Object>> getStockChanges(Integer productId,
                                              String startTime,
                                              String endTime);

    @Transactional
    boolean adjustStockWithRecord(Integer productId, Integer warehouseId, 
                                Integer quantity, InventoryRecord record);

    // 添加新方法
    List<Map<String, Object>> getInventoryRecordsWithDetails(String startTime, 
                                                           String endTime, 
                                                           Integer productId, 
                                                           Integer warehouseId, 
                                                           Byte operationType);
}
