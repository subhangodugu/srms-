package com.sorts.srms.backend.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "workflow_steps")
public class WorkflowStep {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "step_name", nullable = false, length = 100)
    private String stepName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_role_id")
    private Role approverRole;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    public WorkflowStep() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Workflow getWorkflow() { return workflow; }
    public void setWorkflow(Workflow workflow) { this.workflow = workflow; }
    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }
    public Role getApproverRole() { return approverRole; }
    public void setApproverRole(Role approverRole) { this.approverRole = approverRole; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
