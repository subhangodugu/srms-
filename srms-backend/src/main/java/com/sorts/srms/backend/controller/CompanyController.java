package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.CompanyDTO;
import com.sorts.srms.backend.security.CustomUserDetails;
import com.sorts.srms.backend.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMPANY_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable String id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMPANY_CREATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyDTO> createCompany(
            @Valid @RequestBody CompanyDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails != null ? userDetails.getUser().getId() : "SYSTEM";
        String username = userDetails != null ? userDetails.getUsername() : "system";
        CompanyDTO created = companyService.createCompany(dto, userId, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyDTO> updateCompany(
            @PathVariable String id,
            @Valid @RequestBody CompanyDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails != null ? userDetails.getUser().getId() : "SYSTEM";
        String username = userDetails != null ? userDetails.getUsername() : "system";
        CompanyDTO updated = companyService.updateCompany(id, dto, userId, username);
        return ResponseEntity.ok(updated);
    }
}
