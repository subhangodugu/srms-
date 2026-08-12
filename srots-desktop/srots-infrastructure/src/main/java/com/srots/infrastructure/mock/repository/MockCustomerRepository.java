package com.srots.infrastructure.mock.repository;

import com.srots.domain.customer.Customer;
import com.srots.domain.repository.CustomerRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockCustomerRepository implements CustomerRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockCustomerRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Customer> findById(String id) {
        behavior.beforeRead();
        return store.customers().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<Customer> findAll() {
        behavior.beforeRead();
        return store.customers();
    }

    @Override
    public List<Customer> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.customers(), query, e -> e.getCompanyName() + " " + nullToEmpty(e.getContactName()) + " " + nullToEmpty(e.getEmail()) + " " + e.getId());
    }

    @Override
    public PageResult<Customer> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.customers(),
                pageRequest,
                search,
                filters,
                e -> e.getCompanyName() + " " + nullToEmpty(e.getContactName()) + " " + nullToEmpty(e.getEmail()) + " " + e.getId(),
                e -> Map.of("status", e.getStatus().name(), "productId", nullToEmpty(e.getProductId())),
                field -> {
                    return Comparator.comparing(Customer::getCompanyName, String.CASE_INSENSITIVE_ORDER);
                });
    }

    @Override
    public Customer save(Customer entity) {
        behavior.beforeWrite();
        return store.putCustomer(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeCustomer(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
