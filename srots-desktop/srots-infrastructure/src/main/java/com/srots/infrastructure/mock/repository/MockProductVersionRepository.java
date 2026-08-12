package com.srots.infrastructure.mock.repository;

import com.srots.domain.version.ProductVersion;
import com.srots.domain.repository.ProductVersionRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockProductVersionRepository implements ProductVersionRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockProductVersionRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<ProductVersion> findById(String id) {
        behavior.beforeRead();
        return store.versions().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<ProductVersion> findAll() {
        behavior.beforeRead();
        return store.versions();
    }

    @Override
    public List<ProductVersion> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.versions(), query, e -> e.getVersion() + " " + e.getId());
    }

    @Override
    public PageResult<ProductVersion> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.versions(),
                pageRequest,
                search,
                filters,
                e -> e.getVersion() + " " + e.getId(),
                e -> Map.of("productId", nullToEmpty(e.getProductId()), "status", e.getStatus().name()),
                field -> {
                    return Comparator.comparing(ProductVersion::getVersion);
                });
    }

    @Override
    public ProductVersion save(ProductVersion entity) {
        behavior.beforeWrite();
        return store.putVersion(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeVersion(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
