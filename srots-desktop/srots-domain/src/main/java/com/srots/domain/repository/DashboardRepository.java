package com.srots.domain.repository;

import com.srots.domain.dashboard.DashboardMetrics;

public interface DashboardRepository {
    DashboardMetrics getMetrics();
}
