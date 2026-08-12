package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.EmployeeDTO;
import com.sorts.srms.backend.security.CustomUserDetails;
import com.sorts.srms.backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(employeeService.getEmployeesByCompany(companyId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Valid @RequestBody EmployeeDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails != null ? userDetails.getUser().getId() : "SYSTEM";
        String username = userDetails != null ? userDetails.getUsername() : "system";
        EmployeeDTO created = employeeService.createEmployee(dto, userId, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
