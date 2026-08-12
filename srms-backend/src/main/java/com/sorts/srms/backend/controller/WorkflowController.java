package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.WorkflowDTO;
import com.sorts.srms.backend.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('WORKFLOW_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<WorkflowDTO>> getWorkflowsByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(workflowService.getWorkflowsByCompany(companyId));
    }

    @PostMapping("/{workflowId}/steps/{stepId}/approve")
    @PreAuthorize("hasAuthority('WORKFLOW_APPROVE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<WorkflowDTO> approveStep(
            @PathVariable String workflowId,
            @PathVariable String stepId) {
        return ResponseEntity.ok(workflowService.approveStep(workflowId, stepId));
    }
}
