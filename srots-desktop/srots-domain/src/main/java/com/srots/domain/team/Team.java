package com.srots.domain.team;

import java.util.Objects;

public final class Team {
    private final String id;
    private final String name;
    private final String departmentId;
    private final String leadEmployeeId;

    public Team(String id, String name, String departmentId, String leadEmployeeId) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.departmentId = Objects.requireNonNull(departmentId);
        this.leadEmployeeId = leadEmployeeId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartmentId() { return departmentId; }
    public String getLeadEmployeeId() { return leadEmployeeId; }
}
