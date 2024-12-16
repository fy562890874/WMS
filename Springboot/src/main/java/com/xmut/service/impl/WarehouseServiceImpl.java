package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Warehouse;
import com.xmut.mapper.WarehouseMapper;
import com.xmut.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Override
    public List<Warehouse> getAllWarehouses() {
        return baseMapper.getAllWarehouses();
    }

    @Override
    public Warehouse getWarehouseById(Integer warehouseId) {
        return baseMapper.getWarehouseById(warehouseId);
    }

    @Override
    public boolean addWarehouse(Warehouse warehouse) {
        int result = baseMapper.addWarehouse(warehouse);
        return result > 0;
    }

    @Override
    public boolean updateWarehouse(Warehouse warehouse) {
        int result = baseMapper.updateWarehouse(warehouse);
        return result > 0;
    }

    @Override
    public boolean deleteWarehouse(Integer warehouseId) {
        int result = baseMapper.deleteWarehouse(warehouseId);
        return result > 0;
    }
}