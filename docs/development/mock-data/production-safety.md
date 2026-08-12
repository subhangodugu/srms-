# Production Safety

Mock mode must not silently ship.

1. Maven profiles: `development` (default), `test`, `production`
2. System properties: `-Dsrots.env=production` and `-Dsrots.data.mode=REMOTE|LOCAL`
3. `ProductionMockGuard` fails fast if `env=production` and `DataMode.MOCK`
4. `AppContainer` also refuses production + MOCK at startup
5. Mock auth / user switching / diagnostics are development-only

Never place real employee data, passwords, JWTs, or API keys in mock datasets.
