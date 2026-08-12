package com.srots.domain.release;

import com.srots.domain.model.enums.GatePhaseStatus;
import com.srots.domain.model.enums.ReleaseStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class Release {
    private final String id;
    private final String productId;
    private final String versionId;
    private final String releaseName;
    private final ReleaseStatus status;
    private final String ownerEmployeeId;
    private final LocalDate targetDate;
    private final GatePhaseStatus qaStatus;
    private final GatePhaseStatus securityStatus;
    private final GatePhaseStatus approvalStatus;
    private final GatePhaseStatus deploymentStatus;
    private final int progressPercent;
    private final List<ReleasePipelineGate> pipelineGates;

    public Release(String id, String productId, String versionId, String releaseName, ReleaseStatus status,
                   String ownerEmployeeId, LocalDate targetDate, GatePhaseStatus qaStatus,
                   GatePhaseStatus securityStatus, GatePhaseStatus approvalStatus,
                   GatePhaseStatus deploymentStatus, int progressPercent,
                   List<ReleasePipelineGate> pipelineGates) {
        this.id = Objects.requireNonNull(id);
        this.productId = Objects.requireNonNull(productId);
        this.versionId = versionId;
        this.releaseName = Objects.requireNonNull(releaseName);
        this.status = Objects.requireNonNull(status);
        this.ownerEmployeeId = ownerEmployeeId;
        this.targetDate = targetDate;
        this.qaStatus = qaStatus;
        this.securityStatus = securityStatus;
        this.approvalStatus = approvalStatus;
        this.deploymentStatus = deploymentStatus;
        this.progressPercent = progressPercent;
        this.pipelineGates = pipelineGates == null ? List.of() : List.copyOf(pipelineGates);
    }

    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getVersionId() { return versionId; }
    public String getReleaseName() { return releaseName; }
    public ReleaseStatus getStatus() { return status; }
    public String getOwnerEmployeeId() { return ownerEmployeeId; }
    public LocalDate getTargetDate() { return targetDate; }
    public GatePhaseStatus getQaStatus() { return qaStatus; }
    public GatePhaseStatus getSecurityStatus() { return securityStatus; }
    public GatePhaseStatus getApprovalStatus() { return approvalStatus; }
    public GatePhaseStatus getDeploymentStatus() { return deploymentStatus; }
    public int getProgressPercent() { return progressPercent; }
    public List<ReleasePipelineGate> getPipelineGates() { return pipelineGates; }
}
