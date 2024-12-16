package com.xmut.controller;

import com.xmut.entity.Category;
import com.xmut.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "货品类别管理")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("Get all categories")
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ApiOperation("Add category")
    @PostMapping
    public String addCategory(@RequestBody Category category) {
        boolean result = categoryService.addCategory(category);
        return result ? "Add successful" : "Add failed";
    }

    @ApiOperation("Update category information")
    @PutMapping
    public String updateCategory(@RequestBody Category category) {
        boolean result = categoryService.updateCategory(category);
        return result ? "Update successful" : "Update failed";
    }

    @ApiOperation("Delete category")
    @DeleteMapping("/{categoryId}")
    public String deleteCategory(@PathVariable Integer categoryId) {
        boolean result = categoryService.deleteCategory(categoryId);
        return result ? "Delete successful" : "Delete failed";
    }
}