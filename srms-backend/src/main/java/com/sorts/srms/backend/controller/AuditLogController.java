package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.AuditLogDTO;
import com.sorts.srms.backend.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('AUDIT_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogs(@RequestParam(required = false) String companyId) {
        if (companyId != null && !companyId.isBlank()) {
            return ResponseEntity.ok(auditLogService.getAuditLogsByCompany(companyId));
        }
        return ResponseEntity.ok(auditLogService.getRecentAuditLogs());
    }
}
