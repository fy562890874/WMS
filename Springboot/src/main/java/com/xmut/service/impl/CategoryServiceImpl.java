package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Category;
import com.xmut.mapper.CategoryMapper;
import com.xmut.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getAllCategories() {
        return baseMapper.getAllCategories();
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        return baseMapper.getCategoryById(categoryId);
    }

    @Override
    public boolean addCategory(Category category) {
        int result = baseMapper.addCategory(category);
        return result > 0;
    }

    @Override
    public boolean updateCategory(Category category) {
        int result = baseMapper.updateCategory(category);
        return result > 0;
    }

    @Override
    public boolean deleteCategory(Integer categoryId) {
        int result = baseMapper.deleteCategory(categoryId);
        return result > 0;
    }
}