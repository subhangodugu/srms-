package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.AssetDTO;
import com.sorts.srms.backend.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('ASSET_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AssetDTO>> getAssetsByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(assetService.getAssetsByCompany(companyId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ASSET_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AssetDTO> createAsset(@Valid @RequestBody AssetDTO dto) {
        AssetDTO created = assetService.createAsset(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{assetId}/assign")
    @PreAuthorize("hasAuthority('ASSET_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AssetDTO> assignAsset(
            @PathVariable String assetId,
            @RequestParam(required = false) String employeeId) {
        return ResponseEntity.ok(assetService.assignAsset(assetId, employeeId));
    }
}
