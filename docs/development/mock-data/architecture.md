# Mock Architecture

## Packages

`com.srots.infrastructure.mock`

- `configuration` — `DataMode`, latency, failure policy, production guard
- `state` — `MockStateStore`
- `seed` — `MockDataSeeder`
- `factory` — realistic entity factories
- `datasource` — store-facing mock sources
- `repository` — domain repository implementations
- `scenario` — NORMAL / EMPTY / ERROR / OFFLINE / LOADING / LARGE
- `auth` — development-only mock users (no passwords)
- `event` — `MockEventPublisher` (WebSocket stand-in)
- `diagnostics` — development diagnostics snapshot

## Composition

`MockInfrastructure` wires configuration → seeder → repositories.

`AppContainer` selects data mode via `-Dsrots.data.mode` and `-Dsrots.env`.

## Rules

- Domain models only (no UI row DTOs in mock)
- No SQL / REST / JWT inside mock packages
- No mock branches in View / ViewModel / Domain
