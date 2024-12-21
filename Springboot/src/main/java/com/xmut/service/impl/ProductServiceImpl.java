package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Product;
import com.xmut.entity.Stock;
import com.xmut.entity.Warehouse;
import com.xmut.mapper.ProductMapper;
import com.xmut.mapper.StockMapper;
import com.xmut.mapper.CategoryMapper;
import com.xmut.mapper.WarehouseMapper;
import com.xmut.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public List<Product> getAllProducts() {
        return baseMapper.getAllProducts();
    }

    @Override
    public Product getProductById(Integer productId) {
        return baseMapper.getProductById(productId);
    }

    @Override
    public boolean addProduct(Product product) {
        // 验证类别是否存在
        if (categoryMapper.getCategoryById(product.getCategoryId()) == null) {
            throw new RuntimeException("类别不存在");
        }

        int result = baseMapper.addProduct(product);
        if (result > 0) {
            // 初始化所有仓库的库存记录为0
            List<Warehouse> warehouses = warehouseMapper.getAllWarehouses();
            for (Warehouse warehouse : warehouses) {
                Stock stock = new Stock();
                stock.setProductId(product.getProductId());
                stock.setWarehouseId(warehouse.getWarehouseId());
                stock.setStockQuantity(0);
                stockMapper.addStock(stock);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateProduct(Product product) {
        int result = baseMapper.updateProduct(product);
        return result > 0;
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        int result = baseMapper.deleteProduct(productId);
        return result > 0;
    }

    @Override
    public boolean adjustStock(Integer productId, Integer warehouseId, Integer quantity) {
        Stock stock = stockMapper.getStock(productId, warehouseId);
        if (stock == null) {
            stock = new Stock();
            stock.setProductId(productId);
            stock.setWarehouseId(warehouseId);
            stock.setStockQuantity(quantity);
            return stockMapper.addStock(stock) > 0;
        } else {
            stock.setStockQuantity(quantity);
            return stockMapper.updateStock(stock) > 0;
        }
    }

    @Override
    public boolean uploadImage(Integer productId, String imageUrl) {
        Product product = new Product();
        product.setProductId(productId);
        product.setImageUrl(imageUrl);
        return baseMapper.updateProduct(product) > 0;
    }
    
    // 添加以下方法实现
    @Override
    public List<Map<String, Object>> getProductsByCondition(String keyword, Integer categoryId, Integer warehouseId) {
        return baseMapper.getProductsByCondition(keyword, categoryId, warehouseId);
    }
}