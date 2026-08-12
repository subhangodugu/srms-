# Search Provider Architecture

```text
GlobalSearchService
  ├── EmployeeSearchProvider
  ├── ProjectSearchProvider
  └── …
```

Contract: `SearchProvider`

- `id()`
- `supportedTypes()`
- `search(SearchQuery, SearchAccessContext)`

Development uses `MockSearchProviders`. Production must inject real providers — never leave mock wiring active accidentally.
