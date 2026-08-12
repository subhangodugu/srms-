package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.model.Workflow;
import com.sorts.srms.backend.domain.model.WorkflowStep;
import com.sorts.srms.backend.dto.WorkflowDTO;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.WorkflowRepository;
import com.sorts.srms.backend.repository.WorkflowStepRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowStepRepository stepRepository;

    public WorkflowService(WorkflowRepository workflowRepository, WorkflowStepRepository stepRepository) {
        this.workflowRepository = workflowRepository;
        this.stepRepository = stepRepository;
    }

    @Transactional(readOnly = true)
    public List<WorkflowDTO> getWorkflowsByCompany(String companyId) {
        return workflowRepository.findByCompanyId(companyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkflowDTO approveStep(String workflowId, String stepId) {
        WorkflowStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow step not found: " + stepId));

        step.setStatus("APPROVED");
        stepRepository.save(step);

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found: " + workflowId));

        return mapToDTO(workflow);
    }

    private WorkflowDTO mapToDTO(Workflow wf) {
        WorkflowDTO dto = new WorkflowDTO();
        dto.setId(wf.getId());
        dto.setCompanyId(wf.getCompany().getId());
        dto.setCode(wf.getCode());
        dto.setName(wf.getName());
        dto.setDescription(wf.getDescription());
        dto.setEntityType(wf.getEntityType());
        dto.setStatus(wf.getStatus().name());

        List<WorkflowStep> steps = stepRepository.findByWorkflowIdOrderByStepOrderAsc(wf.getId());
        dto.setSteps(steps.stream().map(s -> {
            WorkflowDTO.WorkflowStepDTO sDto = new WorkflowDTO.WorkflowStepDTO();
            sDto.setId(s.getId());
            sDto.setStepOrder(s.getStepOrder());
            sDto.setStepName(s.getStepName());
            if (s.getApproverRole() != null) {
                sDto.setApproverRoleId(s.getApproverRole().getId());
                sDto.setApproverRoleName(s.getApproverRole().getName());
            }
            sDto.setStatus(s.getStatus());
            return sDto;
        }).collect(Collectors.toList()));

        return dto;
    }
}
