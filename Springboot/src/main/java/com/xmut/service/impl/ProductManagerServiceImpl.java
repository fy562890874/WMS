package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.InventoryRecord;
import com.xmut.entity.Product;
import com.xmut.entity.Stock;
import com.xmut.entity.Warehouse;
import com.xmut.mapper.ProductManagerMapper;
import com.xmut.service.ProductManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductManagerServiceImpl extends ServiceImpl<ProductManagerMapper, Product> implements ProductManagerService {

    @Autowired
    private ProductManagerMapper productManagerMapper;

    // 实现ProductService的方法

    @Override
    public List<Product> getAllProducts() {
        return productManagerMapper.getAllProducts();
    }

    @Override
    public Product getProductById(Integer productId) {
        return productManagerMapper.getProductById(productId);
    }

    @Override
    public boolean addProduct(Product product, Integer initialWarehouseId, Integer initialQuantity) {
        // 验证类别是否存在
        if (productManagerMapper.getCategoryById(product.getCategoryId()) == null) {
            throw new RuntimeException("类别不存在");
        }

        // 验证初始仓库
        if (initialWarehouseId == null) {
            throw new RuntimeException("必须指定初始仓库");
        }

        if (productManagerMapper.getWarehouseById(initialWarehouseId) == null) {
            throw new RuntimeException("指定的仓库不存在");
        }

        int result = productManagerMapper.addProduct(product);
        if (result > 0) {
            // 初始化库存记录
            Stock stock = new Stock();
            stock.setProductId(product.getProductId());
            stock.setWarehouseId(initialWarehouseId);
            stock.setStockQuantity(initialQuantity != null ? initialQuantity : 0);
            return productManagerMapper.addStock(stock) > 0;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateProduct(Product product) {
        // 验证产品是否存在
        Product existingProduct = productManagerMapper.getProductById(product.getProductId());
        if (existingProduct == null) {
            throw new RuntimeException("产品不存在");
        }
        
        return productManagerMapper.updateProduct(product) > 0;
    }

    @Transactional
    @Override
    public boolean deleteProduct(Integer productId) {
        // 验证产品是否存在
        Product product = productManagerMapper.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("产品不存在，产品ID: " + productId);
        }

        // 删除相关的库存记录
        List<Stock> stocks = productManagerMapper.getStocksByProductId(productId);
        for (Stock stock : stocks) {
            productManagerMapper.deleteStock(stock.getId());
        }
        
        return productManagerMapper.deleteProduct(productId) > 0;
    }

    @Transactional
    @Override
    public boolean adjustStock(Integer productId, Integer warehouseId, Integer quantity) {
        // 验证产品和仓库是否存在
        Product product = productManagerMapper.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("产品不存在");
        }

        if (productManagerMapper.getWarehouseById(warehouseId) == null) {
            throw new RuntimeException("仓库不存在");
        }

        Stock stock = productManagerMapper.getStock(productId, warehouseId);
        if (stock == null) {
            // 如果不存在则新增库存记录
            stock = new Stock();
            stock.setProductId(productId);
            stock.setWarehouseId(warehouseId);
            stock.setStockQuantity(quantity);
            return productManagerMapper.addStock(stock) > 0;
        } else {
            // 更新库存
            stock.setStockQuantity(quantity);
            return productManagerMapper.updateStock(stock) > 0;
        }
    }

    @Override
    @Transactional
    public boolean adjustStockWithRecord(Integer productId, Integer warehouseId, Integer quantity, InventoryRecord record) {
        try {
            // 1. 验证产品和仓库是否存在
            Product product = productManagerMapper.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("产品不存在，产品ID: " + productId);
            }
            Warehouse warehouse = productManagerMapper.getWarehouseById(warehouseId);
            if (warehouse == null) {
                throw new RuntimeException("仓库不存在，仓库ID: " + warehouseId);
            }

            // 2. 获取当前库存
            Stock stock = productManagerMapper.getStock(productId, warehouseId);
            int oldQuantity = stock != null ? stock.getStockQuantity() : 0;
            
            // 3. 根据库存变化设置操作类型
            if (quantity > oldQuantity) {
                record.setOperationType((byte) 0); // 入库
                record.setQuantity(quantity - oldQuantity);
            } else if (quantity < oldQuantity) {
                record.setOperationType((byte) 1); // 出库
                record.setQuantity(oldQuantity - quantity);
            } else {
                return true; // 库存没有变化
            }

            // 4. 更新或创建库存记录
            if (stock == null) {
                stock = new Stock();
                stock.setProductId(productId);
                stock.setWarehouseId(warehouseId);
                stock.setStockQuantity(quantity);
                productManagerMapper.addStock(stock);
            } else {
                stock.setStockQuantity(quantity);
                productManagerMapper.updateStock(stock);
            }

            // 5. 添加库存变动记录
            productManagerMapper.addInventoryRecord(record);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("调整库存失败: " + e.getMessage());
        }
    }

    @Override
    public boolean uploadImage(Integer productId, String imageUrl) {
        Product product = new Product();
        product.setProductId(productId);
        product.setImageUrl(imageUrl);
        return productManagerMapper.updateProduct(product) > 0;
    }

    @Override
    public List<Map<String, Object>> getProductsByCondition(String keyword, Integer categoryId, Integer warehouseId) {
        return productManagerMapper.getProductsByCondition(keyword, categoryId, warehouseId);
    }

    // 实现InventoryService的方法

    @Override
    public boolean stockIn(InventoryRecord record) {
        // 更新库存
        Stock stock = productManagerMapper.getStock(record.getProductId(), record.getWarehouseId());
        if (stock == null) {
            stock = new Stock();
            stock.setProductId(record.getProductId());
            stock.setWarehouseId(record.getWarehouseId());
            stock.setStockQuantity(record.getQuantity());
            productManagerMapper.addStock(stock);
        } else {
            stock.setStockQuantity(stock.getStockQuantity() + record.getQuantity());
            productManagerMapper.updateStock(stock);
        }
        // 添加入库记录
        record.setOperationType((byte) 0);
        int result = productManagerMapper.addInventoryRecord(record);
        return result > 0;
    }

    @Override
    public boolean stockOut(InventoryRecord record) {
        // 更新库存
        Stock stock = productManagerMapper.getStock(record.getProductId(), record.getWarehouseId());
        if (stock == null || stock.getStockQuantity() < record.getQuantity()) {
            return false;
        } else {
            stock.setStockQuantity(stock.getStockQuantity() - record.getQuantity());
            productManagerMapper.updateStock(stock);
        }
        // 添加出库记录
        record.setOperationType((byte) 1);
        int result = productManagerMapper.addInventoryRecord(record);
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
        productManagerMapper.updateInventoryRecord(outRecord);
        productManagerMapper.updateInventoryRecord(inRecord);
        return true;
    }

    @Override
    @Transactional
    public boolean transferStock(Integer productId, Integer fromWarehouseId,
                                 Integer toWarehouseId, Integer quantity,
                                 Integer operatorId, String contactPerson, String remark) {
        // 检查库存是否足够
        if (!checkStock(productId, fromWarehouseId, quantity)) {
            return false;
        }

        // 出库记录
        InventoryRecord outRecord = new InventoryRecord();
        outRecord.setProductId(productId);
        outRecord.setWarehouseId(fromWarehouseId);
        outRecord.setQuantity(quantity);
        outRecord.setOperationType((byte) 1); // 出库
        outRecord.setOperatorId(operatorId);
        outRecord.setContactPerson(contactPerson);
        outRecord.setRemark("调拨出库：" + remark);

        // 入库记录
        InventoryRecord inRecord = new InventoryRecord();
        inRecord.setProductId(productId);
        inRecord.setWarehouseId(toWarehouseId);
        inRecord.setQuantity(quantity);
        inRecord.setOperationType((byte) 0); // 入库
        inRecord.setOperatorId(operatorId);
        inRecord.setContactPerson(contactPerson);
        inRecord.setRemark("调拨入库：" + remark);

        // 执行调拨
        return transferStock(outRecord, inRecord);
    }

    @Override
    public List<Map<String, Object>> getStockChanges(Integer productId,
                                                     String startTime,
                                                     String endTime) {
        List<InventoryRecord> records = productManagerMapper.getInventoryRecordsByConditions(
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
        return productManagerMapper.getAllInventoryRecords();
    }

    @Override
    public InventoryRecord getInventoryRecordById(Integer recordId) {
        return productManagerMapper.getInventoryRecordById(recordId);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordsByConditions(String startTime, String endTime,
                                                                 Integer productId, Integer warehouseId, Byte operationType) {
        return productManagerMapper.getInventoryRecordsByConditions(startTime, endTime, productId, warehouseId, operationType);
    }

    @Override
    public boolean checkStock(Integer productId, Integer warehouseId, Integer quantity) {
        Stock stock = productManagerMapper.getStock(productId, warehouseId);
        return stock != null && stock.getStockQuantity() >= quantity;
    }

    @Override
    public Integer getStock(Integer productId, Integer warehouseId) {
        Stock stock = productManagerMapper.getStock(productId, warehouseId);
        return stock != null ? stock.getStockQuantity() : 0;
    }

    // 添加获取产品仓库的辅助方法
    private List<Stock> getProductWarehouses(Integer productId) {
        return productManagerMapper.getStocksByProductId(productId);
    }

    @Override
    public List<Map<String, Object>> getInventoryRecordsWithDetails(String startTime,
                                                                  String endTime,
                                                                  Integer productId,
                                                                  Integer warehouseId,
                                                                  Byte operationType) {
        return productManagerMapper.getInventoryRecordsWithDetails(startTime, endTime, 
                                                                 productId, warehouseId, 
                                                                 operationType);
    }
}
