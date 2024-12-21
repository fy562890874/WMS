package com.xmut.controller;

import com.xmut.entity.Product;
import com.xmut.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "货品管理")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("Get all products")
    @GetMapping
    public Map<String, Object> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer warehouseId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> products = productService.getProductsByCondition(keyword, categoryId, warehouseId);
            response.put("success", true);
            response.put("data", products);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取货品列表失败");
        }
        return response;
    }

    @ApiOperation("Add product")
    @PostMapping
    public String addProduct(@RequestBody Product product) {
        boolean result = productService.addProduct(product);
        return result ? "Add successful" : "Add failed";
    }

    @ApiOperation("Update product information")
    @PutMapping
    public String updateProduct(@RequestBody Product product) {
        boolean result = productService.updateProduct(product);
        return result ? "Update successful" : "Update failed";
    }

    @ApiOperation("Delete product")
    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Integer productId) {
        boolean result = productService.deleteProduct(productId);
        return result ? "Delete successful" : "Delete failed";
    }

    @ApiOperation("Adjust stock")
    @PostMapping("/adjustStock")
    public Map<String, Object> adjustStock(@RequestBody Map<String, Object> params) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = productService.adjustStock(
                (Integer) params.get("productId"),
                (Integer) params.get("warehouseId"),
                (Integer) params.get("quantity")
            );
            response.put("success", result);
            response.put("message", result ? "库存调整成功" : "库存调整失败");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "库存调整失败");
        }
        return response;
    }

    @ApiOperation("Upload product image")
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam Integer productId, @RequestParam String imageUrl) {
        boolean result = productService.uploadImage(productId, imageUrl);
        return result ? "Image upload successful" : "Image upload failed";
    }
}