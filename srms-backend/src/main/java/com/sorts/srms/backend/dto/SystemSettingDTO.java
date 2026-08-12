package com.sorts.srms.backend.dto;

import java.time.LocalDateTime;

public class SystemSettingDTO {

    private String id;
    private String settingKey;
    private String settingValue;
    private String category;
    private String description;
    private boolean encrypted;
    private LocalDateTime updatedAt;

    public SystemSettingDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
