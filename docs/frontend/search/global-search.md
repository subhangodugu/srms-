# SROTS Global Search

## Role

Application-wide find + command entry from the TopBar.

```text
TopBar / Ctrl+K → SrotsGlobalSearch → ViewModel → GlobalSearchService → SearchProviders
                                                      ↘ CommandRegistry (via `>` mode)
```

## Packages

| Layer | Location |
|-------|----------|
| Application | `com.srots.application.search` |
| Mock providers | `com.srots.application.search.mock` |
| Presentation | `com.srots.presentation.search` |

## Behavior

| Input | Result |
|-------|--------|
| Ctrl/Cmd + K | Open overlay, focus field |
| Escape | Close |
| Type ≥ 2 chars | Debounced async search (200ms) |
| Prefix `>` | Command mode |
| Enter / double-click | Open selected result/command |

## Non-goals

No Stage, no direct DB/REST/WebSocket, no business logic in JavaFX cells.
