# SRMS Testing Strategy & Verification Suite

SRMS includes automated unit, integration, and UI verification layers.

## Test Layers

1. **Unit Testing**:
   - `AuthenticationServiceTest`: Tests credential validation, BCrypt password matching, and JWT issuance.
   - `EmployeeServiceTest`: Tests employee provisioning business rules and duplicate code validation.
   - `ProjectServiceTest`: Tests project creation and task mapping logic.

2. **Database Integration Testing**:
   - Tests JPA entities and Flyway migrations (`V1__initial_schema.sql`, `V2__seed_enterprise_data.sql`) against H2 database.

3. **Desktop Verification**:
   - Verifies JavaFX thread isolation, async task execution (`AsyncTaskExecutor`), REST client authentication, and view rendering across all 14 enterprise modules.

## Running Tests
```bash
mvn test
```
