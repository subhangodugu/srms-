package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.model.Permission;
import com.sorts.srms.backend.domain.model.Role;
import com.sorts.srms.backend.dto.PermissionDTO;
import com.sorts.srms.backend.dto.RoleDTO;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.PermissionRepository;
import com.sorts.srms.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapRoleToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::mapPermissionToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoleDTO updateRolePermissions(String roleId, Set<String> permissionCodes) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        Set<Permission> newPermissions = new HashSet<>();
        for (String permCode : permissionCodes) {
            permissionRepository.findByCode(permCode).ifPresent(newPermissions::add);
        }

        role.setPermissions(newPermissions);
        Role updated = roleRepository.save(role);
        return mapRoleToDTO(updated);
    }

    private RoleDTO mapRoleToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setCode(role.getCode());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setSystemRole(role.isSystemRole());
        dto.setPermissionCodes(role.getPermissions().stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet()));
        return dto;
    }

    private PermissionDTO mapPermissionToDTO(Permission p) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(p.getId());
        dto.setCode(p.getCode());
        dto.setCategory(p.getCategory());
        dto.setDescription(p.getDescription());
        return dto;
    }
}
