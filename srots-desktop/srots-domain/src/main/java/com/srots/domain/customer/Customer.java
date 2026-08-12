package com.srots.domain.customer;

import com.srots.domain.model.enums.ContractStatus;
import com.srots.domain.model.enums.CustomerStatus;
import java.util.Objects;

public final class Customer {
    private final String id;
    private final String companyName;
    private final String industry;
    private final String contactName;
    private final String email;
    private final CustomerStatus status;
    private final String productId;
    private final String accountOwnerEmployeeId;
    private final ContractStatus contractStatus;

    public Customer(String id, String companyName, String industry, String contactName, String email,
                    CustomerStatus status, String productId, String accountOwnerEmployeeId,
                    ContractStatus contractStatus) {
        this.id = Objects.requireNonNull(id);
        this.companyName = Objects.requireNonNull(companyName);
        this.industry = industry;
        this.contactName = contactName;
        this.email = email;
        this.status = Objects.requireNonNull(status);
        this.productId = productId;
        this.accountOwnerEmployeeId = accountOwnerEmployeeId;
        this.contractStatus = contractStatus;
    }

    public String getId() { return id; }
    public String getCompanyName() { return companyName; }
    public String getIndustry() { return industry; }
    public String getContactName() { return contactName; }
    public String getEmail() { return email; }
    public CustomerStatus getStatus() { return status; }
    public String getProductId() { return productId; }
    public String getAccountOwnerEmployeeId() { return accountOwnerEmployeeId; }
    public ContractStatus getContractStatus() { return contractStatus; }
}
