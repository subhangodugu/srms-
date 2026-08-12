package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.enums.Status;
import com.sorts.srms.backend.domain.model.*;
import com.sorts.srms.backend.dto.EmployeeDTO;
import com.sorts.srms.backend.exception.BusinessValidationException;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogRepository auditLogRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository,
                           CompanyRepository companyRepository, DepartmentRepository departmentRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           AuditLogRepository auditLogRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByCompany(String companyId) {
        return employeeRepository.findByCompanyId(companyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return mapToDTO(employee);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO dto, String currentUserId, String currentUsername) {
        if (employeeRepository.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new BusinessValidationException("Employee code already exists: " + dto.getEmployeeCode());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessValidationException("User email already exists: " + dto.getEmail());
        }

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));

        Department department = null;
        if (dto.getDepartmentId() != null && !dto.getDepartmentId().isBlank()) {
            department = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
        }

        Role role = roleRepository.findByCode(dto.getRoleCode() != null ? dto.getRoleCode() : "EMPLOYEE")
                .orElseGet(() -> roleRepository.findAll().stream().findFirst().orElseThrow());

        // Create User account
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setCompany(company);
        user.setDepartment(department);
        user.setUsername(dto.getUsername() != null && !dto.getUsername().isBlank() ? dto.getUsername() : dto.getEmail());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode("Default@123456"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setRole(role);
        user.setStatus(Status.ACTIVE);
        User savedUser = userRepository.save(user);

        // Create Employee profile
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setUser(savedUser);
        employee.setCompany(company);
        employee.setDepartment(department);
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setJobTitle(dto.getJobTitle());
        employee.setEmploymentType(dto.getEmploymentType() != null ? dto.getEmploymentType() : "FULL_TIME");
        employee.setHireDate(dto.getHireDate() != null ? dto.getHireDate() : LocalDate.now());
        employee.setSalary(dto.getSalary() != null ? dto.getSalary() : BigDecimal.ZERO);
        employee.setStatus(Status.ACTIVE);

        if (dto.getManagerId() != null && !dto.getManagerId().isBlank()) {
            employeeRepository.findById(dto.getManagerId()).ifPresent(employee::setManager);
        }

        Employee savedEmployee = employeeRepository.save(employee);

        auditLogRepository.save(new AuditLog(
                UUID.randomUUID().toString(), currentUserId, currentUsername, company.getId(),
                "CREATE_EMPLOYEE", "EMPLOYEE", savedEmployee.getId(), "Created employee " + savedEmployee.getEmployeeCode() + " (" + dto.getFirstName() + " " + dto.getLastName() + ")", "127.0.0.1", "SUCCESS"
        ));

        return mapToDTO(savedEmployee);
    }

    private EmployeeDTO mapToDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setUserId(e.getUser().getId());
        dto.setCompanyId(e.getCompany().getId());
        dto.setCompanyName(e.getCompany().getName());
        if (e.getDepartment() != null) {
            dto.setDepartmentId(e.getDepartment().getId());
            dto.setDepartmentName(e.getDepartment().getName());
        }
        dto.setEmployeeCode(e.getEmployeeCode());
        dto.setJobTitle(e.getJobTitle());
        dto.setFirstName(e.getUser().getFirstName());
        dto.setLastName(e.getUser().getLastName());
        dto.setEmail(e.getUser().getEmail());
        dto.setUsername(e.getUser().getUsername());
        dto.setPhone(e.getUser().getPhone());

        if (e.getUser().getRole() != null) {
            dto.setRoleId(e.getUser().getRole().getId());
            dto.setRoleCode(e.getUser().getRole().getCode());
            dto.setRoleName(e.getUser().getRole().getName());
        }

        if (e.getManager() != null) {
            dto.setManagerId(e.getManager().getId());
            dto.setManagerName(e.getManager().getUser().getFirstName() + " " + e.getManager().getUser().getLastName());
        }

        dto.setEmploymentType(e.getEmploymentType());
        dto.setHireDate(e.getHireDate());
        dto.setSalary(e.getSalary());
        dto.setStatus(e.getStatus().name());
        return dto;
    }
}
