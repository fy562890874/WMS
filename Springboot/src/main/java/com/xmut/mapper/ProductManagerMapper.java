package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.InventoryRecord;
import com.xmut.entity.Product;
import com.xmut.entity.Stock;
import org.apache.ibatis.annotations.*;
import com.xmut.entity.Warehouse;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductManagerMapper extends BaseMapper<Product> {

    // ProductMapper的方法

    @Select("SELECT * FROM xmut_product")
    List<Product> getAllProducts();

    @Select("SELECT * FROM xmut_product WHERE product_id = #{productId}")
    Product getProductById(@Param("productId") Integer productId);

    @Insert("INSERT INTO xmut_product(product_name, price, image_url, category_id, create_time) " +
            "VALUES(#{productName}, #{price}, #{imageUrl}, #{categoryId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    int addProduct(Product product);

    @Update("UPDATE xmut_product SET product_name = #{productName}, price = #{price}, " +
            "image_url = #{imageUrl}, category_id = #{categoryId}, " +
            "update_time = NOW() WHERE product_id = #{productId}")
    int updateProduct(Product product);

    @Delete("DELETE FROM xmut_product WHERE product_id = #{productId}")
    int deleteProduct(@Param("productId") Integer productId);

    @Select("<script>" +
            "SELECT DISTINCT p.*, c.category_name, " +
            "(SELECT CONCAT_WS(',', " +
            "GROUP_CONCAT(CONCAT(w2.warehouse_name, ':', COALESCE(s2.stock_quantity, 0)))) " +
            "FROM xmut_product p2 " +
            "LEFT JOIN xmut_stock s2 ON p2.product_id = s2.product_id " +
            "LEFT JOIN xmut_warehouse w2 ON s2.warehouse_id = w2.warehouse_id " +
            "WHERE p2.product_id = p.product_id) as stock_info " +
            "FROM xmut_product p " +
            "LEFT JOIN xmut_category c ON p.category_id = c.category_id " +
            "LEFT JOIN xmut_stock s ON p.product_id = s.product_id " +
            "LEFT JOIN xmut_warehouse w ON s.warehouse_id = w.warehouse_id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'> AND p.product_name LIKE CONCAT('%',#{keyword},'%') </if>" +
            "<if test='categoryId != null'> AND p.category_id = #{categoryId} </if>" +
            "<if test='warehouseId != null'> AND s.warehouse_id = #{warehouseId} </if>" +
            "</script>")
    List<Map<String, Object>> getProductsByCondition(@Param("keyword") String keyword,
                                                     @Param("categoryId") Integer categoryId,
                                                     @Param("warehouseId") Integer warehouseId);

    // 新增获取产品所有仓库库存的方法
    @Select("SELECT s.*, w.warehouse_name FROM xmut_stock s " +
            "LEFT JOIN xmut_warehouse w ON s.warehouse_id = w.warehouse_id " +
            "WHERE s.product_id = #{productId}")
    List<Map<String, Object>> getProductStockInfo(@Param("productId") Integer productId);

    // StockMapper的方法

    @Select("SELECT * FROM xmut_stock WHERE warehouse_id = #{warehouseId}")
    List<Stock> getStocksByWarehouseId(@Param("warehouseId") Integer warehouseId);

    @Select("SELECT * FROM xmut_stock WHERE product_id = #{productId}")
    List<Stock> getStocksByProductId(@Param("productId") Integer productId);

    @Select("SELECT * FROM xmut_stock WHERE product_id = #{productId} AND warehouse_id = #{warehouseId}")
    Stock getStock(@Param("productId") Integer productId, @Param("warehouseId") Integer warehouseId);

    @Insert("INSERT INTO xmut_stock(product_id, warehouse_id, stock_quantity, create_time) " +
            "VALUES(#{productId}, #{warehouseId}, #{stockQuantity}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addStock(Stock stock);

    @Update("UPDATE xmut_stock SET stock_quantity = #{stockQuantity}, update_time = NOW() " +
            "WHERE id = #{id}")
    int updateStock(Stock stock);

    @Delete("DELETE FROM xmut_stock WHERE id = #{id}")
    int deleteStock(@Param("id") Integer id);

    @Delete("DELETE FROM xmut_stock WHERE product_id = #{productId} AND warehouse_id = #{warehouseId}")
    int deleteStockByProductIdAndWarehouseId(@Param("productId") Integer productId, 
                                           @Param("warehouseId") Integer warehouseId);

    @Select("SELECT s.*, w.warehouse_name, p.product_name " +
            "FROM xmut_stock s " +
            "LEFT JOIN xmut_warehouse w ON s.warehouse_id = w.warehouse_id " +
            "LEFT JOIN xmut_product p ON s.product_id = p.product_id " +
            "WHERE s.product_id = #{productId}")
    List<Map<String, Object>> getStockDetailsByProductId(@Param("productId") Integer productId);

    @Select("SELECT CASE WHEN stock_quantity >= #{quantity} THEN 1 ELSE 0 END " +
            "FROM xmut_stock WHERE product_id = #{productId} AND warehouse_id = #{warehouseId}")
    boolean checkStockAvailable(@Param("productId") Integer productId,
                                @Param("warehouseId") Integer warehouseId,
                                @Param("quantity") Integer quantity);

    // InventoryRecordMapper的方法

    @Select("SELECT * FROM xmut_inventory_record")
    List<InventoryRecord> getAllInventoryRecords();

    @Select("SELECT * FROM xmut_inventory_record WHERE record_id = #{recordId}")
    InventoryRecord getInventoryRecordById(@Param("recordId") Integer recordId);

    @Insert("INSERT INTO xmut_inventory_record(product_id, warehouse_id, quantity, operation_type, " +
            "related_record_id, operator_id, contact_person, remark, record_time, create_time) " +
            "VALUES(#{productId}, #{warehouseId}, #{quantity}, #{operationType}, #{relatedRecordId}, " +
            "#{operatorId}, #{contactPerson}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int addInventoryRecord(InventoryRecord record);

    @Update("UPDATE xmut_inventory_record SET quantity = #{quantity}, remark = #{remark}, related_record_id = #{relatedRecordId}, update_time = NOW() " +
            "WHERE record_id = #{recordId}")
    int updateInventoryRecord(InventoryRecord record);

    @Delete("DELETE FROM xmut_inventory_record WHERE record_id = #{recordId}")
    int deleteInventoryRecord(@Param("recordId") Integer recordId);

    @Select("<script>" +
            "SELECT * FROM xmut_inventory_record " +
            "WHERE 1=1 " +
            "<if test='startTime != null'> AND record_time <![CDATA[>=]]> #{startTime} </if>" +
            "<if test='endTime != null'> AND record_time <![CDATA[<=]]> #{endTime} </if>" +
            "<if test='productId != null'> AND product_id = #{productId} </if>" +
            "<if test='warehouseId != null'> AND warehouse_id = #{warehouseId} </if>" +
            "<if test='operationType != null'> AND operation_type = #{operationType} </if>" +
            "</script>")
    List<InventoryRecord> getInventoryRecordsByConditions(@Param("startTime") String startTime,
                                                          @Param("endTime") String endTime,
                                                          @Param("productId") Integer productId,
                                                          @Param("warehouseId") Integer warehouseId,
                                                          @Param("operationType") Byte operationType);

    @Select("<script>" +
            "SELECT r.*, " +
            "p.product_name, " +
            "w.warehouse_name, " +
            "u.username as operatorName " + // 确保字段名为 operatorName
            "FROM xmut_inventory_record r " +
            "LEFT JOIN xmut_product p ON r.product_id = p.product_id " +
            "LEFT JOIN xmut_warehouse w ON r.warehouse_id = w.warehouse_id " +
            "LEFT JOIN xmut_user u ON r.operator_id = u.user_id " +
            "WHERE 1=1 " +
            "<if test='startTime != null'> AND DATE(r.record_time) &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND DATE(r.record_time) &lt;= #{endTime} </if>" +
            "<if test='productId != null'> AND r.product_id = #{productId} </if>" +
            "<if test='warehouseId != null'> AND r.warehouse_id = #{warehouseId} </if>" +
            "<if test='operationType != null'> AND r.operation_type = #{operationType} </if>" +
            "ORDER BY r.record_time DESC" +
            "</script>")
    List<Map<String, Object>> getInventoryRecordsWithDetails(@Param("startTime") String startTime,
                                                            @Param("endTime") String endTime,
                                                            @Param("productId") Integer productId,
                                                            @Param("warehouseId") Integer warehouseId,
                                                            @Param("operationType") Byte operationType);

    // 引入CategoryMapper和WarehouseMapper的方法用于辅助

    @Select("SELECT * FROM xmut_category WHERE category_id = #{categoryId}")
    Map<String, Object> getCategoryById(@Param("categoryId") Integer categoryId);

    @Select("SELECT * FROM xmut_warehouse")
    List<Warehouse> getAllWarehouses();

    @Select("SELECT * FROM xmut_warehouse WHERE warehouse_id = #{warehouseId}")
    Warehouse getWarehouseById(@Param("warehouseId") Integer warehouseId);

    @Insert("INSERT INTO xmut_stock(product_id, warehouse_id, stock_quantity, create_time) " +
            "VALUES(#{productId}, #{warehouseId}, #{quantity}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addInitialStock(@Param("productId") Integer productId,
                        @Param("warehouseId") Integer warehouseId,
                        @Param("quantity") Integer quantity);
}
