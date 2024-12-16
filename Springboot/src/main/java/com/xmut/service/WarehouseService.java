package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Warehouse;

import java.util.List;

public interface WarehouseService extends IService<Warehouse> {

    List<Warehouse> getAllWarehouses();

    Warehouse getWarehouseById(Integer warehouseId);

    boolean addWarehouse(Warehouse warehouse);

    boolean updateWarehouse(Warehouse warehouse);

    boolean deleteWarehouse(Integer warehouseId);
}