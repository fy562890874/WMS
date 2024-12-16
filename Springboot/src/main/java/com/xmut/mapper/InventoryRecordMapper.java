package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.InventoryRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InventoryRecordMapper extends BaseMapper<InventoryRecord> {

    // 查询所有出入库记录
    @Select("SELECT * FROM xmut_inventory_record")
    List<InventoryRecord> getAllInventoryRecords();

    // 根据ID查询记录
    @Select("SELECT * FROM xmut_inventory_record WHERE record_id = #{recordId}")
    InventoryRecord getInventoryRecordById(@Param("recordId") Integer recordId);

    // 添加出入库记录
    @Insert("INSERT INTO xmut_inventory_record(product_id, warehouse_id, quantity, operation_type, " +
            "related_record_id, operator_id, remark, record_time, create_time) " +
            "VALUES(#{productId}, #{warehouseId}, #{quantity}, #{operationType}, #{relatedRecordId}, " +
            "#{operatorId}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int addInventoryRecord(InventoryRecord record);

    // 更新出入库记录
    @Update("UPDATE xmut_inventory_record SET quantity = #{quantity}, remark = #{remark}, update_time = NOW() " +
            "WHERE record_id = #{recordId}")
    int updateInventoryRecord(InventoryRecord record);

    // 删除出入库记录
    @Delete("DELETE FROM xmut_inventory_record WHERE record_id = #{recordId}")
    int deleteInventoryRecord(@Param("recordId") Integer recordId);

    // 按条件查询记录（可用于统计）
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
}