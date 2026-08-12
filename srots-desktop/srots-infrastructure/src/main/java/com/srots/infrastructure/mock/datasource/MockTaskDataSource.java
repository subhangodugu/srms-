package com.srots.infrastructure.mock.datasource;

import com.srots.domain.task.Task;
import com.srots.infrastructure.mock.state.MockStateStore;

import java.util.List;
import java.util.Optional;

public final class MockTaskDataSource {
    private final MockStateStore store;

    public MockTaskDataSource(MockStateStore store) { this.store = store; }

    public List<Task> findAll() { return store.tasks(); }
    public Optional<Task> findById(String id) { return store.task(id); }
    public Task save(Task task) { return store.putTask(task); }
    public boolean deleteById(String id) { return store.removeTask(id); }
}
