package com.srots.infrastructure.mock.repository;

import com.srots.domain.sales.SalesDeal;
import com.srots.domain.repository.SalesDealRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockSalesDealRepository implements SalesDealRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockSalesDealRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<SalesDeal> findById(String id) {
        behavior.beforeRead();
        return store.deals().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<SalesDeal> findAll() {
        behavior.beforeRead();
        return store.deals();
    }

    @Override
    public List<SalesDeal> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.deals(), query, e -> e.getName() + " " + e.getId());
    }

    @Override
    public PageResult<SalesDeal> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.deals(),
                pageRequest,
                search,
                filters,
                e -> e.getName() + " " + e.getId(),
                e -> Map.of("stage", e.getStage().name(), "productId", nullToEmpty(e.getProductId())),
                field -> {
                    return Comparator.comparing(SalesDeal::getName, String.CASE_INSENSITIVE_ORDER);
                });
    }

    @Override
    public SalesDeal save(SalesDeal entity) {
        behavior.beforeWrite();
        return store.putDeal(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeDeal(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
