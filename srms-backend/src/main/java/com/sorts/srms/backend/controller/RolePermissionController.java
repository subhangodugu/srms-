package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.PermissionDTO;
import com.sorts.srms.backend.dto.RoleDTO;
import com.sorts.srms.backend.service.RolePermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/roles")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(rolePermissionService.getAllRoles());
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('ROLE_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(rolePermissionService.getAllPermissions());
    }

    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('ROLE_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<RoleDTO> updateRolePermissions(
            @PathVariable String roleId,
            @RequestBody Set<String> permissionCodes) {
        return ResponseEntity.ok(rolePermissionService.updateRolePermissions(roleId, permissionCodes));
    }
}
