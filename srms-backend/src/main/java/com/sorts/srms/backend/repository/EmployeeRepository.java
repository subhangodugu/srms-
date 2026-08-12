package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByUserId(String userId);
    Optional<Employee> findByEmployeeCode(String employeeCode);
    List<Employee> findByCompanyId(String companyId);
    List<Employee> findByDepartmentId(String departmentId);
    boolean existsByEmployeeCode(String employeeCode);
}
