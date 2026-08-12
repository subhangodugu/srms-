package com.srots.domain.repository;

import com.srots.domain.model.Product;
import com.srots.domain.valueobject.ProductId;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(ProductId id);
    List<Product> findAll();
    List<Product> search(String query);
    PageResult<Product> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Product save(Product product);
    boolean deleteById(ProductId id);
}
