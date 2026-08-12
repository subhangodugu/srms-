# SROTS Dependency Rules Matrix

## Enforced Module Dependency Graph

```text
srots-domain              <── NO DEPENDENCIES (Pure Java)
      ▲
      │
srots-application         <── depends on srots-domain
      ▲
      ├─── srots-infrastructure <── depends on srots-application + srots-domain
      │
      └─── srots-presentation   <── depends on srots-application + srots-domain

srots-app                 <── depends on ALL 4 modules (Bootstrap & Wiring)
```

## Strictly Prohibited Dependencies
- `Domain -> JavaFX`
- `Domain -> Spring / Hibernate / JPA`
- `Domain -> SQLite / PostgreSQL / REST API`
- `Presentation -> Infrastructure` (Views/ViewModels MUST NOT depend directly on SQLite/REST repositories)
- `View -> Database / REST API`
- `Controller -> SQL / REST Client`
