# SROTS Desktop — Final Master Architecture Specification

## 1. Complete SROTS Product Architecture
**SROTS (SORTS Resource & Operations Control System)** is the enterprise control plane for internal operations, product lifecycle governance, workforce allocation, and engineering orchestration. 

It is designed strictly as a **native cross-platform desktop application** built with **Java 21 LTS, JavaFX 21, FXML, JavaFX CSS, Maven, MVVM, and Clean Architecture**, supporting Windows, Linux, and macOS.

---

## 2. Desktop Application Architecture
SROTS Desktop follows a strictly decoupled layered architecture:

```text
┌─────────────────────────────────────────────────────────────┐
│                 PRESENTATION LAYER (JavaFX)                 │
│    FXML Views ──► ViewModels (Bindings / Commands)          │
└──────────────────────────────┬──────────────────────────────┘
                               │ (Inward Call)
┌──────────────────────────────▼──────────────────────────────┐
│                      APPLICATION LAYER                      │
│      Use Cases / Interactors ──► DTOs / Commands            │
└──────────────────────────────┬──────────────────────────────┘
                               │ (Depends on Interfaces)
┌──────────────────────────────▼──────────────────────────────┐
│                        DOMAIN LAYER                         │
│     Entities ──► Value Objects ──► Repository Interfaces    │
└──────────────────────────────▲──────────────────────────────┘
                               │ (Implements Contracts)
┌──────────────────────────────┴──────────────────────────────┐
│                    INFRASTRUCTURE LAYER                     │
│  SQLite (Local DB) │ Spring Boot REST API │ System OS APIs │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. Module Hierarchy
1. **Organization**: Companies, Business Units, Facilities.
2. **People**: Employees, Teams, Departments, Roles, Permissions.
3. **Work**: Projects, Tasks, Issues, Approvals.
4. **Products**: Internal SROTS Platform, COMPTY Flagship Product.
5. **Engineering**: UI, Backend, DB, AI/ML, QA, DevOps, Security verticals.
6. **Releases**: Versions, Release Candidates, Release Gates, Deployments.
7. **Business**: Sales, Leads, Customers, Opportunities, Support.
8. **Knowledge**: Wiki, Runbooks, Documentation.
9. **Analytics**: Utilization, Velocity, SLA metrics, BI reports.
10. **Governance**: Audit trails, Security policies, Compliance.

---

## 4. Portal & Role Hierarchy
- **Admin Portal**: For Company Administrators and Executives to manage Organization, Roles, Permissions, Products, Releases, Customers, Governance, and Settings.
- **Employee Portal**: Tailored workspace for workers to view My Tasks, My Projects, My Team, My Approvals, Notifications, Knowledge, and Profile.

---

## 5. Product Hierarchy
```text
SROTS COMPANY
├── Organization
├── People (Employees, Teams, Departments, Roles, Permissions)
├── Work (Projects, Tasks, Issues, Approvals)
├── Products (SROTS Control Plane, COMPTY Flagship Product)
├── Engineering (UI, Backend, DB, AI/ML, QA, DevOps, Security)
├── Releases (Versions, RCs, Release Gates, Deployments, Rollbacks)
├── Business (Sales, Leads, Customers, Opportunities, Support)
├── Knowledge
├── Analytics
└── Governance
```

---

## 6. COMPTY Relationship
COMPTY is an independent flagship product managed by SROTS. COMPTY's runtime app and client UI remain completely isolated from SROTS. SROTS acts as COMPTY's governance control plane for requirements, roadmaps, engineering team assignments, versioning, release gates, deployments, customer accounts, and operational analytics.

---

## 7. Engineering Team Relationship
Every product features 7 discipline teams:
`COMPTY` $\rightarrow$ `UI Team`, `Backend Team`, `Database Team`, `AI/ML Team`, `QA Team`, `DevOps Team`, `Security Team`.
Each team manages members, responsibilities, tasks, issues, milestones, activity, and signoff gates.

---

## 8. Product Lifecycle Pipeline
```text
IDEA ──► REQUIREMENT ──► PLANNING ──► DEVELOPMENT ──► CODE REVIEW ──► BUILD ──► QA ──► SECURITY REVIEW ──► STAGING ──► RELEASE APPROVAL ──► PRODUCTION ──► MONITORING ──► FEEDBACK ──► NEXT VERSION
```

---

## 9. Release Lifecycle & Block Gates
Releases follow SemVer (`vX.Y.Z`). A release candidate evaluates approval gates across all 7 discipline verticals. If any mandatory gate (e.g., Security, QA, Docs) is incomplete, the Release Status automatically evaluates to **BLOCKED** and prevents production deployment.

---

## 10. Data Architecture
- **Local Desktop Layer**: SQLite via JDBC for offline caching, local drafts, and fast offline query responsiveness.
- **Central Enterprise Layer**: PostgreSQL 16 via Spring Boot REST API for company-wide single source of truth.

---

## 11. Future Backend Architecture
- **Technology**: Spring Boot 3.x, Spring Security, Flyway, PostgreSQL, Redis, MinIO/S3.
- **Scope**: Authentication, Authorization, Organization Data, Project Tracking, Audit Logging, and Sync Endpoints.

---

## 12. Future Synchronization Architecture
Offline-first synchronization engine using an SQLite local mutation queue. When network connectivity is re-established, pending mutations are replayed to the central Spring Boot REST API using field-level merge or server-authoritative conflict resolution.

---

## 13. Security Architecture
- **Auth**: JWT with HMAC-SHA256.
- **Secure Keyring**: Tokens stored in native OS Keyring APIs (Windows DPAPI, macOS Keychain, Linux Secret Service).
- **RBAC**: Dual-layer authorization at UI controls and backend controllers.
- **Audit**: Immutable event logging for every security-sensitive action.

---

## 14. Frontend Architecture
- **Framework**: JavaFX 21 + FXML + JavaFX CSS.
- **Design Token System**: Glassmorphism dark enterprise palette (`#0F172A` Slate Dark, `#3B82F6` Accent Blue).
- **Custom Controls**: `SRMSBadge`, `SRMSCard`, `SRMSButton`.

---

## 15. Package Structure
```text
com.sorts.srms.desktop
├── presentation (view, viewmodel, component, theme)
├── application (usecase, dto, service)
├── domain (model, valueobject, repository)
└── infrastructure (persistence, client, security, system)
```

---

## 16. Development Roadmap
- **Prompt 01**: Define Complete Product & Desktop Architecture (DONE).
- **Prompt 02**: Create Java 21 + JavaFX + Maven Desktop Foundation (DONE).
- **Prompt 03**: Implement MVVM Navigation & Theme Design System (DONE).
- **Prompt 04**: Implement Core Subsystem Screens & Mock Repositories.
- **Prompt 05**: Define Complete SROTS Desktop Design System (DONE).
- **Prompt 06**: Define Complete SROTS JavaFX CSS Theme (DONE).
- **Prompt 07**: Define Complete Reusable SROTS UI Component System (DONE).
- **Prompt 08**: Define Complete SROTS Desktop Navigation Architecture (DONE).
- **Prompt 09**: Define Complete Mock-Data Architecture (DONE).
- **Prompt 10**: Establish Complete SROTS Frontend Coding Standards (DONE).
- **Prompt 11**: Create SROTS Desktop Application Launcher (DONE).
- **Prompt 12**: Create SROTS Desktop Splash Screen (DONE).
- **Prompt 13**: Create the SROTS Desktop Main Window (DONE).
- **Prompt 14**: Create the SROTS Desktop Application Sidebar (DONE).
- **Prompt 15**: Create the SROTS Desktop Top Navigation / Header (DONE).
- **Prompt 16**: Create the SROTS Desktop Status Bar (DONE).
- **Prompt 17**: Create the SROTS Desktop User / Profile Menu (DONE).
- **Prompt 18**: Create the SROTS Desktop Notification Panel (DONE).
- **Prompt 19**: Create the SROTS Desktop Global Search (DONE).
- **Prompt 20**: Create the SROTS desktop command palette and global keyboard shortcut architecture (next), keeping it separate from Global Search while sharing Ctrl/Cmd + K.

---

## 17. Architecture Decisions & Rationale (ADRs)
1. **JavaFX Native Desktop over Browser**: Low latency, native windowing, offline capabilities, secure OS storage.
2. **Inward Clean Architecture**: Decouples presentation views from database engines and network clients.
3. **Decoupled COMPTY Control Plane**: Protects runtime product autonomy while centralizing governance.

---

## 18. Prohibited Technologies Matrix (Desktop Frontend)

> [!CAUTION]
> The following web technologies are **STRICTLY PROHIBITED** for the SROTS desktop frontend:
> - **React / Next.js / Angular / Vue**
> - **HTML5 / Browser DOM Routing**
> - **Electron / NW.js / Tauri**
> - **SaaS Web Portals / Web Browsers**

---

## 19. Architectural Risks & Trade-Offs
- **Trade-Off**: Offline SQLite sync complexity vs uninterrupted desktop user productivity.
- **Mitigation**: Standardized mutation queue with explicit conflict resolution strategies.

---

## 20. Recommended Implementation Order
1. Establish multi-module Maven structure (`srots-desktop` Clean Architecture modules, optional `srms-backend`).
2. Implement core JavaFX application stage launcher and dark glassmorphic CSS tokens.
3. Implement `NavigationManager` and `MainLayoutView` container.
4. Implement ViewModels and mock repositories for core enterprise modules.
