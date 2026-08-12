package com.srots.infrastructure.mock.repository;

import com.srots.domain.project.Project;
import com.srots.domain.repository.ProjectRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockProjectRepository implements ProjectRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockProjectRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Project> findById(String id) {
        behavior.beforeRead();
        return store.project(id);
    }

    @Override
    public List<Project> findAll() {
        behavior.beforeRead();
        return store.projects();
    }

    @Override
    public List<Project> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.projects(), query, e -> e.getName() + " " + nullToEmpty(e.getDescription()) + " " + e.getId());
    }

    @Override
    public PageResult<Project> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.projects(),
                pageRequest,
                search,
                filters,
                e -> e.getName() + " " + nullToEmpty(e.getDescription()) + " " + e.getId(),
                e -> Map.of("status", e.getStatus().name(), "priority", e.getPriority().name(), "productId", nullToEmpty(e.getProductId())),
                field -> {
                    if ("name".equals(field)) return Comparator.comparing(Project::getName, String.CASE_INSENSITIVE_ORDER);
                    if ("progress".equals(field)) return Comparator.comparingInt(Project::getProgressPercent);
                    if ("targetDate".equals(field)) return Comparator.comparing(Project::getTargetDate, Comparator.nullsLast(Comparator.naturalOrder()));
                    return Comparator.comparing(Project::getId);
                });
    }

    @Override
    public Project save(Project entity) {
        behavior.beforeWrite();
        return store.putProject(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeProject(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
