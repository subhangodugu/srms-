package com.srots.infrastructure.mock.factory;

import com.srots.domain.model.enums.ProjectPriority;
import com.srots.domain.model.enums.ProjectStatus;
import com.srots.domain.project.Project;
import com.srots.infrastructure.mock.configuration.MockConfiguration;

public final class MockProjectFactory {

    private MockProjectFactory() {
    }

    public static Project create(String id, String name, String description, String productId,
                                 String ownerEmployeeId, String teamId, ProjectStatus status,
                                 ProjectPriority priority, int progress) {
        LocalDates dates = LocalDates.relative(30, 60);
        return new Project(id, name, description, productId, ownerEmployeeId, teamId,
                status, priority, dates.start, dates.target, progress);
    }

    private record LocalDates(java.time.LocalDate start, java.time.LocalDate target) {
        static LocalDates relative(int startDaysAgo, int targetDaysAhead) {
            var ref = MockConfiguration.MOCK_REFERENCE_DATE;
            return new LocalDates(ref.minusDays(startDaysAgo), ref.plusDays(targetDaysAhead));
        }
    }
}
