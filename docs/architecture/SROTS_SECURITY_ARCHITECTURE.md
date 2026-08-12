# SROTS Security Architecture Specification

## 1. Authentication & Token Management
- **Protocol**: JWT (JSON Web Tokens) with HMAC-SHA256 signing.
- **Token Expiration**: Access token expires in 24 hours.
- **Secure Local Storage**: Desktop client stores tokens securely using OS Keyring APIs (Windows DPAPI, macOS Keychain, Linux Secret Service). Plaintext token storage on disk is strictly prohibited.

## 2. Authorization & RBAC
- **Authoritative Backend**: All REST endpoints are guarded with Spring Security (`@PreAuthorize`).
- **UI RBAC**: Layout views and menu items adapt dynamically based on `UserSessionContext.getPermissions()`.

## 3. Security Audit Logging
- Every authentication attempt, password change, permission edit, release gate override, or sensitive data modification generates an immutable audit record in the `audit_logs` table (`user_id`, `company_id`, `action`, `resource`, `ip_address`, `status`, `timestamp`).
