# SROTS Data Architecture Specification

## 1. Dual-Storage Architecture
SROTS Desktop utilizes a hybrid data storage strategy:

```text
DESKTOP CLIENT
  ├── Local Storage: SQLite (Embedded JDBC / Local Caching / Drafts)
  └── Central Storage: Spring Boot REST API ──► PostgreSQL 16 (Production)
```

- **Local Storage (SQLite)**: Stores local user preferences, cached UI states, offline mutation queues, draft tasks/issues, and session metadata.
- **Central Storage (PostgreSQL)**: Enterprise central source of truth containing organization, employees, projects, tasks, releases, audit trails, and financial records. Managed via Flyway database migrations (`V1__initial_schema.sql`, `V2__seed_enterprise_data.sql`).

## 2. Repository Abstraction Pattern
ViewModels interact strictly with Repository interfaces (e.g., `ProjectRepository`). Infrastructure implementations decide whether to fulfill read requests from local SQLite cache or remote REST API based on connectivity.
