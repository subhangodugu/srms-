package com.srots.infrastructure.mock.auth;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * DEVELOPMENT ONLY authentication provider for role-aware UI testing.
 */
public final class MockAuthenticationProvider {

    private final Map<String, MockUser> users = new LinkedHashMap<>();
    private volatile MockUser currentUser;

    public MockAuthenticationProvider() {
        register(new MockUser("USER-ADMIN", "Alex Admin", "ADMIN", Set.of("*")));
        register(new MockUser("USER-MANAGER", "Morgan Manager", "MANAGER",
                Set.of("EMPLOYEE_READ", "PROJECT_READ", "PROJECT_WRITE", "TASK_READ", "TASK_WRITE")));
        register(new MockUser("USER-EMPLOYEE", "Eden Employee", "EMPLOYEE",
                Set.of("TASK_READ", "PROJECT_READ", "ISSUE_READ", "KNOWLEDGE_READ")));
        register(new MockUser("USER-DEV", "Devon Developer", "DEVELOPER",
                Set.of("PROJECT_READ", "TASK_READ", "TASK_WRITE", "ISSUE_READ", "ISSUE_WRITE", "ENGINEERING_READ")));
        register(new MockUser("USER-QA", "Quinn QA", "QA",
                Set.of("PROJECT_READ", "ISSUE_READ", "ISSUE_WRITE", "RELEASE_READ", "QA_WRITE")));
        register(new MockUser("USER-DEVOPS", "Drew DevOps", "DEVOPS",
                Set.of("RELEASE_READ", "RELEASE_WRITE", "DEPLOYMENT_READ", "DEPLOYMENT_WRITE")));
        register(new MockUser("USER-SALES", "Sam Sales", "SALES",
                Set.of("CUSTOMER_READ", "SALES_READ", "SALES_WRITE")));
        register(new MockUser("USER-SUPPORT", "Sky Support", "SUPPORT",
                Set.of("CUSTOMER_READ", "SUPPORT_READ", "SUPPORT_WRITE", "KNOWLEDGE_READ")));
        this.currentUser = users.get("USER-ADMIN");
    }

    private void register(MockUser user) {
        users.put(user.getRole(), user);
        users.put(user.getId(), user);
    }

    public MockUser currentUser() {
        return currentUser;
    }

    public void switchUser(String roleOrId) {
        MockUser user = users.get(roleOrId);
        if (user == null) {
            throw new IllegalArgumentException("Unknown mock user: " + roleOrId);
        }
        this.currentUser = user;
    }

    public List<MockUser> availableUsers() {
        return List.of(
                users.get("ADMIN"),
                users.get("MANAGER"),
                users.get("EMPLOYEE"),
                users.get("DEVELOPER"),
                users.get("QA"),
                users.get("DEVOPS"),
                users.get("SALES"),
                users.get("SUPPORT"));
    }

    public Optional<MockUser> findByRole(String role) {
        return Optional.ofNullable(users.get(role));
    }
}
