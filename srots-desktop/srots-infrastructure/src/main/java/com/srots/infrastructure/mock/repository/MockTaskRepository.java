package com.srots.infrastructure.mock.repository;

import com.srots.domain.task.Task;
import com.srots.domain.repository.TaskRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockTaskRepository implements TaskRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockTaskRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Task> findById(String id) {
        behavior.beforeRead();
        return store.task(id);
    }

    @Override
    public List<Task> findAll() {
        behavior.beforeRead();
        return store.tasks();
    }

    @Override
    public List<Task> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.tasks(), query, e -> e.getTitle() + " " + e.getId());
    }

    @Override
    public PageResult<Task> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.tasks(),
                pageRequest,
                search,
                filters,
                e -> e.getTitle() + " " + e.getId(),
                e -> Map.of("status", e.getStatus().name(), "priority", e.getPriority().name(), "projectId", nullToEmpty(e.getProjectId())),
                field -> {
                    if ("title".equals(field)) return Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER);
                    if ("dueDate".equals(field)) return Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
                    if ("createdDate".equals(field)) return Comparator.comparing(Task::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder()));
                    return Comparator.comparing(Task::getId);
                });
    }

    @Override
    public Task save(Task entity) {
        behavior.beforeWrite();
        return store.putTask(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeTask(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
