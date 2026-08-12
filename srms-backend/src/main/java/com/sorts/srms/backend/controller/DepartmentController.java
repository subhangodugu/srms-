package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.DepartmentDTO;
import com.sorts.srms.backend.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('DEPT_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(departmentService.getDepartmentsByCompany(companyId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DEPT_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO dto) {
        DepartmentDTO created = departmentService.createDepartment(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
