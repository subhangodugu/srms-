# SROTS COMPTY Integration Architecture Specification

## 1. COMPTY Relationship Model
COMPTY is an independent flagship product managed by SROTS.
- COMPTY's application code, client UI, and runtime servers remain completely decoupled from SROTS.
- SROTS serves as COMPTY's **control plane** (managing requirements, roadmaps, engineering team assignments, versioning, release gates, deployments, customer accounts, and analytics).

## 2. Integration Interface Contracts

```text
SROTS CONTROL PLANE
  │
  ├── REST API / Webhooks ──► COMPTY Telemetry Collector (Health & Usage)
  ├── Customer Portal API ──► COMPTY License Manager (Entitlements)
  └── CI/CD Deploy Hooks  ──► COMPTY Staging/Prod Environments (Releases)
```
