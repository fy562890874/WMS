package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> getAllCategories();

    Category getCategoryById(Integer categoryId);

    boolean addCategory(Category category);

    boolean updateCategory(Category category);

    boolean deleteCategory(Integer categoryId);
}