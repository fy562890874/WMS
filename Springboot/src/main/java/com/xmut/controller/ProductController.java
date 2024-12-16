package com.xmut.controller;

import com.xmut.entity.Product;
import com.xmut.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "货品管理")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("Get all products")
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
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
    public String adjustStock(@RequestParam Integer productId, @RequestParam Integer warehouseId, @RequestParam Integer quantity) {
        boolean result = productService.adjustStock(productId, warehouseId, quantity);
        return result ? "Stock adjustment successful" : "Stock adjustment failed";
    }

    @ApiOperation("Upload product image")
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam Integer productId, @RequestParam String imageUrl) {
        boolean result = productService.uploadImage(productId, imageUrl);
        return result ? "Image upload successful" : "Image upload failed";
    }
}