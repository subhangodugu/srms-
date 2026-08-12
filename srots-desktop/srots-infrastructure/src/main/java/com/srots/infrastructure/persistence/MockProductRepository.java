package com.srots.infrastructure.persistence;

import com.srots.domain.model.Product;
import com.srots.domain.repository.ProductRepository;
import com.srots.domain.valueobject.ProductId;
import com.srots.infrastructure.mock.MockInfrastructure;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @deprecated Use {@link com.srots.infrastructure.mock.repository.MockProductRepository} via {@link MockInfrastructure}.
 * Kept as a thin compatibility delegate for older call sites.
 */
@Deprecated
public class MockProductRepository implements ProductRepository {

    private final ProductRepository delegate;

    public MockProductRepository() {
        this.delegate = MockInfrastructure.forDevelopment().productRepository();
    }

    public MockProductRepository(ProductRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return delegate.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return delegate.findAll();
    }

    @Override
    public List<Product> search(String query) {
        return delegate.search(query);
    }

    @Override
    public PageResult<Product> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        return delegate.findPage(pageRequest, search, filters);
    }

    @Override
    public Product save(Product product) {
        return delegate.save(product);
    }

    @Override
    public boolean deleteById(ProductId id) {
        return delegate.deleteById(id);
    }
}
