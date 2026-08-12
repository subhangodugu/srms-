package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByCompanyId(String companyId);
    Optional<Project> findByCode(String code);
    boolean existsByCode(String code);
}
