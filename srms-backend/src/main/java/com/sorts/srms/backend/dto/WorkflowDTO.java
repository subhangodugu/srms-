package com.sorts.srms.backend.dto;

import java.util.List;

public class WorkflowDTO {

    private String id;
    private String companyId;
    private String code;
    private String name;
    private String description;
    private String entityType;
    private String status;
    private List<WorkflowStepDTO> steps;

    public WorkflowDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<WorkflowStepDTO> getSteps() { return steps; }
    public void setSteps(List<WorkflowStepDTO> steps) { this.steps = steps; }

    public static class WorkflowStepDTO {
        private String id;
        private Integer stepOrder;
        private String stepName;
        private String approverRoleId;
        private String approverRoleName;
        private String status;

        public WorkflowStepDTO() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public Integer getStepOrder() { return stepOrder; }
        public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
        public String getStepName() { return stepName; }
        public void setStepName(String stepName) { this.stepName = stepName; }
        public String getApproverRoleId() { return approverRoleId; }
        public void setApproverRoleId(String approverRoleId) { this.approverRoleId = approverRoleId; }
        public String getApproverRoleName() { return approverRoleName; }
        public void setApproverRoleName(String approverRoleName) { this.approverRoleName = approverRoleName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
