package com.srots.domain.repository;

import com.srots.domain.department.Department;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DepartmentRepository {
    Optional<Department> findById(String id);
    List<Department> findAll();
    List<Department> search(String query);
    PageResult<Department> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Department save(Department entity);
    boolean deleteById(String id);
}
