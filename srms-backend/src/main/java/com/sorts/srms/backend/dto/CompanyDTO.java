package com.sorts.srms.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CompanyDTO {

    private String id;

    @NotBlank(message = "Company Code is required")
    private String code;

    @NotBlank(message = "Company Name is required")
    private String name;

    private String taxId;
    private String email;
    private String phone;
    private String address;
    private String status;

    public CompanyDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
