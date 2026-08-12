package com.srots.domain.version;

import com.srots.domain.model.enums.VersionStatus;
import java.time.LocalDate;
import java.util.Objects;

public final class ProductVersion {
    private final String id;
    private final String productId;
    private final String version;
    private final VersionStatus status;
    private final LocalDate releaseDate;
    private final LocalDate targetDate;
    private final int featureCount;
    private final int bugCount;
    private final int completionPercent;

    public ProductVersion(String id, String productId, String version, VersionStatus status,
                          LocalDate releaseDate, LocalDate targetDate, int featureCount,
                          int bugCount, int completionPercent) {
        this.id = Objects.requireNonNull(id);
        this.productId = Objects.requireNonNull(productId);
        this.version = Objects.requireNonNull(version);
        this.status = Objects.requireNonNull(status);
        this.releaseDate = releaseDate;
        this.targetDate = targetDate;
        this.featureCount = featureCount;
        this.bugCount = bugCount;
        this.completionPercent = completionPercent;
    }

    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getVersion() { return version; }
    public VersionStatus getStatus() { return status; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public LocalDate getTargetDate() { return targetDate; }
    public int getFeatureCount() { return featureCount; }
    public int getBugCount() { return bugCount; }
    public int getCompletionPercent() { return completionPercent; }
}
