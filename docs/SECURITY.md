# SRMS Security Specification

SRMS enforces enterprise-grade defense-in-depth security controls across desktop and backend boundaries.

## Security Principles
1. **Zero Trust API**: Every API request (excluding `/api/v1/auth/login` and `/actuator/health`) requires a valid JWT Bearer token signed via HMAC-SHA256.
2. **Password Security**: Passwords are hashed using BCrypt (`strength=10`). Plaintext passwords are never logged or stored.
3. **Backend Authorization**: Spring Security `@PreAuthorize` annotations enforce authority checks at controller endpoints. Client-side button disabling is supplemented by backend enforcement.
4. **Audit Trail**: Sensitive actions (logins, failed login attempts, company creation, employee provisioning, role modifications, report exports) log immutable records to the `audit_logs` database table.
5. **No Secrets in Code**: JWT secret keys and database passwords are pulled from environment variables or secure configuration profiles (`application.yml` / `application-prod.yml`).
