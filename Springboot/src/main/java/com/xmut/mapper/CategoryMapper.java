package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    // 查询所有类别
    @Select("SELECT * FROM xmut_category")
    List<Category> getAllCategories();

    // 根据ID查询类别
    @Select("SELECT * FROM xmut_category WHERE category_id = #{categoryId}")
    Category getCategoryById(@Param("categoryId") Integer categoryId);

    // 添加类别
    @Insert("INSERT INTO xmut_category(category_name, description, create_time) " +
            "VALUES(#{categoryName}, #{description}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "categoryId")
    int addCategory(Category category);

    // 更新类别信息
    @Update("UPDATE xmut_category SET category_name = #{categoryName}, description = #{description}, update_time = NOW() " +
            "WHERE category_id = #{categoryId}")
    int updateCategory(Category category);

    // 删除类别
    @Delete("DELETE FROM xmut_category WHERE category_id = #{categoryId}")
    int deleteCategory(@Param("categoryId") Integer categoryId);
}