package com.srots.domain.employee;

import com.srots.domain.model.enums.EmployeeStatus;
import java.time.LocalDate;
import java.util.Objects;

public final class Employee {
    private final String id;
    private final String fullName;
    private final String email;
    private final String jobTitle;
    private final String departmentId;
    private final String teamId;
    private final EmployeeStatus status;
    private final LocalDate joinDate;
    private final String managerId;
    private final String location;
    private final String initials;

    public Employee(String id, String fullName, String email, String jobTitle, String departmentId,
                    String teamId, EmployeeStatus status, LocalDate joinDate, String managerId,
                    String location, String initials) {
        this.id = Objects.requireNonNull(id);
        this.fullName = Objects.requireNonNull(fullName);
        this.email = Objects.requireNonNull(email);
        this.jobTitle = Objects.requireNonNull(jobTitle);
        this.departmentId = departmentId;
        this.teamId = teamId;
        this.status = Objects.requireNonNull(status);
        this.joinDate = joinDate;
        this.managerId = managerId;
        this.location = location;
        this.initials = initials;
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getJobTitle() { return jobTitle; }
    public String getDepartmentId() { return departmentId; }
    public String getTeamId() { return teamId; }
    public EmployeeStatus getStatus() { return status; }
    public LocalDate getJoinDate() { return joinDate; }
    public String getManagerId() { return managerId; }
    public String getLocation() { return location; }
    public String getInitials() { return initials; }
}
