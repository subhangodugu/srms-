package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Optional<Company> findByCode(String code);
    boolean existsByCode(String code);
}
