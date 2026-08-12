package com.sorts.srms.backend.dto;

import java.util.Set;

public class AuthResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String companyId;
    private String companyName;
    private String roleCode;
    private String roleName;
    private Set<String> permissions;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String userId, String username, String email, String firstName, String lastName, String companyId, String companyName, String roleCode, String roleName, Set<String> permissions) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
}
