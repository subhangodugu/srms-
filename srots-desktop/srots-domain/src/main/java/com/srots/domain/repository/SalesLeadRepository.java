package com.srots.domain.repository;

import com.srots.domain.sales.SalesLead;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SalesLeadRepository {
    Optional<SalesLead> findById(String id);
    List<SalesLead> findAll();
    List<SalesLead> search(String query);
    PageResult<SalesLead> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    SalesLead save(SalesLead entity);
    boolean deleteById(String id);
}
