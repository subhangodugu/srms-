package com.srots.domain.department;

import java.util.Objects;

public final class Department {
    private final String id;
    private final String name;
    private final String code;
    private final String headEmployeeId;

    public Department(String id, String name, String code, String headEmployeeId) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.code = Objects.requireNonNull(code);
        this.headEmployeeId = headEmployeeId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getHeadEmployeeId() { return headEmployeeId; }
}
