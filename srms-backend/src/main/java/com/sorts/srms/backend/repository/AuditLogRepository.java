package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    List<AuditLog> findByCompanyIdOrderByTimestampDesc(String companyId);
    List<AuditLog> findTop100ByOrderByTimestampDesc();
}
