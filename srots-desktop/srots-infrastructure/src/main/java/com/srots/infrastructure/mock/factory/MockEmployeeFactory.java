package com.srots.infrastructure.mock.factory;

import com.srots.domain.employee.Employee;
import com.srots.domain.model.enums.EmployeeStatus;
import com.srots.domain.model.enums.ProjectPriority;
import com.srots.domain.model.enums.ProjectStatus;
import com.srots.domain.model.enums.TaskPriority;
import com.srots.domain.model.enums.TaskStatus;
import com.srots.domain.project.Project;
import com.srots.domain.task.Task;
import com.srots.infrastructure.mock.configuration.MockConfiguration;

import java.time.LocalDate;

/**
 * Programmatic factories for realistic synthetic records.
 */
public final class MockEmployeeFactory {

    private MockEmployeeFactory() {
    }

    public static Employee create(String id, String fullName, String email, String jobTitle,
                                  String departmentId, String teamId, String managerId, String location) {
        String initials = initials(fullName);
        return new Employee(id, fullName, email, jobTitle, departmentId, teamId, EmployeeStatus.ACTIVE,
                MockConfiguration.MOCK_REFERENCE_DATE.minusDays(120), managerId, location, initials);
    }

    private static String initials(String name) {
        String[] parts = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isBlank()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return sb.length() >= 2 ? sb.substring(0, 2) : sb.toString();
    }
}
