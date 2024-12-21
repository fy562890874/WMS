package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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

    // 添加联表查询，获取完整的产品信息(包含类别名称)
    @Select("SELECT p.*, c.category_name FROM xmut_product p " +
            "LEFT JOIN xmut_category c ON p.category_id = c.category_id")
    List<Map<String, Object>> getAllProductsWithCategory();

    // 添加条件查询
    @Select("<script>" +
            "SELECT p.product_id, " +
            "p.product_name as productName, " +  // 添加别名
            "p.price, " +
            "p.image_url as imageUrl, " +
            "c.category_name as categoryName, " +  // 添加别名
            "(SELECT GROUP_CONCAT(CONCAT(w.warehouse_name, ':', s.stock_quantity)) " +
            "FROM xmut_stock s " +
            "LEFT JOIN xmut_warehouse w ON s.warehouse_id = w.warehouse_id " +
            "WHERE s.product_id = p.product_id) as stock_info " +
            "FROM xmut_product p " +
            "LEFT JOIN xmut_category c ON p.category_id = c.category_id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'> AND p.product_name LIKE CONCAT('%',#{keyword},'%') </if>" +
            "<if test='categoryId != null'> AND p.category_id = #{categoryId} </if>" +
            "<if test='warehouseId != null'> AND EXISTS (SELECT 1 FROM xmut_stock s WHERE s.product_id = p.product_id AND s.warehouse_id = #{warehouseId}) </if>" +
            "</script>")
    List<Map<String, Object>> getProductsByCondition(@Param("keyword") String keyword,
                                                    @Param("categoryId") Integer categoryId,
                                                    @Param("warehouseId") Integer warehouseId);
}