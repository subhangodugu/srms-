package com.sorts.srms.backend.dto;

import java.util.Set;

public class RoleDTO {

    private String id;
    private String code;
    private String name;
    private String description;
    private boolean systemRole;
    private Set<String> permissionCodes;

    public RoleDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isSystemRole() { return systemRole; }
    public void setSystemRole(boolean systemRole) { this.systemRole = systemRole; }
    public Set<String> getPermissionCodes() { return permissionCodes; }
    public void setPermissionCodes(Set<String> permissionCodes) { this.permissionCodes = permissionCodes; }
}
