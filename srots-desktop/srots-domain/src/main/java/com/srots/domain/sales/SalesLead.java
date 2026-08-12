package com.srots.domain.sales;

import com.srots.domain.model.enums.SalesStage;
import java.time.LocalDate;
import java.util.Objects;

public final class SalesLead {
    private final String id;
    private final String companyName;
    private final String contactName;
    private final String email;
    private final SalesStage stage;
    private final String productId;
    private final String ownerEmployeeId;
    private final LocalDate createdDate;

    public SalesLead(String id, String companyName, String contactName, String email, SalesStage stage,
                     String productId, String ownerEmployeeId, LocalDate createdDate) {
        this.id = Objects.requireNonNull(id);
        this.companyName = Objects.requireNonNull(companyName);
        this.contactName = contactName;
        this.email = email;
        this.stage = Objects.requireNonNull(stage);
        this.productId = productId;
        this.ownerEmployeeId = ownerEmployeeId;
        this.createdDate = createdDate;
    }

    public String getId() { return id; }
    public String getCompanyName() { return companyName; }
    public String getContactName() { return contactName; }
    public String getEmail() { return email; }
    public SalesStage getStage() { return stage; }
    public String getProductId() { return productId; }
    public String getOwnerEmployeeId() { return ownerEmployeeId; }
    public LocalDate getCreatedDate() { return createdDate; }
}
