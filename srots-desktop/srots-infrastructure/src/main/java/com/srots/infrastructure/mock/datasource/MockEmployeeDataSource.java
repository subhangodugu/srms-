package com.srots.infrastructure.mock.datasource;

import com.srots.domain.employee.Employee;
import com.srots.infrastructure.mock.state.MockStateStore;

import java.util.List;
import java.util.Optional;

/** Mock data source for employees (no SQL / REST). */
public final class MockEmployeeDataSource {
    private final MockStateStore store;

    public MockEmployeeDataSource(MockStateStore store) {
        this.store = store;
    }

    public List<Employee> findAll() { return store.employees(); }
    public Optional<Employee> findById(String id) { return store.employee(id); }
    public Employee save(Employee employee) { return store.putEmployee(employee); }
    public boolean deleteById(String id) { return store.removeEmployee(id); }
}
