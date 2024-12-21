package com.xmut.controller;

import com.xmut.entity.InventoryRecord;
import com.xmut.entity.Product;
import com.xmut.service.ProductManagerService;
import com.xmut.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Api(tags = "货品及库存管理")
@RestController
@RequestMapping("/productManager")
public class ProductManagerController {

    @Autowired
    private ProductManagerService productManagerService;

    // 原ProductController的方法

    @ApiOperation("获取所有货品")
    @GetMapping("/products")
    public Map<String, Object> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer warehouseId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> products = productManagerService.getProductsByCondition(keyword, categoryId, warehouseId);
            response.put("success", true);
            response.put("data", products);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取货品列表失败");
        }
        return response;
    }

    @ApiOperation("添加货品")
    @PostMapping("/products")
    public Map<String, Object> addProduct(@RequestBody Map<String, Object> params) {
        Product product = new Product();
        product.setProductName((String) params.get("productName"));
        product.setPrice(new BigDecimal(params.get("price").toString()));
        product.setImageUrl((String) params.get("imageUrl"));
        product.setCategoryId((Integer) params.get("categoryId"));

        Integer initialWarehouseId = (Integer) params.get("initialWarehouseId");
        Integer initialQuantity = (Integer) params.get("initialQuantity");

        boolean result = productManagerService.addProduct(product, initialWarehouseId, initialQuantity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("message", result ? "添加成功" : "添加失败");
        return response;
    }

    @ApiOperation("更新货品信息")
    @PutMapping("/products")
    public Map<String, Object> updateProduct(@RequestBody Map<String, Object> params) {
        try {
            Product product = new Product();
            product.setProductId((Integer) params.get("productId"));
            product.setProductName((String) params.get("productName"));
            product.setPrice(new BigDecimal(params.get("price").toString()));
            product.setImageUrl((String) params.get("imageUrl"));
            product.setCategoryId((Integer) params.get("categoryId"));
            
            boolean result = productManagerService.updateProduct(product);
            
            // 如果有初始库存信息，同时更新库存
            Integer warehouseId = (Integer) params.get("warehouseId");
            Integer quantity = (Integer) params.get("quantity");
            if (warehouseId != null && quantity != null) {
                productManagerService.adjustStock(product.getProductId(), warehouseId, quantity);
            }
            
            return Map.of("success", result, "message", result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "更新失败：" + e.getMessage());
        }
    }

    @ApiOperation("删除货品")
    @DeleteMapping("/products/{productId}")
    public Map<String, Object> deleteProduct(@PathVariable String productId) {
        try {
            // 添加参数验证
            if (productId == null || productId.equals("undefined")) {
                return Map.of("success", false, "message", "无效的产品ID");
            }
            
            Integer pid = Integer.valueOf(productId);
            
            // 检查产品是否存在
            Product product = productManagerService.getProductById(pid);
            if (product == null) {
                return Map.of("success", false, "message", "产品不存在");
            }

            // 检查是否有未完成的出入库记录
            List<InventoryRecord> records = productManagerService.getInventoryRecordsByConditions(
                null, null, pid, null, null);
            if (!records.isEmpty()) {
                return Map.of("success", false, "message", "该货品存在出入库记录，无法删除");
            }
            
            boolean result = productManagerService.deleteProduct(pid);
            return Map.of("success", result, "message", result ? "删除成功" : "删除失败");
        } catch (NumberFormatException e) {
            return Map.of("success", false, "message", "无效的产品ID格式");
        } catch (Exception e) {
            return Map.of("success", false, "message", "删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("调整库存")
    @PostMapping("/products/adjustStock")
    public Map<String, Object> adjustStock(@RequestBody Map<String, Object> params) {
        try {
            // 添加请求参数验证
            Object productIdObj = params.get("productId");
            if (productIdObj == null) {
                return Map.of("success", false, "message", "产品ID不能为空");
            }
            
            Integer productId = null;
            try {
                productId = Integer.valueOf(productIdObj.toString());
            } catch (NumberFormatException e) {
                return Map.of("success", false, "message", "无效的产品ID格式");
            }

            // 检查产品是否存在
            Product product = productManagerService.getProductById(productId);
            if (product == null) {
                return Map.of("success", false, "message", "产品不存在");
            }

            Integer warehouseId = Integer.valueOf(params.get("warehouseId").toString());
            Integer quantity = Integer.valueOf(params.get("quantity").toString());
            String remark = (String) params.get("remark");
            String contactPerson = (String) params.get("contactPerson");

            if (quantity == null || quantity < 0) {
                return Map.of("success", false, "message", "无效的库存数量");
            }

            // 从 UserContext 获取操作人ID
            Integer operatorId = UserContext.getUserId();

            // 创建库存记录
            InventoryRecord record = new InventoryRecord();
            record.setProductId(productId);
            record.setWarehouseId(warehouseId);
            record.setQuantity(quantity);
            record.setOperatorId(operatorId);
            record.setRemark(remark);
            record.setContactPerson(contactPerson);

            boolean result = productManagerService.adjustStockWithRecord(productId, warehouseId, quantity, record);
            return Map.of(
                "success", result,
                "message", result ? "调整成功" : "调整失败"
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "message", "调整失败：" + e.getMessage()
            );
        }
    }

    @ApiOperation("上传货品图片")
    @PostMapping("/products/uploadImage")
    public String uploadImage(@RequestParam Integer productId, @RequestParam String imageUrl) {
        boolean result = productManagerService.uploadImage(productId, imageUrl);
        return result ? "图片上传成功" : "图片上传失败";
    }

    // 原InventoryController的方法

    @ApiOperation("入库操作")
    @PostMapping("/inventory/stockIn")
    public Map<String, Object> stockIn(@RequestBody InventoryRecord record) {
        Map<String, Object> response = new HashMap<>();
        // 这里不再从前端接收 operatorId，直接从 UserContext 获取
        record.setOperatorId(UserContext.getUserId());
        boolean result = productManagerService.stockIn(record);
        response.put("success", result);
        response.put("message", result ? "入库成功" : "入库失败");
        return response;
    }

    @ApiOperation("出库操作")
    @PostMapping("/inventory/stockOut")
    public Map<String, Object> stockOut(@RequestBody InventoryRecord record) {
        Map<String, Object> response = new HashMap<>();
        record.setOperatorId(UserContext.getUserId());
        boolean result = productManagerService.stockOut(record);
        response.put("success", result);
        response.put("message", result ? "出库成功" : "出库失败");
        return response;
    }

    @ApiOperation("调拨操作")
    @PostMapping("/inventory/transfer")
    public Map<String, Object> transferStock(@RequestBody Map<String, Object> params) {
        Integer productId = (Integer) params.get("productId");
        Integer fromWarehouseId = (Integer) params.get("fromWarehouseId");
        Integer toWarehouseId = (Integer) params.get("toWarehouseId");
        Integer quantity = (Integer) params.get("quantity");
        String contactPerson = (String) params.get("contactPerson");
        String remark = (String) params.get("remark");

        // 从UserContext获取操作人ID
        Integer operatorId = UserContext.getUserId();

        Map<String, Object> response = new HashMap<>();
        boolean result = productManagerService.transferStock(
                productId, fromWarehouseId, toWarehouseId, quantity, operatorId, contactPerson, remark);
        response.put("success", result);
        response.put("message", result ? "调拨成功" : "调拨失败");
        return response;
    }

    @ApiOperation("获取出入库记录")
    @GetMapping("/inventory/records")
    public Map<String, Object> getInventoryRecords(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer warehouseId,
            @RequestParam(required = false) Byte operationType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            // 获取记录总数和分页数据
            List<Map<String, Object>> records = productManagerService.getInventoryRecordsWithDetails(
                    startTime, endTime, null, warehouseId, operationType);

            // 计算分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, records.size());
            List<Map<String, Object>> pageRecords = records.subList(start, end);

            Map<String, Object> data = new HashMap<>();
            data.put("records", pageRecords);
            data.put("total", records.size());
            data.put("page", page);
            data.put("size", size);

            return Map.of("success", true, "data", data);
        } catch (Exception e) {
            return Map.of("success", false, "message", "获取记录失败：" + e.getMessage());
        }
    }

    @ApiOperation("检查库存")
    @GetMapping("/inventory/check")
    public Map<String, Object> checkStock(
            @RequestParam Integer productId,
            @RequestParam Integer warehouseId,
            @RequestParam Integer quantity) {
        boolean isEnough = productManagerService.checkStock(productId, warehouseId, quantity);
        return Map.of(
                "isEnough", isEnough,
                "currentStock", productManagerService.getStock(productId, warehouseId)
        );
    }

    @ApiOperation("获取库存详情")
    @GetMapping("/inventory/stock")
    public Integer getStock(
            @RequestParam Integer productId,
            @RequestParam Integer warehouseId) {
        return productManagerService.getStock(productId, warehouseId);
    }

    @ApiOperation("获取库存变动记录")
    @GetMapping("/inventory/changes")
    public Map<String, Object> getStockChanges(
            @RequestParam Integer productId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> changes = productManagerService.getStockChanges(
                productId, startTime, endTime);
        response.put("success", true);
        response.put("data", changes);
        return response;
    }
}
