# SROTS Navigation Architecture Specification

## 1. Multi-Tier Desktop Navigation Tree
SROTS Desktop features a collapsible, role-aware sidebar navigation system:

```text
SROTS NAV TREE
├── Overview (Executive Dashboard)
├── My Workspace (Personal Tasks, Assigned Projects, Approvals, Profile)
├── Company
│   ├── Employees
│   ├── Teams
│   ├── Departments
│   └── Organization
├── Work
│   ├── Projects
│   ├── Tasks
│   ├── Issues
│   └── Approvals
├── Products
│   ├── All Products
│   ├── SROTS Platform
│   └── COMPTY Product
├── Engineering
│   ├── UI Engineering
│   ├── Backend Engineering
│   ├── Database Engineering
│   ├── AI/ML Engineering
│   ├── QA Engineering
│   ├── DevOps Engineering
│   └── Security Engineering
├── Releases
│   ├── Versions
│   ├── Release Board & Gates
│   ├── Deployments
│   └── History & Changelogs
├── Sales & Customers
│   ├── Leads
│   ├── Accounts & Customers
│   ├── Opportunities
│   └── Deals
├── Support (Helpdesk, Tickets, SLAs)
├── Knowledge (Docs, SOPs, KB Articles)
├── Analytics (Telemetry, Resource Utilization, BI Reports)
└── Governance & Settings (Audit Logs, RBAC Roles, System Config)
```

## 2. Navigation State Engine
- **View Switcher**: Centrally managed by `NavigationManager`. Dynamically loads and caches views inside the `MainLayoutView` content pane.
- **Deep Linking / Breadcrumbs**: Maintains a navigation stack allowing back/forward navigation and contextual breadcrumb trails (e.g., `Products > COMPTY > Releases > v1.9.0 > Gates`).
- **Role-Aware Filtering**: Navigation items check current user permissions; items without access are automatically hidden from the navigation tree.
