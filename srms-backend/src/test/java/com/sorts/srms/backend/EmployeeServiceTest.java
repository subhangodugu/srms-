package com.sorts.srms.backend;

import com.sorts.srms.backend.dto.EmployeeDTO;
import com.sorts.srms.backend.exception.BusinessValidationException;
import com.sorts.srms.backend.repository.*;
import com.sorts.srms.backend.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private UserRepository userRepository;
    @Mock private CompanyRepository companyRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuditLogRepository auditLogRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository, userRepository, companyRepository, departmentRepository, roleRepository, passwordEncoder, auditLogRepository);
    }

    @Test
    void testCreateEmployeeDuplicateCodeThrowsException() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeCode("EMP-999");
        dto.setEmail("duplicate@test.com");

        when(employeeRepository.existsByEmployeeCode("EMP-999")).thenReturn(true);

        assertThrows(BusinessValidationException.class, () ->
                employeeService.createEmployee(dto, "u-1", "user1")
        );
    }
}
