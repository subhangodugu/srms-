package com.srots.domain.sales;

import com.srots.domain.model.enums.SalesStage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class SalesOpportunity {
    private final String id;
    private final String name;
    private final String customerId;
    private final String productId;
    private final SalesStage stage;
    private final BigDecimal amount;
    private final String ownerEmployeeId;
    private final LocalDate closeDate;

    public SalesOpportunity(String id, String name, String customerId, String productId, SalesStage stage,
                            BigDecimal amount, String ownerEmployeeId, LocalDate closeDate) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.customerId = customerId;
        this.productId = productId;
        this.stage = Objects.requireNonNull(stage);
        this.amount = amount;
        this.ownerEmployeeId = ownerEmployeeId;
        this.closeDate = closeDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCustomerId() { return customerId; }
    public String getProductId() { return productId; }
    public SalesStage getStage() { return stage; }
    public BigDecimal getAmount() { return amount; }
    public String getOwnerEmployeeId() { return ownerEmployeeId; }
    public LocalDate getCloseDate() { return closeDate; }
}
