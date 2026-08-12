package com.sorts.srms.backend;

import com.sorts.srms.backend.domain.model.Company;
import com.sorts.srms.backend.domain.model.Role;
import com.sorts.srms.backend.domain.model.User;
import com.sorts.srms.backend.dto.AuthRequestDTO;
import com.sorts.srms.backend.dto.AuthResponseDTO;
import com.sorts.srms.backend.exception.UnauthorizedException;
import com.sorts.srms.backend.repository.AuditLogRepository;
import com.sorts.srms.backend.repository.UserRepository;
import com.sorts.srms.backend.security.JwtTokenProvider;
import com.sorts.srms.backend.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuditLogRepository auditLogRepository;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtTokenProvider, auditLogRepository);
    }

    @Test
    void testLoginSuccess() {
        Company company = new Company();
        company.setId("c-test");
        company.setName("Test Corp");

        Role role = new Role();
        role.setCode("SUPER_ADMIN");
        role.setName("Super Admin");

        User user = new User();
        user.setId("u-test");
        user.setUsername("testadmin");
        user.setEmail("admin@test.com");
        user.setPasswordHash("hashed_password");
        user.setFirstName("Test");
        user.setLastName("Admin");
        user.setCompany(company);
        user.setRole(role);

        when(userRepository.findByUsername("testadmin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw_password", "hashed_password")).thenReturn(true);
        when(jwtTokenProvider.generateToken("testadmin", "SUPER_ADMIN", "c-test")).thenReturn("mocked_jwt_token");

        AuthResponseDTO response = authenticationService.login(new AuthRequestDTO("testadmin", "raw_password"), "127.0.0.1");

        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.getToken());
        assertEquals("testadmin", response.getUsername());
        assertEquals("SUPER_ADMIN", response.getRoleCode());
    }

    @Test
    void testLoginInvalidPassword() {
        User user = new User();
        user.setUsername("testadmin");
        user.setPasswordHash("hashed_password");

        when(userRepository.findByUsername("testadmin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "hashed_password")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () ->
                authenticationService.login(new AuthRequestDTO("testadmin", "wrong_password"), "127.0.0.1")
        );
    }
}
