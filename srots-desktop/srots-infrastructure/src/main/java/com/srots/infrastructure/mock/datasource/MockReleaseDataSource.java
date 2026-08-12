package com.srots.infrastructure.mock.datasource;

import com.srots.domain.release.Release;
import com.srots.infrastructure.mock.state.MockStateStore;

import java.util.List;
import java.util.Optional;

public final class MockReleaseDataSource {
    private final MockStateStore store;

    public MockReleaseDataSource(MockStateStore store) { this.store = store; }

    public List<Release> findAll() { return store.releases(); }
    public Optional<Release> findById(String id) { return store.release(id); }
    public Release save(Release release) { return store.putRelease(release); }
    public boolean deleteById(String id) { return store.removeRelease(id); }
}
