# SROTS Product Architecture Specification

## 1. Executive Summary & Vision
SROTS (SORTS Resource & Operations Control System) is the primary internal enterprise operating and control platform. It serves as the company's **central control plane**, bridging corporate operations (organization, employees, teams, departments, business operations) directly with product development, engineering disciplines, and complete product lifecycles.

SROTS is designed exclusively as a **cross-platform native desktop application** targeting Windows, macOS, and Linux. It is **NOT** a web application, SaaS frontend, browser portal, or Electron app.

## 2. Product Hierarchy Model
Every domain object, entity, and process within SROTS strictly adheres to the following unified enterprise product model:

```text
SROTS COMPANY CONTROL PLANE
│
├── Organization
│   ├── Legal Entities & Tenants
│   ├── Business Units
│   └── Facilities & Locations
│
├── People
│   ├── Employees (Profile, Allocation, Performance)
│   ├── Teams (Cross-functional & Discipline-specific)
│   ├── Departments (Exec, Eng, HR, Ops, Support, Sales)
│   ├── Roles (Super Admin, Executive, PM, Lead, Dev, QA, DevOps)
│   └── Permissions (Fine-grained domain capability matrices)
│
├── Work
│   ├── Projects (Milestones, Budgets, Timelines)
│   ├── Tasks (Assignments, Workloads, Status)
│   ├── Issues (Bugs, Vulnerabilities, Escalations)
│   └── Approvals (Release gates, Budget approvals, Sign-offs)
│
├── Products
│   ├── SROTS (Internal Control Plane Platform)
│   └── COMPTY (Independent Flagship Product managed by SROTS)
│
├── Engineering
│   ├── UI Discipline
│   ├── Backend Discipline
│   ├── Database Discipline
│   ├── AI/ML Discipline
│   ├── QA Discipline
│   ├── DevOps Discipline
│   └── Security Discipline
│
├── Releases
│   ├── Versions (SemVer vX.Y.Z)
│   ├── Release Candidates (RC builds)
│   ├── Release Gates (Quality & Security Checkpoints)
│   ├── Deployments (Target Environments)
│   └── Rollbacks (Emergency Restoration)
│
├── Business
│   ├── Sales (Pipeline, Deals)
│   ├── Leads (Prospect tracking)
│   ├── Customers (Accounts & Licenses)
│   ├── Opportunities (Upsell/Cross-sell)
│   └── Support (Incident Tickets, SLA tracking)
│
├── Knowledge (Documentation, SOPs, Wiki, Runbooks)
│
├── Analytics (Resource utilization, Velocity, SLA metrics, Financials)
│
└── Governance (Audit trails, Compliance checks, Policy enforcement)
```

## 3. Product vs Platform Management (COMPTY Relationship)
COMPTY is a flagship external software product whose complete lifecycle is managed by SROTS.
- **Isolation Rule**: COMPTY's source code, application UI, and client runtime remain completely independent.
- **SROTS Responsibility**: SROTS acts as the control plane that manages COMPTY's requirements, roadmaps, engineering team allocations (UI, Backend, DB, AI, QA, DevOps, Security), releases, versioning, release gates, deployments, customer accounts, and operational analytics.

## 4. Architectural Decisions & Rationale (ADR)
1. **Desktop Native Architecture over Browser App**: Enterprise control plane operations require high-performance local data caching, multi-window support, system tray notifications, offline resilience, and secure hardware-level credential protection.
2. **Clean Layered Architecture**: Clear isolation between Presentation (JavaFX/MVVM), Application (Use Cases/DTOs), Domain (Entities/Business Rules), and Infrastructure (SQLite/Spring Boot REST).
3. **Decoupled Control Plane**: Decouples governance and release orchestration from target product runtime engines.
