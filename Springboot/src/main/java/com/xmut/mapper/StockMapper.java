package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {

    // 查询指定仓库的所有库存
    @Select("SELECT * FROM xmut_stock WHERE warehouse_id = #{warehouseId}")
    List<Stock> getStocksByWarehouseId(@Param("warehouseId") Integer warehouseId);

    // 查询指定货品在所有仓库的库存
    @Select("SELECT * FROM xmut_stock WHERE product_id = #{productId}")
    List<Stock> getStocksByProductId(@Param("productId") Integer productId);

    // 查询指定货品在指定仓库的库存
    @Select("SELECT * FROM xmut_stock WHERE product_id = #{productId} AND warehouse_id = #{warehouseId}")
    Stock getStock(@Param("productId") Integer productId, @Param("warehouseId") Integer warehouseId);

    // 添加库存记录
    @Insert("INSERT INTO xmut_stock(product_id, warehouse_id, stock_quantity, create_time) " +
            "VALUES(#{productId}, #{warehouseId}, #{stockQuantity}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addStock(Stock stock);

    // 更新库存数量
    @Update("UPDATE xmut_stock SET stock_quantity = #{stockQuantity}, update_time = NOW() " +
            "WHERE id = #{id}")
    int updateStock(Stock stock);

    // 删除库存记录
    @Delete("DELETE FROM xmut_stock WHERE id = #{id}")
    int deleteStock(@Param("id") Integer id);

    // 获取带仓库名称的库存信息
    @Select("SELECT s.*, w.warehouse_name, p.product_name " +
            "FROM xmut_stock s " +
            "LEFT JOIN xmut_warehouse w ON s.warehouse_id = w.warehouse_id " +
            "LEFT JOIN xmut_product p ON s.product_id = p.product_id " +
            "WHERE s.product_id = #{productId}")
    List<Map<String, Object>> getStockDetailsByProductId(@Param("productId") Integer productId);

    // 检查库存是否充足
    @Select("SELECT CASE WHEN stock_quantity >= #{quantity} THEN 1 ELSE 0 END " +
            "FROM xmut_stock WHERE product_id = #{productId} AND warehouse_id = #{warehouseId}")
    boolean checkStockAvailable(@Param("productId") Integer productId, 
                              @Param("warehouseId") Integer warehouseId,
                              @Param("quantity") Integer quantity);
}