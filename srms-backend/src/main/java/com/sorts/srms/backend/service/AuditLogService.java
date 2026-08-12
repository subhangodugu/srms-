package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.model.AuditLog;
import com.sorts.srms.backend.dto.AuditLogDTO;
import com.sorts.srms.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(readOnly = true)
    public List<AuditLogDTO> getAuditLogsByCompany(String companyId) {
        return auditLogRepository.findByCompanyIdOrderByTimestampDesc(companyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogDTO> getRecentAuditLogs() {
        return auditLogRepository.findTop100ByOrderByTimestampDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AuditLogDTO mapToDTO(AuditLog log) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(log.getId());
        dto.setUserId(log.getUserId());
        dto.setUsername(log.getUsername());
        dto.setCompanyId(log.getCompanyId());
        dto.setAction(log.getAction());
        dto.setResource(log.getResource());
        dto.setResourceId(log.getResourceId());
        dto.setDetails(log.getDetails());
        dto.setIpAddress(log.getIpAddress());
        dto.setStatus(log.getStatus());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}
