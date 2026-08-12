# Search Command Mode

Commands remain separate from entity search.

- Entry: query starts with `>`
- Source: `NavigationCommandCatalog` / command supplier
- Execution: command `Runnable` → usually `NavigationService`

A later prompt may expand the dedicated command palette while keeping this shared Ctrl/Cmd + K entry point.
