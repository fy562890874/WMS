package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService extends IService<Product> {

    List<Product> getAllProducts();

    Product getProductById(Integer productId);

    boolean addProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(Integer productId);

    boolean adjustStock(Integer productId, Integer warehouseId, Integer quantity);

    boolean uploadImage(Integer productId, String imageUrl);
    
    // 添加以下方法
    List<Map<String, Object>> getProductsByCondition(String keyword, Integer categoryId, Integer warehouseId);
}