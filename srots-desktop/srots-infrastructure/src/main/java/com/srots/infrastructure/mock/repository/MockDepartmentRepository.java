package com.srots.infrastructure.mock.repository;

import com.srots.domain.department.Department;
import com.srots.domain.repository.DepartmentRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockDepartmentRepository implements DepartmentRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockDepartmentRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Department> findById(String id) {
        behavior.beforeRead();
        return store.departments().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<Department> findAll() {
        behavior.beforeRead();
        return store.departments();
    }

    @Override
    public List<Department> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.departments(), query, e -> e.getName() + " " + e.getCode() + " " + e.getId());
    }

    @Override
    public PageResult<Department> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.departments(),
                pageRequest,
                search,
                filters,
                e -> e.getName() + " " + e.getCode() + " " + e.getId(),
                e -> Map.of("code", e.getCode()),
                field -> {
                    return Comparator.comparing(Department::getName, String.CASE_INSENSITIVE_ORDER);
                });
    }

    @Override
    public Department save(Department entity) {
        behavior.beforeWrite();
        return store.putDepartment(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeDepartment(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
