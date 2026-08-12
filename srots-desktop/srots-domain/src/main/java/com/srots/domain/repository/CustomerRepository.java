package com.srots.domain.repository;

import com.srots.domain.customer.Customer;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findById(String id);
    List<Customer> findAll();
    List<Customer> search(String query);
    PageResult<Customer> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Customer save(Customer entity);
    boolean deleteById(String id);
}
