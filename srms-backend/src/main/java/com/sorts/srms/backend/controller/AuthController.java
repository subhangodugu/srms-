package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.AuthRequestDTO;
import com.sorts.srms.backend.dto.AuthResponseDTO;
import com.sorts.srms.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request, HttpServletRequest servletRequest) {
        String ipAddress = servletRequest.getRemoteAddr();
        AuthResponseDTO response = authenticationService.login(request, ipAddress);
        return ResponseEntity.ok(response);
    }
}
