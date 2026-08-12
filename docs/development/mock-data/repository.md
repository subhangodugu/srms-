# Mock Repositories

Each domain repository has a `Mock*` implementation with:

- `findById` / `findAll`
- `search`
- `findPage` (filter + sort + pagination)
- `save` / `deleteById`

Repositories share `MockRepositoryBehavior` for latency and failure injection.

Contracts live in `com.srots.domain.repository` and must match future SQLite/REST adapters.
