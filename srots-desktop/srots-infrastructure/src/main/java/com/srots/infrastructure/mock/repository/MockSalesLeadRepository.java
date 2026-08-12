package com.srots.infrastructure.mock.repository;

import com.srots.domain.sales.SalesLead;
import com.srots.domain.repository.SalesLeadRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockSalesLeadRepository implements SalesLeadRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockSalesLeadRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<SalesLead> findById(String id) {
        behavior.beforeRead();
        return store.leads().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<SalesLead> findAll() {
        behavior.beforeRead();
        return store.leads();
    }

    @Override
    public List<SalesLead> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.leads(), query, e -> e.getCompanyName() + " " + nullToEmpty(e.getContactName()) + " " + e.getId());
    }

    @Override
    public PageResult<SalesLead> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.leads(),
                pageRequest,
                search,
                filters,
                e -> e.getCompanyName() + " " + nullToEmpty(e.getContactName()) + " " + e.getId(),
                e -> Map.of("stage", e.getStage().name(), "productId", nullToEmpty(e.getProductId())),
                field -> {
                    return Comparator.comparing(SalesLead::getCompanyName, String.CASE_INSENSITIVE_ORDER);
                });
    }

    @Override
    public SalesLead save(SalesLead entity) {
        behavior.beforeWrite();
        return store.putLead(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeLead(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
