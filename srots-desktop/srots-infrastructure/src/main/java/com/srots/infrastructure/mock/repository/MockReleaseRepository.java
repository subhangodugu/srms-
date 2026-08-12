package com.srots.infrastructure.mock.repository;

import com.srots.domain.release.Release;
import com.srots.domain.repository.ReleaseRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockReleaseRepository implements ReleaseRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockReleaseRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Release> findById(String id) {
        behavior.beforeRead();
        return store.release(id);
    }

    @Override
    public List<Release> findAll() {
        behavior.beforeRead();
        return store.releases();
    }

    @Override
    public List<Release> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.releases(), query, e -> e.getReleaseName() + " " + e.getId());
    }

    @Override
    public PageResult<Release> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.releases(),
                pageRequest,
                search,
                filters,
                e -> e.getReleaseName() + " " + e.getId(),
                e -> Map.of("status", e.getStatus().name(), "productId", nullToEmpty(e.getProductId())),
                field -> {
                    if ("releaseName".equals(field)) return Comparator.comparing(Release::getReleaseName, String.CASE_INSENSITIVE_ORDER);
                    if ("targetDate".equals(field)) return Comparator.comparing(Release::getTargetDate, Comparator.nullsLast(Comparator.naturalOrder()));
                    return Comparator.comparing(Release::getId);
                });
    }

    @Override
    public Release save(Release entity) {
        behavior.beforeWrite();
        return store.putRelease(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeRelease(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
