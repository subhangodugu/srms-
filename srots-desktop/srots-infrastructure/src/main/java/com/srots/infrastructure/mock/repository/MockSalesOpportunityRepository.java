package com.srots.infrastructure.mock.repository;

import com.srots.domain.sales.SalesOpportunity;
import com.srots.domain.repository.SalesOpportunityRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockSalesOpportunityRepository implements SalesOpportunityRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockSalesOpportunityRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<SalesOpportunity> findById(String id) {
        behavior.beforeRead();
        return store.opportunities().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<SalesOpportunity> findAll() {
        behavior.beforeRead();
        return store.opportunities();
    }

    @Override
    public List<SalesOpportunity> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.opportunities(), query, e -> e.getName() + " " + e.getId());
    }

    @Override
    public PageResult<SalesOpportunity> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.opportunities(),
                pageRequest,
                search,
                filters,
                e -> e.getName() + " " + e.getId(),
                e -> Map.of("stage", e.getStage().name(), "productId", nullToEmpty(e.getProductId())),
                field -> {
                    return Comparator.comparing(SalesOpportunity::getName, String.CASE_INSENSITIVE_ORDER);
                });
    }

    @Override
    public SalesOpportunity save(SalesOpportunity entity) {
        behavior.beforeWrite();
        return store.putOpportunity(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeOpportunity(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
