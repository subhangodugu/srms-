package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    List<Department> findByCompanyId(String companyId);
    Optional<Department> findByCompanyIdAndCode(String companyId, String code);
}
