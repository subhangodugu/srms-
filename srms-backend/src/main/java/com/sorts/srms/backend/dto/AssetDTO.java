package com.sorts.srms.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AssetDTO {

    private String id;

    @NotBlank(message = "Company ID is required")
    private String companyId;

    @NotBlank(message = "Asset Tag is required")
    private String assetTag;

    @NotBlank(message = "Asset Name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    private String serialNumber;
    private String assignedToEmployeeId;
    private String assignedToEmployeeName;
    private LocalDate purchaseDate;
    private BigDecimal purchaseCost;
    private String status;

    public AssetDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getAssignedToEmployeeId() { return assignedToEmployeeId; }
    public void setAssignedToEmployeeId(String assignedToEmployeeId) { this.assignedToEmployeeId = assignedToEmployeeId; }
    public String getAssignedToEmployeeName() { return assignedToEmployeeName; }
    public void setAssignedToEmployeeName(String assignedToEmployeeName) { this.assignedToEmployeeName = assignedToEmployeeName; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public BigDecimal getPurchaseCost() { return purchaseCost; }
    public void setPurchaseCost(BigDecimal purchaseCost) { this.purchaseCost = purchaseCost; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
