package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Warehouse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WarehouseService extends IService<Warehouse> {

    List<Warehouse> getAllWarehouses();

    Warehouse getWarehouseById(Integer warehouseId);

    @Transactional
    boolean addWarehouse(Warehouse warehouse);

    @Transactional
    boolean updateWarehouse(Warehouse warehouse);

    @Transactional
    boolean deleteWarehouse(Integer warehouseId);
}