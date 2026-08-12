package com.srots.domain.sales;

import com.srots.domain.model.enums.SalesStage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class SalesDeal {
    private final String id;
    private final String name;
    private final String opportunityId;
    private final String customerId;
    private final String productId;
    private final SalesStage stage;
    private final BigDecimal amount;
    private final String ownerEmployeeId;
    private final LocalDate closedDate;

    public SalesDeal(String id, String name, String opportunityId, String customerId, String productId,
                     SalesStage stage, BigDecimal amount, String ownerEmployeeId, LocalDate closedDate) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.opportunityId = opportunityId;
        this.customerId = customerId;
        this.productId = productId;
        this.stage = Objects.requireNonNull(stage);
        this.amount = amount;
        this.ownerEmployeeId = ownerEmployeeId;
        this.closedDate = closedDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getOpportunityId() { return opportunityId; }
    public String getCustomerId() { return customerId; }
    public String getProductId() { return productId; }
    public SalesStage getStage() { return stage; }
    public BigDecimal getAmount() { return amount; }
    public String getOwnerEmployeeId() { return ownerEmployeeId; }
    public LocalDate getClosedDate() { return closedDate; }
}
