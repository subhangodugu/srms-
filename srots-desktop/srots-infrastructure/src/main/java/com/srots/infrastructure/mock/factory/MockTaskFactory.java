package com.srots.infrastructure.mock.factory;

import com.srots.domain.model.enums.TaskPriority;
import com.srots.domain.model.enums.TaskStatus;
import com.srots.domain.task.Task;
import com.srots.infrastructure.mock.configuration.MockConfiguration;

public final class MockTaskFactory {

    private MockTaskFactory() {
    }

    public static Task create(String id, String title, String projectId, String assigneeId,
                              TaskStatus status, TaskPriority priority, int progress) {
        var ref = MockConfiguration.MOCK_REFERENCE_DATE;
        return new Task(id, title, projectId, assigneeId, status, priority,
                ref.plusDays(14), progress, ref.minusDays(7));
    }
}
