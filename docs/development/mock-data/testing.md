# Testing Mock Data

- Unit tests: `srots-infrastructure` → `MockInfrastructureTest`
- Deterministic: fixed reference date, no random UUIDs, no network
- Cover: seed counts, search/page/CRUD, derived dashboard metrics, empty/error/offline, reset, production guard
- UI / TestFX tests should inject repository interfaces (via use cases), never hard-code mock tables in views
