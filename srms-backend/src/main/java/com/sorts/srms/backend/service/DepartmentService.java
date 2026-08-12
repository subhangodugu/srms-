package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.enums.Status;
import com.sorts.srms.backend.domain.model.Company;
import com.sorts.srms.backend.domain.model.Department;
import com.sorts.srms.backend.dto.DepartmentDTO;
import com.sorts.srms.backend.exception.BusinessValidationException;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.CompanyRepository;
import com.sorts.srms.backend.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    public DepartmentService(DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getDepartmentsByCompany(String companyId) {
        return departmentRepository.findByCompanyId(companyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));

        if (departmentRepository.findByCompanyIdAndCode(dto.getCompanyId(), dto.getCode()).isPresent()) {
            throw new BusinessValidationException("Department code already exists for company: " + dto.getCode());
        }

        Department parent = null;
        if (dto.getParentDepartmentId() != null && !dto.getParentDepartmentId().isBlank()) {
            parent = departmentRepository.findById(dto.getParentDepartmentId()).orElse(null);
        }

        Department dept = new Department(
                UUID.randomUUID().toString(), company, dto.getCode(), dto.getName(), parent, Status.ACTIVE
        );

        return mapToDTO(departmentRepository.save(dept));
    }

    private DepartmentDTO mapToDTO(Department d) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(d.getId());
        dto.setCompanyId(d.getCompany().getId());
        dto.setCompanyName(d.getCompany().getName());
        dto.setCode(d.getCode());
        dto.setName(d.getName());
        if (d.getParentDepartment() != null) {
            dto.setParentDepartmentId(d.getParentDepartment().getId());
            dto.setParentDepartmentName(d.getParentDepartment().getName());
        }
        dto.setStatus(d.getStatus().name());
        return dto;
    }
}
