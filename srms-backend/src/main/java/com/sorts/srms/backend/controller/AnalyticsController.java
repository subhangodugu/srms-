package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.AnalyticsMetricDTO;
import com.sorts.srms.backend.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AnalyticsMetricDTO> getMetricsForCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(analyticsService.getMetricsForCompany(companyId));
    }
}
