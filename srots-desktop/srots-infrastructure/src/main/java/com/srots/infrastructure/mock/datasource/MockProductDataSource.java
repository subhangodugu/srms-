package com.srots.infrastructure.mock.datasource;

import com.srots.domain.model.Product;
import com.srots.infrastructure.mock.state.MockStateStore;

import java.util.List;
import java.util.Optional;

public final class MockProductDataSource {
    private final MockStateStore store;

    public MockProductDataSource(MockStateStore store) { this.store = store; }

    public List<Product> findAll() { return store.products(); }
    public Optional<Product> findById(String id) { return store.product(id); }
    public Product save(Product product) { return store.putProduct(product); }
    public boolean deleteById(String id) { return store.removeProduct(id); }
}
