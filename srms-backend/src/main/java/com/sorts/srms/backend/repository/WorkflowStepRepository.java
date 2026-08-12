package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, String> {
    List<WorkflowStep> findByWorkflowIdOrderByStepOrderAsc(String workflowId);
}
