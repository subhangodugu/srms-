package com.srots.infrastructure.mock.repository;

import com.srots.domain.model.Product;
import com.srots.domain.repository.ProductRepository;
import com.srots.domain.valueobject.ProductId;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockProductRepository implements ProductRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockProductRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        behavior.beforeRead();
        if (id == null) {
            return Optional.empty();
        }
        return store.product(id.getValue());
    }

    @Override
    public List<Product> findAll() {
        behavior.beforeRead();
        return store.products();
    }

    @Override
    public List<Product> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.products(), query,
                p -> p.getName() + " " + p.getCode() + " " + p.getDescription() + " " + p.getId().getValue());
    }

    @Override
    public PageResult<Product> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.products(),
                pageRequest,
                search,
                filters,
                p -> p.getName() + " " + p.getCode() + " " + p.getDescription() + " " + p.getId().getValue(),
                p -> Map.of(
                        "status", p.getStatus() == null ? "" : p.getStatus().name(),
                        "code", p.getCode() == null ? "" : p.getCode()),
                field -> {
                    if ("name".equals(field)) {
                        return Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
                    }
                    if ("code".equals(field)) {
                        return Comparator.comparing(Product::getCode, String.CASE_INSENSITIVE_ORDER);
                    }
                    return Comparator.comparing(p -> p.getId().getValue());
                });
    }

    @Override
    public Product save(Product product) {
        behavior.beforeWrite();
        return store.putProduct(product);
    }

    @Override
    public boolean deleteById(ProductId id) {
        behavior.beforeWrite();
        return id != null && store.removeProduct(id.getValue());
    }
}
