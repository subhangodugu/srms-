package com.srots.domain.repository;

import com.srots.domain.sales.SalesDeal;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SalesDealRepository {
    Optional<SalesDeal> findById(String id);
    List<SalesDeal> findAll();
    List<SalesDeal> search(String query);
    PageResult<SalesDeal> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    SalesDeal save(SalesDeal entity);
    boolean deleteById(String id);
}
