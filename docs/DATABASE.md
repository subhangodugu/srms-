# SRMS Database Architecture & Flyway Schema

SRMS uses normalized PostgreSQL 16 database design managed via Flyway versioned migrations.

## Flyway Migration Scripts
- `V1__initial_schema.sql`: Normalized tables (`companies`, `departments`, `roles`, `permissions`, `role_permissions`, `users`, `employees`, `projects`, `project_tasks`, `service_tickets`, `workflows`, `workflow_steps`, `assets`, `knowledge_articles`, `audit_logs`, `notifications`, `system_settings`) with foreign keys, unique constraints, and audit columns (`created_at`, `updated_at`, `created_by`, `status`).
- `V2__seed_enterprise_data.sql`: Enterprise baseline roles, permissions, super admin user (`admin@sorts.com`), sample companies, employees, projects, tickets, assets, articles, and settings.

## Entity Audit Standard
All primary entities inherit audit metadata:
- `id`: UUID 36-character string.
- `status`: String state (`ACTIVE`, `INACTIVE`, `SUSPENDED`, `ARCHIVED`).
- `created_at`: Mandatory UTC timestamp.
- `updated_at`: Automatically updated via JPA `@PreUpdate`.
