# SROTS Role & Portal Architecture Specification

## 1. Enterprise Desktop Portals

SROTS Desktop supports role-tailored user portals to streamline user experience and protect company assets:

### A. Admin Portal
Target Users: Company Administrators, Executives, Department Directors.
- **Capabilities**: Full access to Organization, Employee Directory, Salary/Financial Analytics, Role & Permission Configuration, Release Gate Overrides, Audit Logs, System Settings, and Global Governance.

### B. Employee Portal
Target Users: Software Engineers, QA Engineers, Support Agents, Product Managers, Designers.
- **Capabilities**: Personalized "My Workspace" dashboard, My Tasks, My Projects, My Team Roster, My Approvals, Knowledge Base access, Submit Support Tickets, Personal Profile settings.

---

## 2. Role Hierarchy

```text
SUPER_ADMIN (Company System Owner)
├── EXECUTIVE (CTO, VP of Engineering, Operations Director)
│   ├── PRODUCT_MANAGER (Product Lead, Release Manager)
│   ├── TEAM_LEAD (Engineering Lead, QA Manager, DevOps Lead)
│   │   ├── ENGINEER (UI, Backend, DB, AI, DevOps Engineer)
│   │   ├── QA_ENGINEER (Quality Assurance Tester)
│   │   └── SUPPORT_AGENT (Helpdesk & Incident Management)
│   └── SALES_MANAGER (Account Executive, Business Development)
└── EMPLOYEE (Standard Corporate Worker)
```
