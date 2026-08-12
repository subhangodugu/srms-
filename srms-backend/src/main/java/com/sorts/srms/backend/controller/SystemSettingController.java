package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.SystemSettingDTO;
import com.sorts.srms.backend.service.SystemSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings")
public class SystemSettingController {

    private final SystemSettingService settingService;

    public SystemSettingController(SystemSettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SETTINGS_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<SystemSettingDTO>> getAllSettings() {
        return ResponseEntity.ok(settingService.getAllSettings());
    }

    @PutMapping("/{key}")
    @PreAuthorize("hasAuthority('SETTINGS_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SystemSettingDTO> updateSetting(@PathVariable String key, @RequestParam String value) {
        return ResponseEntity.ok(settingService.updateSetting(key, value));
    }
}
