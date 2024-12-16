package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Product;
import com.xmut.entity.Stock;
import com.xmut.mapper.ProductMapper;
import com.xmut.mapper.StockMapper;
import com.xmut.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private StockMapper stockMapper;

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
        int result = baseMapper.addProduct(product);
        return result > 0;
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
            int result = stockMapper.addStock(stock);
            return result > 0;
        } else {
            stock.setStockQuantity(quantity);
            int result = stockMapper.updateStock(stock);
            return result > 0;
        }
    }

    @Override
    public boolean uploadImage(Integer productId, String imageUrl) {
        Product product = new Product();
        product.setProductId(productId);
        product.setImageUrl(imageUrl);
        int result = baseMapper.updateProduct(product);
        return result > 0;
    }
}