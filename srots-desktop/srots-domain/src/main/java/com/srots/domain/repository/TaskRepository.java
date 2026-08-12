package com.srots.domain.repository;

import com.srots.domain.task.Task;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskRepository {
    Optional<Task> findById(String id);
    List<Task> findAll();
    List<Task> search(String query);
    PageResult<Task> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Task save(Task entity);
    boolean deleteById(String id);
}
