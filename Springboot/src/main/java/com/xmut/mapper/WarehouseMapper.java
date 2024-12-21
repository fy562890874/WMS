package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    // 查询所有仓库
    @Select("SELECT * FROM xmut_warehouse ORDER BY create_time DESC")
    List<Warehouse> getAllWarehouses();

    // 根据ID查询仓库
    @Select("SELECT * FROM xmut_warehouse WHERE warehouse_id = #{warehouseId}")
    Warehouse getWarehouseById(@Param("warehouseId") Integer warehouseId);

    // 添加仓库
    @Insert("INSERT INTO xmut_warehouse(warehouse_name, address, create_time) " +
            "VALUES(#{warehouseName}, #{address}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "warehouseId")
    int addWarehouse(Warehouse warehouse);

    // 更新仓库信息
    @Update("UPDATE xmut_warehouse SET " +
            "warehouse_name = #{warehouseName}, " +
            "address = #{address}, " +
            "update_time = #{updateTime} " +
            "WHERE warehouse_id = #{warehouseId}")
    int updateWarehouse(Warehouse warehouse);

    // 删除仓库
    @Delete("DELETE FROM xmut_warehouse WHERE warehouse_id = #{warehouseId}")
    int deleteWarehouse(@Param("warehouseId") Integer warehouseId);
}