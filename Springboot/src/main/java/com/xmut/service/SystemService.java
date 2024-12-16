package com.xmut.service;

import java.util.List;
import java.util.Map;

public interface SystemService {
    Map<String, Object> getDashboardOverview();

    List<Map<String, Object>> getLatestMessages();

    Map<String, Object> getSystemInfo();
}
