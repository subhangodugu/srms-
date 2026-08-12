# SROTS Module Architecture Specification

## 1. Module Hierarchy & Package Structure

The SROTS codebase is structured cleanly into logical subsystems reflecting the corporate control plane:

```text
com.sorts.srms.desktop
├── presentation
│   ├── viewmodel             # MVVM ViewModels
│   ├── view                  # JavaFX Views & FXML Controllers
│   ├── component             # Reusable UI Custom Controls (Badges, Cards, Tables)
│   └── theme                 # Design Tokens & CSS Managers
│
├── application
│   ├── usecase               # Interactors & Business Workflows
│   ├── dto                   # Data Transfer Objects
│   └── service               # Application Orchestrations
│
├── domain
│   ├── model                 # Core Enterprise Entities (Employee, Project, Release)
│   ├── valueobject           # Value Objects (SemVer, Email, Currency)
│   └── repository            # Repository Interfaces (Domain contracts)
│
└── infrastructure
    ├── persistence           # SQLite Local DB & JPA/JDBC Repositories
    ├── client                # Spring Boot REST & WebSocket Client
    ├── security              # Token Store & Encryption Services
    └── system                # Native Desktop OS Integrations (Tray, Notifications)
```

## 2. Core Enterprise Subsystems

1. **Organization Subsystem**: Company management, departments, locations, hierarchy.
2. **People Subsystem**: Employees, cross-functional teams, roles, and capability matrices.
3. **Work Subsystem**: Projects, milestones, task breakdowns, issues, and approval workflows.
4. **Products Subsystem**: Internal SROTS platform and COMPTY product lifecycle governance.
5. **Engineering Subsystem**: Discipline verticals (UI, Backend, Database, AI/ML, QA, DevOps, Security).
6. **Releases Subsystem**: Version management, release candidates, multi-discipline approval gates, deployments, and rollbacks.
7. **Business Subsystem**: Customer accounts, sales leads, opportunities, deals, and support incident tickets.
8. **Knowledge Subsystem**: Internal documentation, architecture standards, runbooks, and KB articles.
9. **Analytics Subsystem**: Performance metrics, resource utilization, velocity, financial budget tracking, SLA health.
10. **Governance Subsystem**: Security policy enforcement, immutable audit logging, compliance tracking.
