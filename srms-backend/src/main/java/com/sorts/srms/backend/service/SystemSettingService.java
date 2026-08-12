package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.model.SystemSetting;
import com.sorts.srms.backend.dto.SystemSettingDTO;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.SystemSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemSettingService {

    private final SystemSettingRepository settingRepository;

    public SystemSettingService(SystemSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Transactional(readOnly = true)
    public List<SystemSettingDTO> getAllSettings() {
        return settingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SystemSettingDTO updateSetting(String key, String value) {
        SystemSetting setting = settingRepository.findBySettingKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting key not found: " + key));

        setting.setSettingValue(value);
        return mapToDTO(settingRepository.save(setting));
    }

    private SystemSettingDTO mapToDTO(SystemSetting s) {
        SystemSettingDTO dto = new SystemSettingDTO();
        dto.setId(s.getId());
        dto.setSettingKey(s.getSettingKey());
        dto.setSettingValue(s.isEncrypted() ? "********" : s.getSettingValue());
        dto.setCategory(s.getCategory());
        dto.setDescription(s.getDescription());
        dto.setEncrypted(s.isEncrypted());
        dto.setUpdatedAt(s.getUpdatedAt());
        return dto;
    }
}
