# SRMS REST API Specification

Base Endpoint: `/api/v1`

## Endpoints Summary

### Authentication (`/api/v1/auth`)
- `POST /login`: Authenticate credentials, returns JWT bearer token and permission matrix.

### Company Management (`/api/v1/companies`)
- `GET /`: List all companies.
- `GET /{id}`: Get company details by ID.
- `POST /`: Create company tenant.
- `PUT /{id}`: Update company details.

### Employee Directory (`/api/v1/employees`)
- `GET /company/{companyId}`: List employees for company.
- `POST /`: Provision employee profile & user account.

### Role-Based Access Control (`/api/v1/roles`)
- `GET /`: List system roles.
- `GET /permissions`: List all granular system permissions.
- `PUT /{roleId}/permissions`: Update permissions assigned to a role.

### Projects & Workstreams (`/api/v1/projects`)
- `GET /company/{companyId}`: List projects.
- `POST /`: Create new project.
- `GET /{projectId}/tasks`: List project tasks.
- `POST /tasks`: Add task to project.

### Service Desk (`/api/v1/tickets`)
- `GET /company/{companyId}`: List support tickets.
- `POST /`: Submit new ticket.
- `PUT /{id}/status`: Update ticket status & assignee.

### Workflows (`/api/v1/workflows`)
- `GET /company/{companyId}`: List workflows.
- `POST /{workflowId}/steps/{stepId}/approve`: Approve workflow step.

### Asset Inventory (`/api/v1/assets`)
- `GET /company/{companyId}`: List assets.
- `POST /`: Register new asset.
- `PUT /{assetId}/assign`: Assign asset to employee.

### Knowledge Base (`/api/v1/knowledge`)
- `GET /`: Search knowledge articles.
- `POST /`: Publish article.

### AI Insights (`/api/v1/ai`)
- `GET /insights/company/{companyId}`: Get predictive AI recommendations and risk scores.

### Analytics & Reports (`/api/v1/analytics`, `/api/v1/reports`)
- `GET /analytics/company/{companyId}`: Fetch real database metric calculations.
- `POST /reports/generate`: Generate CSV / PDF enterprise report files.

### Audit Logs & Notifications (`/api/v1/audit-logs`, `/api/v1/notifications`)
- `GET /audit-logs`: Fetch audit records.
- `GET /notifications`: Fetch user notifications.
