package com.srots.domain.repository;

import com.srots.domain.employee.Employee;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeRepository {
    Optional<Employee> findById(String id);
    List<Employee> findAll();
    List<Employee> search(String query);
    PageResult<Employee> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Employee save(Employee entity);
    boolean deleteById(String id);
}
