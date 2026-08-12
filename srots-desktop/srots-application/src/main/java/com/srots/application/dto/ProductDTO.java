package com.srots.application.dto;

public class ProductDTO {

    private final String id;
    private final String name;
    private final String code;
    private final String description;
    private final String currentVersion;
    private final String nextVersion;
    private final String owner;

    public ProductDTO(String id, String name, String code, String description, String currentVersion, String nextVersion, String owner) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.currentVersion = currentVersion;
        this.nextVersion = nextVersion;
        this.owner = owner;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public String getCurrentVersion() { return currentVersion; }
    public String getNextVersion() { return nextVersion; }
    public String getOwner() { return owner; }
}
