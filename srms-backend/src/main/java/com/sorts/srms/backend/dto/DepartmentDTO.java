package com.sorts.srms.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentDTO {

    private String id;

    @NotBlank(message = "Company ID is required")
    private String companyId;

    private String companyName;

    @NotBlank(message = "Department Code is required")
    private String code;

    @NotBlank(message = "Department Name is required")
    private String name;

    private String parentDepartmentId;
    private String parentDepartmentName;
    private String status;

    public DepartmentDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getParentDepartmentId() { return parentDepartmentId; }
    public void setParentDepartmentId(String parentDepartmentId) { this.parentDepartmentId = parentDepartmentId; }
    public String getParentDepartmentName() { return parentDepartmentName; }
    public void setParentDepartmentName(String parentDepartmentName) { this.parentDepartmentName = parentDepartmentName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
