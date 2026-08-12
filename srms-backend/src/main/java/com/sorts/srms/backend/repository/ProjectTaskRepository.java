package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, String> {
    List<ProjectTask> findByProjectId(String projectId);
    List<ProjectTask> findByAssigneeId(String assigneeId);
}
