package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
}