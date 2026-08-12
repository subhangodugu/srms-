package com.srots.domain.model;

import com.srots.domain.valueobject.GateStatus;

public class ReleaseGate {

    private final String id;
    private final String discipline;
    private final String description;
    private GateStatus status;
    private String approvedBy;
    private String comments;

    public ReleaseGate(String id, String discipline, String description, GateStatus status, String approvedBy, String comments) {
        this.id = id;
        this.discipline = discipline;
        this.description = description;
        this.status = status;
        this.approvedBy = approvedBy;
        this.comments = comments;
    }

    public String getId() { return id; }
    public String getDiscipline() { return discipline; }
    public String getDescription() { return description; }
    public GateStatus getStatus() { return status; }
    public String getApprovedBy() { return approvedBy; }
    public String getComments() { return comments; }

    public void approve(String approvedBy, String comments) {
        this.status = GateStatus.PASSED;
        this.approvedBy = approvedBy;
        this.comments = comments;
    }
}
