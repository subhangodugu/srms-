package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.enums.AssetStatus;
import com.sorts.srms.backend.domain.model.Asset;
import com.sorts.srms.backend.domain.model.Company;
import com.sorts.srms.backend.domain.model.Employee;
import com.sorts.srms.backend.dto.AssetDTO;
import com.sorts.srms.backend.exception.BusinessValidationException;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.AssetRepository;
import com.sorts.srms.backend.repository.CompanyRepository;
import com.sorts.srms.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public AssetService(AssetRepository assetRepository, CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.assetRepository = assetRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<AssetDTO> getAssetsByCompany(String companyId) {
        return assetRepository.findByCompanyId(companyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssetDTO createAsset(AssetDTO dto) {
        if (assetRepository.findByAssetTag(dto.getAssetTag()).isPresent()) {
            throw new BusinessValidationException("Asset tag already exists: " + dto.getAssetTag());
        }

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));

        Asset asset = new Asset();
        asset.setId(UUID.randomUUID().toString());
        asset.setCompany(company);
        asset.setAssetTag(dto.getAssetTag());
        asset.setName(dto.getName());
        asset.setCategory(dto.getCategory());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setPurchaseDate(dto.getPurchaseDate() != null ? dto.getPurchaseDate() : LocalDate.now());
        asset.setPurchaseCost(dto.getPurchaseCost() != null ? dto.getPurchaseCost() : BigDecimal.ZERO);
        asset.setStatus(dto.getStatus() != null ? AssetStatus.valueOf(dto.getStatus()) : AssetStatus.AVAILABLE);

        if (dto.getAssignedToEmployeeId() != null && !dto.getAssignedToEmployeeId().isBlank()) {
            employeeRepository.findById(dto.getAssignedToEmployeeId()).ifPresent(emp -> {
                asset.setAssignedToEmployee(emp);
                asset.setStatus(AssetStatus.ASSIGNED);
            });
        }

        return mapToDTO(assetRepository.save(asset));
    }

    @Transactional
    public AssetDTO assignAsset(String assetId, String employeeId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        if (employeeId != null && !employeeId.isBlank()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
            asset.setAssignedToEmployee(employee);
            asset.setStatus(AssetStatus.ASSIGNED);
        } else {
            asset.setAssignedToEmployee(null);
            asset.setStatus(AssetStatus.AVAILABLE);
        }

        return mapToDTO(assetRepository.save(asset));
    }

    private AssetDTO mapToDTO(Asset a) {
        AssetDTO dto = new AssetDTO();
        dto.setId(a.getId());
        dto.setCompanyId(a.getCompany().getId());
        dto.setAssetTag(a.getAssetTag());
        dto.setName(a.getName());
        dto.setCategory(a.getCategory());
        dto.setSerialNumber(a.getSerialNumber());
        if (a.getAssignedToEmployee() != null) {
            dto.setAssignedToEmployeeId(a.getAssignedToEmployee().getId());
            dto.setAssignedToEmployeeName(a.getAssignedToEmployee().getUser().getFirstName() + " " + a.getAssignedToEmployee().getUser().getLastName());
        }
        dto.setPurchaseDate(a.getPurchaseDate());
        dto.setPurchaseCost(a.getPurchaseCost());
        dto.setStatus(a.getStatus().name());
        return dto;
    }
}
