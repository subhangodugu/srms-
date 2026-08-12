-- =============================================================================
-- SRMS - SORTS Resource Management System
-- Database Seed Data - V2 Enterprise Seed Data
-- =============================================================================

-- 1. SEED ROLES
INSERT INTO roles (id, code, name, description, is_system_role) VALUES
('r-super-admin', 'SUPER_ADMIN', 'Super Administrator', 'Full system control across all organizations', true),
('r-company-admin', 'COMPANY_ADMIN', 'Company Administrator', 'Full control within the assigned company tenant', true),
('r-hr-admin', 'HR_ADMIN', 'HR Administrator', 'Manages employee directory, departments, and personnel files', true),
('r-manager', 'MANAGER', 'Department Manager', 'Manages team members, department tasks, and approvals', true),
('r-project-manager', 'PROJECT_MANAGER', 'Project Manager', 'Manages projects, tasks, timelines, and budgets', true),
('r-employee', 'EMPLOYEE', 'Standard Employee', 'Access to self-service, assigned tasks, tickets, and knowledge base', true),
('r-support-agent', 'SUPPORT_AGENT', 'Support Agent', 'Service desk agent handling incoming customer/employee support tickets', true),
('r-auditor', 'AUDITOR', 'System Auditor', 'Read-only access to audit logs, compliance reports, and analytics', true),
('r-read-only', 'READ_ONLY', 'Read-Only Viewer', 'Strict read-only access across assigned modules', true);

-- 2. SEED PERMISSIONS
INSERT INTO permissions (id, code, category, description) VALUES
('p-comp-view', 'COMPANY_VIEW', 'ORGANIZATION', 'View company information and directory'),
('p-comp-create', 'COMPANY_CREATE', 'ORGANIZATION', 'Create new company tenants'),
('p-comp-update', 'COMPANY_UPDATE', 'ORGANIZATION', 'Update company information'),
('p-comp-delete', 'COMPANY_DELETE', 'ORGANIZATION', 'Delete or deactivate companies'),
('p-dept-view', 'DEPT_VIEW', 'ORGANIZATION', 'View department hierarchy'),
('p-dept-manage', 'DEPT_MANAGE', 'ORGANIZATION', 'Create and modify departments'),
('p-emp-view', 'EMPLOYEE_VIEW', 'EMPLOYEE', 'View employee directory'),
('p-emp-create', 'EMPLOYEE_CREATE', 'EMPLOYEE', 'Create employee profiles'),
('p-emp-update', 'EMPLOYEE_UPDATE', 'EMPLOYEE', 'Update employee profiles'),
('p-emp-delete', 'EMPLOYEE_DELETE', 'EMPLOYEE', 'Deactivate employee accounts'),
('p-role-manage', 'ROLE_MANAGE', 'SECURITY', 'Manage roles and assign permissions'),
('p-proj-view', 'PROJECT_VIEW', 'PROJECT', 'View projects and task lists'),
('p-proj-create', 'PROJECT_CREATE', 'PROJECT', 'Create new projects'),
('p-proj-update', 'PROJECT_UPDATE', 'PROJECT', 'Modify projects and tasks'),
('p-proj-delete', 'PROJECT_DELETE', 'PROJECT', 'Delete projects'),
('p-tkt-view', 'TICKET_VIEW', 'SERVICEDESK', 'View service desk tickets'),
('p-tkt-create', 'TICKET_CREATE', 'SERVICEDESK', 'Create support tickets'),
('p-tkt-update', 'TICKET_UPDATE', 'SERVICEDESK', 'Update and assign tickets'),
('p-tkt-resolve', 'TICKET_RESOLVE', 'SERVICEDESK', 'Resolve and close support tickets'),
('p-wf-view', 'WORKFLOW_VIEW', 'WORKFLOW', 'View workflows and approvals'),
('p-wf-manage', 'WORKFLOW_MANAGE', 'WORKFLOW', 'Configure workflow steps'),
('p-wf-approve', 'WORKFLOW_APPROVE', 'WORKFLOW', 'Execute workflow approval actions'),
('p-asset-view', 'ASSET_VIEW', 'ASSET', 'View asset inventory'),
('p-asset-manage', 'ASSET_MANAGE', 'ASSET', 'Add, modify, and assign assets'),
('p-kb-view', 'KB_VIEW', 'KNOWLEDGE', 'View knowledge base articles'),
('p-kb-manage', 'KB_MANAGE', 'KNOWLEDGE', 'Publish and manage knowledge base articles'),
('p-ai-view', 'AI_VIEW', 'AI', 'Access AI insights and recommendations'),
('p-analytics-view', 'ANALYTICS_VIEW', 'ANALYTICS', 'Access enterprise analytics and metrics'),
('p-reports-gen', 'REPORTS_GENERATE', 'REPORTS', 'Generate and export system reports'),
('p-audit-view', 'AUDIT_VIEW', 'SECURITY', 'View system audit logs'),
('p-settings-manage', 'SETTINGS_MANAGE', 'ADMIN', 'Modify system settings and configurations');

-- 3. SEED ROLE_PERMISSIONS FOR SUPER_ADMIN (All permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT 'r-super-admin', id FROM permissions;

-- SEED ROLE_PERMISSIONS FOR COMPANY_ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT 'r-company-admin', id FROM permissions WHERE code NOT IN ('COMPANY_CREATE', 'COMPANY_DELETE');

-- SEED ROLE_PERMISSIONS FOR PROJECT_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT 'r-project-manager', id FROM permissions WHERE category IN ('ORGANIZATION', 'PROJECT', 'SERVICEDESK', 'KNOWLEDGE', 'REPORTS', 'AI');

-- SEED ROLE_PERMISSIONS FOR EMPLOYEE
INSERT INTO role_permissions (role_id, permission_id)
SELECT 'r-employee', id FROM permissions WHERE code IN ('COMPANY_VIEW', 'DEPT_VIEW', 'EMPLOYEE_VIEW', 'PROJECT_VIEW', 'TICKET_VIEW', 'TICKET_CREATE', 'WORKFLOW_VIEW', 'KB_VIEW');

-- 4. SEED COMPANY (Primary Tenant)
INSERT INTO companies (id, code, name, tax_id, email, phone, address, status, created_by) VALUES
('c-sorts-global', 'SORTS-GLOBAL', 'SORTS Enterprise Solutions Corp', 'TAX-9988776655', 'admin@sorts.com', '+1-800-555-7678', '100 Tech Tower Plaza, Suite 500, San Francisco, CA', 'ACTIVE', 'SYSTEM_INIT'),
('c-acme-corp', 'ACME-CORP', 'ACME Industrial Innovations', 'TAX-1122334455', 'contact@acme.com', '+1-800-555-1234', '500 Innovation Way, Austin, TX', 'ACTIVE', 'SYSTEM_INIT');

-- 5. SEED DEPARTMENTS
INSERT INTO departments (id, company_id, code, name, parent_department_id, status) VALUES
('d-exec', 'c-sorts-global', 'EXEC', 'Executive Leadership', NULL, 'ACTIVE'),
('d-eng', 'c-sorts-global', 'ENG', 'Software Engineering', NULL, 'ACTIVE'),
('d-hr', 'c-sorts-global', 'HR', 'Human Resources', NULL, 'ACTIVE'),
('d-ops', 'c-sorts-global', 'OPS', 'IT Operations & Infrastructure', NULL, 'ACTIVE'),
('d-support', 'c-sorts-global', 'SUPPORT', 'Customer Support Desk', NULL, 'ACTIVE');

-- 6. SEED USERS (Password: Admin@123456 -> BCrypt hashed)
-- Hashed value for Admin@123456: $2a$10$zLvH09zS1DTPq15bc4GKt.ulCZYBypZwltWleq69/HlyyNNumGkA6
INSERT INTO users (id, company_id, department_id, username, email, password_hash, first_name, last_name, phone, role_id, status) VALUES
('u-admin', 'c-sorts-global', 'd-exec', 'admin', 'admin@sorts.com', '$2a$10$zLvH09zS1DTPq15bc4GKt.ulCZYBypZwltWleq69/HlyyNNumGkA6', 'Alexander', 'Vance', '+1-555-0100', 'r-super-admin', 'ACTIVE'),
('u-pm1', 'c-sorts-global', 'd-eng', 'sarah.jenkins', 'sarah.j@sorts.com', '$2a$10$zLvH09zS1DTPq15bc4GKt.ulCZYBypZwltWleq69/HlyyNNumGkA6', 'Sarah', 'Jenkins', '+1-555-0101', 'r-project-manager', 'ACTIVE'),
('u-emp1', 'c-sorts-global', 'd-eng', 'david.chen', 'david.c@sorts.com', '$2a$10$zLvH09zS1DTPq15bc4GKt.ulCZYBypZwltWleq69/HlyyNNumGkA6', 'David', 'Chen', '+1-555-0102', 'r-employee', 'ACTIVE'),
('u-agent1', 'c-sorts-global', 'd-support', 'rachel.adams', 'rachel.a@sorts.com', '$2a$10$zLvH09zS1DTPq15bc4GKt.ulCZYBypZwltWleq69/HlyyNNumGkA6', 'Rachel', 'Adams', '+1-555-0103', 'r-support-agent', 'ACTIVE');

-- 7. SEED EMPLOYEES
INSERT INTO employees (id, user_id, company_id, department_id, employee_code, job_title, manager_id, employment_type, hire_date, salary, status) VALUES
('e-admin', 'u-admin', 'c-sorts-global', 'd-exec', 'EMP-00001', 'Chief Technology Officer', NULL, 'FULL_TIME', '2022-01-15', 185000.00, 'ACTIVE'),
('e-pm1', 'u-pm1', 'c-sorts-global', 'd-eng', 'EMP-00002', 'Lead Project Manager', 'e-admin', 'FULL_TIME', '2022-03-01', 135000.00, 'ACTIVE'),
('e-emp1', 'u-emp1', 'c-sorts-global', 'd-eng', 'EMP-00003', 'Senior Full Stack Engineer', 'e-pm1', 'FULL_TIME', '2022-06-15', 120000.00, 'ACTIVE'),
('e-agent1', 'u-agent1', 'c-sorts-global', 'd-support', 'EMP-00004', 'Senior Support Specialist', 'e-admin', 'FULL_TIME', '2023-01-10', 85000.00, 'ACTIVE');

-- 8. SEED PROJECTS
INSERT INTO projects (id, company_id, code, name, description, manager_id, status, priority, budget, start_date, end_date) VALUES
('p-srms-core', 'c-sorts-global', 'PRJ-SRMS-01', 'SRMS Enterprise Platform Core Modernization', 'Next-generation resource management system upgrade with JavaFX UI & Spring Boot backend', 'e-pm1', 'IN_PROGRESS', 'CRITICAL', 450000.00, '2026-01-01', '2026-12-31'),
('p-cloud-migration', 'c-sorts-global', 'PRJ-CLOUD-02', 'Hybrid Infrastructure & Postgres Cluster Migration', 'High availability database cluster deployment and security harding', 'e-pm1', 'PLANNING', 'HIGH', 220000.00, '2026-03-15', '2026-09-30');

-- 9. SEED PROJECT TASKS
INSERT INTO project_tasks (id, project_id, title, description, assignee_id, priority, status, estimated_hours, logged_hours, due_date) VALUES
('t-task-101', 'p-srms-core', 'Design JavaFX Enterprise Design System Theme', 'Implement dark slate theme tokens, glassmorphism cards, and responsive sidebar navigation', 'e-emp1', 'HIGH', 'IN_PROGRESS', 40.00, 24.50, '2026-08-30'),
('t-task-102', 'p-srms-core', 'Implement Spring Security JWT Bearer Token Auth', 'Configure stateless security filter chain, BCrypt encoder, and role authority mapping', 'e-emp1', 'CRITICAL', 'DONE', 32.00, 32.00, '2026-08-15'),
('t-task-103', 'p-srms-core', 'Build Flyway V1 & V2 Database Schema & Seed Data', 'Write normalized PostgreSQL tables, indexes, constraints, and audit logging tables', 'e-emp1', 'HIGH', 'DONE', 24.00, 24.00, '2026-08-12');

-- 10. SEED SERVICE TICKETS
INSERT INTO service_tickets (id, company_id, ticket_number, title, description, requester_id, assignee_id, priority, status, category) VALUES
('tkt-001', 'c-sorts-global', 'TKT-2026-001', 'Developer Workstation VPN Gateway Auth Failure', 'Unable to connect to internal staging cluster via WireGuard VPN since morning update', 'e-emp1', 'e-agent1', 'HIGH', 'IN_PROGRESS', 'NETWORK'),
('tkt-002', 'c-sorts-global', 'TKT-2026-002', 'Request for PostgreSQL Read Replica DB Credentials', 'Need read-only credentials for analytical query reporting test environment', 'e-pm1', 'e-agent1', 'MEDIUM', 'OPEN', 'ACCESS_REQUEST');

-- 11. SEED WORKFLOWS & STEPS
INSERT INTO workflows (id, company_id, code, name, description, entity_type, status) VALUES
('wf-capex', 'c-sorts-global', 'WF-CAPEX-01', 'Capital Expenditure Approval Workflow', 'Multi-tier authorization workflow for capital expenditure equipment requests over $5,000', 'PURCHASE_REQUEST', 'ACTIVE');

INSERT INTO workflow_steps (id, workflow_id, step_order, step_name, approver_role_id, status) VALUES
('wfs-1', 'wf-capex', 1, 'Department Manager Initial Review', 'r-manager', 'APPROVED'),
('wfs-2', 'wf-capex', 2, 'Financial Controller Verification', 'r-company-admin', 'PENDING');

-- 12. SEED ASSETS
INSERT INTO assets (id, company_id, asset_tag, name, category, serial_number, assigned_to_employee_id, purchase_date, purchase_cost, status) VALUES
('ast-001', 'c-sorts-global', 'AST-MBP-9021', 'MacBook Pro 16" M3 Max 64GB', 'LAPTOP', 'C02GX987N123', 'e-emp1', '2024-02-10', 3899.00, 'ASSIGNED'),
('ast-002', 'c-sorts-global', 'AST-DELL-4011', 'Dell UltraSharp 34" Curved Monitor', 'MONITOR', 'CN-098231-DELL', 'e-emp1', '2024-02-15', 899.00, 'ASSIGNED'),
('ast-003', 'c-sorts-global', 'AST-SRV-001', 'Dell PowerEdge R750 Server', 'SERVER', 'SERV-2025-9988', NULL, '2025-01-20', 14500.00, 'IN_USE');

-- 13. SEED KNOWLEDGE ARTICLES
INSERT INTO knowledge_articles (id, author_id, title, category, tags, content, views, status) VALUES
('kb-001', 'e-admin', 'SRMS Architecture & Security Guidelines Baseline', 'ARCHITECTURE', 'security, architecture, rbac, desktop', 'SRMS is built on JavaFX 21 presentation layer and Spring Boot 3 backend with PostgreSQL database. RBAC permissions are evaluated at both backend controller level and UI layout level.', 142, 'PUBLISHED'),
('kb-002', 'e-agent1', 'VPN Access Troubleshooting & Certificate Setup', 'SUPPORT', 'vpn, network, security', 'Standard procedures for updating client SSL certificates and resetting WireGuard VPN credentials on company issued laptops.', 89, 'PUBLISHED');

-- 14. SEED AUDIT LOGS
INSERT INTO audit_logs (id, user_id, username, company_id, action, resource, resource_id, details, ip_address, status) VALUES
('aud-001', 'u-admin', 'admin', 'c-sorts-global', 'SYSTEM_INITIALIZATION', 'SYSTEM', 'BOOTSTRAP', 'Database initialized and Flyway enterprise seed data loaded successfully', '127.0.0.1', 'SUCCESS'),
('aud-002', 'u-admin', 'admin', 'c-sorts-global', 'USER_LOGIN', 'AUTH', 'u-admin', 'Super Administrator logged into desktop client', '127.0.0.1', 'SUCCESS');

-- 15. SEED SYSTEM SETTINGS
INSERT INTO system_settings (id, setting_key, setting_value, category, description, is_encrypted) VALUES
('set-001', 'app.name', 'SRMS Enterprise Resource Platform', 'GENERAL', 'Application Display Name', false),
('set-002', 'app.version', '1.0.0-RELEASE', 'GENERAL', 'Installed Application Version', false),
('set-003', 'auth.session.timeout.minutes', '60', 'SECURITY', 'User session timeout duration in minutes', false),
('set-004', 'ai.engine.mode', 'HYBRID_PREDICTIVE', 'AI', 'AI Insights Calculation Mode', false);
