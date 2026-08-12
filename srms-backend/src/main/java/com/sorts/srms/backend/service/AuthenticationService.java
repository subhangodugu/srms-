package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.model.AuditLog;
import com.sorts.srms.backend.domain.model.User;
import com.sorts.srms.backend.dto.AuthRequestDTO;
import com.sorts.srms.backend.dto.AuthResponseDTO;
import com.sorts.srms.backend.exception.UnauthorizedException;
import com.sorts.srms.backend.repository.AuditLogRepository;
import com.sorts.srms.backend.repository.UserRepository;
import com.sorts.srms.backend.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuditLogRepository auditLogRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuthResponseDTO login(AuthRequestDTO request, String ipAddress) {
        log.info("Login attempt for user: {}", request.getUsernameOrEmail());

        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(request.getUsernameOrEmail())
                        .orElseThrow(() -> new UnauthorizedException("Invalid username or password")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Password mismatch for user: {}", user.getUsername());
            String companyId = user.getCompany() != null ? user.getCompany().getId() : "";
            auditLogRepository.save(new AuditLog(
                    UUID.randomUUID().toString(), user.getId(), user.getUsername(), companyId,
                    "LOGIN_FAILED", "AUTH", user.getId(), "Invalid password attempt", ipAddress, "FAILED"
            ));
            throw new UnauthorizedException("Invalid username or password");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus().name())) {
            log.warn("Account suspended or inactive for user: {}", user.getUsername());
            throw new UnauthorizedException("Account is inactive or suspended");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String roleCode = user.getRole() != null ? user.getRole().getCode() : "READ_ONLY";
        String companyId = user.getCompany() != null ? user.getCompany().getId() : "";
        String token = jwtTokenProvider.generateToken(user.getUsername(), roleCode, companyId);

        var permissions = user.getRole() != null && user.getRole().getPermissions() != null ?
                user.getRole().getPermissions().stream().map(p -> p.getCode()).collect(Collectors.toSet()) :
                java.util.Collections.<String>emptySet();

        auditLogRepository.save(new AuditLog(
                UUID.randomUUID().toString(), user.getId(), user.getUsername(), companyId,
                "USER_LOGIN", "AUTH", user.getId(), "User logged in successfully", ipAddress, "SUCCESS"
        ));

        return new AuthResponseDTO(
                token, user.getId(), user.getUsername(), user.getEmail(),
                user.getFirstName(), user.getLastName(), companyId,
                user.getCompany() != null ? user.getCompany().getName() : "",
                roleCode, user.getRole() != null ? user.getRole().getName() : "",
                permissions
        );
    }
}
