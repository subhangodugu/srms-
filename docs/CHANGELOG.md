# SRMS Changelog

## [1.0.1] - 2026-08-12

### Removed
- **Legacy `srms-desktop`**: Retired the old Spring-coupled JavaFX client. The product desktop is `srots-desktop` only.

## [1.0.0-RELEASE] - 2026-08-12

### Added
- **Multi-Module Project Architecture**: Initialized root POM, `srms-backend` (Spring Boot 3), and `srms-desktop` (JavaFX 21).
- **Presentation Layer**: Built JavaFX 21 enterprise desktop application with dark slate theme, glassmorphism card styling, responsive layout controls, and non-blocking background workers (`AsyncTaskExecutor`).
- **Backend Service Layer**: Built Spring Boot 3 REST API with Spring Security, JWT session authentication, BCrypt password hashing, Flyway database migrations, and 16 REST controllers (`/api/v1/*`).
- **Database Architecture**: Created normalized Flyway database schema (`V1__initial_schema.sql`) and baseline enterprise seed data (`V2__seed_enterprise_data.sql`) supporting PostgreSQL 16 and H2.
- **Enterprise Modules**: Implemented functional logic across 14 enterprise modules (Companies, Departments, Employees, RBAC Roles, Projects, Service Desk, Workflows, Assets, Knowledge Base, AI Insights, Analytics, Reports, Notifications, Audit Logs, Settings).
- **Security & RBAC**: Implemented 9 baseline system roles and granular permissions evaluated across backend REST endpoints and frontend navigation sidebar.
- **Build & Packaging**: Configured Maven multi-module compilation, JUnit 5 unit tests, PowerShell automation scripts (`build.ps1`, `run-backend.ps1`, `run-desktop.ps1`, `package-windows.ps1`), and `jpackage` Windows installer setup.
