package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    List<Asset> findByCompanyId(String companyId);
    Optional<Asset> findByAssetTag(String assetTag);
    List<Asset> findByAssignedToEmployeeId(String employeeId);
}
