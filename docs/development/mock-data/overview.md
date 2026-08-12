# Mock Data Overview

SROTS uses mock infrastructure **only as a temporary implementation of permanent repository contracts**.

```text
View → ViewModel → UseCase → Repository Interface → Mock Repository → Mock State Store
```

Later the same interfaces bind to SQLite / REST without rewriting JavaFX screens.

- Dataset version: `1.0`
- Default mode: `MOCK` (development profile)
- Reference date: `2026-08-01` (deterministic)
- Products: **SROTS** + **COMPTY**
