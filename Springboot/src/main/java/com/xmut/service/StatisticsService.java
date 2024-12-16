package com.xmut.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getCompanyOverview(String startTime, String endTime);

    List<Map<String, Object>> getCompanyTopProducts(String startTime, String endTime);

    Map<String, Object> getWarehouseOverview(Integer warehouseId, String startTime, String endTime);

    List<Map<String, Object>> getWarehouseTopProducts(Integer warehouseId, String startTime, String endTime);
}
