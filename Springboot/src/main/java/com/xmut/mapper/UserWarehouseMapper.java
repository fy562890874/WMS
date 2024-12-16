package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.UserWarehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserWarehouseMapper extends BaseMapper<UserWarehouse> {

    // 根据用户ID查询仓库ID列表
    @Select("SELECT warehouse_id FROM xmut_user_warehouse WHERE user_id = #{userId}")
    List<Integer> getWarehouseIdsByUserId(@Param("userId") Integer userId);

    // 为用户分配仓库权限
    @Insert("INSERT INTO xmut_user_warehouse(user_id, warehouse_id, create_time) " +
            "VALUES(#{userId}, #{warehouseId}, NOW())")
    int addUserWarehouse(@Param("userId") Integer userId, @Param("warehouseId") Integer warehouseId);

    // 删除用户的仓库权限
    @Delete("DELETE FROM xmut_user_warehouse WHERE user_id = #{userId} AND warehouse_id = #{warehouseId}")
    int deleteUserWarehouse(@Param("userId") Integer userId, @Param("warehouseId") Integer warehouseId);

    // 删除用户的所有仓库权限
    @Delete("DELETE FROM xmut_user_warehouse WHERE user_id = #{userId}")
    int deleteWarehousesByUserId(@Param("userId") Integer userId);
}