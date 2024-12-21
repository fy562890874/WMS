package com.xmut.controller;

import com.xmut.entity.Category;
import com.xmut.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "货品类别管理")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取所有类别")
    @GetMapping
    public Map<String, Object> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        Map<String, Object> response = new HashMap<>();
        response.put("data", categories);
        return response;
    }

    @ApiOperation("添加类别")
    @PostMapping
    public Map<String, Object> addCategory(@RequestBody Category category) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = categoryService.addCategory(category);
            response.put("success", result);
            response.put("message", result ? "添加成功" : "添加失败");
        } catch (Exception e) {
            if (e.getMessage().contains("unique_category_name")) {
                response.put("success", false);
                response.put("message", "类别名称已存在");
            } else {
                response.put("success", false);
                response.put("message", "添加失败");
            }
        }
        return response;
    }

    @ApiOperation("更新类别")
    @PutMapping
    public Map<String, Object> updateCategory(@RequestBody Category category) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = categoryService.updateCategory(category);
            response.put("success", result);
            response.put("message", result ? "更新成功" : "更新失败");
        } catch (Exception e) {
            if (e.getMessage().contains("unique_category_name")) {
                response.put("success", false);
                response.put("message", "类别名称已存在");
            } else {
                response.put("success", false);
                response.put("message", "更新失败");
            }
        }
        return response;
    }

    @ApiOperation("删除类别")
    @DeleteMapping("/{categoryId}")
    public Map<String, Object> deleteCategory(@PathVariable Integer categoryId) {
        Map<String, Object> response = new HashMap<>();
        boolean result = categoryService.deleteCategory(categoryId);
        response.put("success", result);
        response.put("message", result ? "删除成功" : "删除失败");
        return response;
    }
}