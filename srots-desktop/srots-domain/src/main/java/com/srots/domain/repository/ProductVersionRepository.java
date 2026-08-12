package com.srots.domain.repository;

import com.srots.domain.version.ProductVersion;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductVersionRepository {
    Optional<ProductVersion> findById(String id);
    List<ProductVersion> findAll();
    List<ProductVersion> search(String query);
    PageResult<ProductVersion> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    ProductVersion save(ProductVersion entity);
    boolean deleteById(String id);
}
