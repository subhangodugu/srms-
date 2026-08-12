package com.srots.infrastructure.mock.repository;

import com.srots.domain.employee.Employee;
import com.srots.domain.repository.EmployeeRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockEmployeeRepository implements EmployeeRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockEmployeeRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Employee> findById(String id) {
        behavior.beforeRead();
        return store.employee(id);
    }

    @Override
    public List<Employee> findAll() {
        behavior.beforeRead();
        return store.employees();
    }

    @Override
    public List<Employee> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.employees(), query, e -> e.getFullName() + " " + e.getEmail() + " " + e.getJobTitle() + " " + e.getId());
    }

    @Override
    public PageResult<Employee> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.employees(),
                pageRequest,
                search,
                filters,
                e -> e.getFullName() + " " + e.getEmail() + " " + e.getJobTitle() + " " + e.getId(),
                e -> Map.of("status", e.getStatus().name(), "departmentId", nullToEmpty(e.getDepartmentId()), "teamId", nullToEmpty(e.getTeamId())),
                field -> {
                    if ("fullName".equals(field)) return Comparator.comparing(Employee::getFullName, String.CASE_INSENSITIVE_ORDER);
                    if ("email".equals(field)) return Comparator.comparing(Employee::getEmail, String.CASE_INSENSITIVE_ORDER);
                    if ("joinDate".equals(field)) return Comparator.comparing(Employee::getJoinDate, Comparator.nullsLast(Comparator.naturalOrder()));
                    return Comparator.comparing(Employee::getId);
                });
    }

    @Override
    public Employee save(Employee entity) {
        behavior.beforeWrite();
        return store.putEmployee(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeEmployee(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
