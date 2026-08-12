package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.enums.Status;
import com.sorts.srms.backend.domain.model.AuditLog;
import com.sorts.srms.backend.domain.model.Company;
import com.sorts.srms.backend.dto.CompanyDTO;
import com.sorts.srms.backend.exception.BusinessValidationException;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.AuditLogRepository;
import com.sorts.srms.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AuditLogRepository auditLogRepository;

    public CompanyService(CompanyRepository companyRepository, AuditLogRepository auditLogRepository) {
        this.companyRepository = companyRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompanyDTO getCompanyById(String id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        return mapToDTO(company);
    }

    @Transactional
    public CompanyDTO createCompany(CompanyDTO dto, String currentUserId, String currentUsername) {
        if (companyRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Company code already exists: " + dto.getCode());
        }

        Company company = new Company(
                UUID.randomUUID().toString(), dto.getCode(), dto.getName(),
                dto.getTaxId(), dto.getEmail(), dto.getPhone(), dto.getAddress(),
                Status.ACTIVE, currentUsername
        );

        Company saved = companyRepository.save(company);

        auditLogRepository.save(new AuditLog(
                UUID.randomUUID().toString(), currentUserId, currentUsername, saved.getId(),
                "CREATE_COMPANY", "COMPANY", saved.getId(), "Created company: " + saved.getName(), "127.0.0.1", "SUCCESS"
        ));

        return mapToDTO(saved);
    }

    @Transactional
    public CompanyDTO updateCompany(String id, CompanyDTO dto, String currentUserId, String currentUsername) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        company.setName(dto.getName());
        company.setTaxId(dto.getTaxId());
        company.setEmail(dto.getEmail());
        company.setPhone(dto.getPhone());
        company.setAddress(dto.getAddress());
        if (dto.getStatus() != null) {
            company.setStatus(Status.valueOf(dto.getStatus()));
        }

        Company updated = companyRepository.save(company);

        auditLogRepository.save(new AuditLog(
                UUID.randomUUID().toString(), currentUserId, currentUsername, updated.getId(),
                "UPDATE_COMPANY", "COMPANY", updated.getId(), "Updated company details for " + updated.getName(), "127.0.0.1", "SUCCESS"
        ));

        return mapToDTO(updated);
    }

    private CompanyDTO mapToDTO(Company c) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(c.getId());
        dto.setCode(c.getCode());
        dto.setName(c.getName());
        dto.setTaxId(c.getTaxId());
        dto.setEmail(c.getEmail());
        dto.setPhone(c.getPhone());
        dto.setAddress(c.getAddress());
        dto.setStatus(c.getStatus().name());
        return dto;
    }
}
