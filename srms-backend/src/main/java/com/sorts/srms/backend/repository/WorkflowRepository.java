package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, String> {
    List<Workflow> findByCompanyId(String companyId);
    Optional<Workflow> findByCode(String code);
}
