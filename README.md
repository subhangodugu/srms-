# SRMS — SORTS Resource Management System

SRMS is a multi-tenant enterprise desktop application built with JavaFX 21, Spring Boot 3, and PostgreSQL 16 database.

## System Architecture Overview

- **Presentation Layer**: JavaFX 21 Enterprise UI (Dark slate theme, Glassmorphism, Custom controls, State Context, Keyboard Accessibility).
- **Backend Service**: Spring Boot 3 REST API (Spring Security, JWT Session Authentication, BCrypt Password Security, Spring Data JPA, Flyway Schema Migrations, Actuator Health).
- **Database Persistence**: PostgreSQL 16 (Production driver with Flyway schema migration scripts `V1__initial_schema.sql` and `V2__seed_enterprise_data.sql`), embedded H2 profile for offline local development and automated testing.
- **Security & RBAC**: Granular Role-Based Access Control enforcing 9 system roles (`SUPER_ADMIN`, `COMPANY_ADMIN`, `HR_ADMIN`, `MANAGER`, `PROJECT_MANAGER`, `EMPLOYEE`, `SUPPORT_AGENT`, `AUDITOR`, `READ_ONLY`) across backend REST controllers and JavaFX UI navigation menus.
- **Core Modules**: Companies, Departments, Employees, Roles & RBAC, Projects, Service Desk, Workflows, Assets, Knowledge Base, AI Predictive Insights, Analytics, Reports (PDF/CSV), Notifications, System Audit Logs, Settings.

## Building & Running

### Prerequisites
- JDK 21+
- Maven 3.9+
- PostgreSQL 16 (Optional; H2 database runs automatically by default)

### Build Project
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Run Backend API Service
```bash
mvn spring-boot:run -pl srms-backend
```

### Run JavaFX Desktop Client
```bash
mvn -f srots-desktop/pom.xml -pl srots-app org.openjfx:javafx-maven-plugin:run
```

### Default Credentials
- **Super Administrator**: `admin@sorts.com` / `Admin@123456`
- **Project Manager**: `sarah.j@sorts.com` / `Admin@123456`
- **Standard Employee**: `david.c@sorts.com` / `Admin@123456`
