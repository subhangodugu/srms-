package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.ReportRequestDTO;
import com.sorts.srms.backend.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('REPORTS_GENERATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<byte[]> generateReport(@Valid @RequestBody ReportRequestDTO request) {
        byte[] content = reportService.generateReport(request);

        String filename = "srms_report_" + System.currentTimeMillis() + ("CSV".equalsIgnoreCase(request.getFormat()) ? ".csv" : ".txt");
        MediaType contentType = "CSV".equalsIgnoreCase(request.getFormat()) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(contentType)
                .body(content);
    }
}
