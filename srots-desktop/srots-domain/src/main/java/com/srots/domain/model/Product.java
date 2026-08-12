package com.srots.domain.model;

import com.srots.domain.model.enums.ProductStatus;
import com.srots.domain.valueobject.ProductId;
import com.srots.domain.valueobject.VersionNumber;

import java.time.LocalDate;

public class Product {

    private final ProductId id;
    private final String name;
    private final String code;
    private final String description;
    private VersionNumber currentVersion;
    private VersionNumber nextVersion;
    private final String owner;
    private final String category;
    private final ProductStatus status;
    private final String teamId;
    private final String repositoryUrl;
    private final LocalDate createdDate;

    public Product(ProductId id, String name, String code, String description, VersionNumber currentVersion,
                   VersionNumber nextVersion, String owner) {
        this(id, name, code, description, currentVersion, nextVersion, owner,
                "Enterprise Platform", ProductStatus.ACTIVE, null, null, null);
    }

    public Product(ProductId id, String name, String code, String description, VersionNumber currentVersion,
                   VersionNumber nextVersion, String owner, String category, ProductStatus status,
                   String teamId, String repositoryUrl, LocalDate createdDate) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.currentVersion = currentVersion;
        this.nextVersion = nextVersion;
        this.owner = owner;
        this.category = category;
        this.status = status == null ? ProductStatus.ACTIVE : status;
        this.teamId = teamId;
        this.repositoryUrl = repositoryUrl;
        this.createdDate = createdDate;
    }

    public ProductId getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public VersionNumber getCurrentVersion() { return currentVersion; }
    public VersionNumber getNextVersion() { return nextVersion; }
    public String getOwner() { return owner; }
    public String getCategory() { return category; }
    public ProductStatus getStatus() { return status; }
    public String getTeamId() { return teamId; }
    public String getRepositoryUrl() { return repositoryUrl; }
    public LocalDate getCreatedDate() { return createdDate; }

    public void updateNextVersion(VersionNumber versionNumber) {
        this.nextVersion = versionNumber;
    }
}
