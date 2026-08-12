# SROTS Permission Architecture Specification

## 1. Dual-Layer Permission Evaluation
Authorization in SROTS is enforced at **two distinct levels**:
1. **Desktop Presentation Layer**: JavaFX UI controls evaluate permission flags (e.g., `UserSessionContext.hasPermission("RELEASE_APPROVE")`) to enable/disable buttons, show/hide menu items, and restrict view access.
2. **Backend Service Layer (Authoritative)**: Spring Security pre-authorizes REST API endpoints (`@PreAuthorize("hasAuthority('RELEASE_APPROVE')")`). Client-side UI hiding is NEVER relied upon as the sole security boundary.

## 2. Permission Matrix Strategy

```text
Domain        │ SuperAdmin │ Executive │ PM  │ Lead │ Dev │ QA  │ Support
──────────────┼────────────┼───────────┼─────┼──────┼─────┼─────┼────────
Org:Manage    │     ✓      │     ✓     │  ✗  │  ✗   │  ✗  │  ✗  │   ✗
Users:Manage  │     ✓      │     ✓     │  ✗  │  ✗   │  ✗  │  ✗  │   ✗
Project:Create│     ✓      │     ✓     │  ✓  │  ✗   │  ✗  │  ✗  │   ✗
Task:Assign   │     ✓      │     ✓     │  ✓  │  ✓   │  ✗  │  ✗  │   ✗
Code:Commit   │     ✓      │     ✗     │  ✗  │  ✓   │  ✓  │  ✗  │   ✗
QA:Signoff    │     ✓      │     ✗     │  ✗  │  ✓   │  ✗  │  ✓  │   ✗
Release:Gate  │     ✓      │     ✓     │  ✓  │  ✓   │  ✗  │  ✓  │   ✗
Audit:Read    │     ✓      │     ✓     │  ✗  │  ✗   │  ✗  │  ✗  │   ✗
```
