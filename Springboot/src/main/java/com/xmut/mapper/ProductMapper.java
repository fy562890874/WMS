package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    // 查询所有货品
    @Select("SELECT * FROM xmut_product")
    List<Product> getAllProducts();

    // 根据ID查询货品
    @Select("SELECT * FROM xmut_product WHERE product_id = #{productId}")
    Product getProductById(@Param("productId") Integer productId);

    // 添加货品
    @Insert("INSERT INTO xmut_product(product_name, price, image_url, category_id, create_time) " +
            "VALUES(#{productName}, #{price}, #{imageUrl}, #{categoryId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    int addProduct(Product product);

    // 更新货品信息
    @Update("UPDATE xmut_product SET product_name = #{productName}, price = #{price}, " +
            "image_url = #{imageUrl}, category_id = #{categoryId}, update_time = NOW() " +
            "WHERE product_id = #{productId}")
    int updateProduct(Product product);

    // 删除货品
    @Delete("DELETE FROM xmut_product WHERE product_id = #{productId}")
    int deleteProduct(@Param("productId") Integer productId);
}