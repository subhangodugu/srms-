# State Management

## Prefer explicit UI state

Use enums such as:

```text
IDLE | LOADING | SUCCESS | EMPTY | ERROR | OFFLINE
```

(See also `com.srots.shared.query.LoadState`.)

Avoid contradictory boolean clusters (`isLoading && isError && hasData`) when one state can represent the situation.

## Form state

```text
INITIAL | EDITING | VALID | INVALID | SUBMITTING | SUCCESS | ERROR
```

Disable / guard submit while submitting to prevent duplicates.

## State ownership

| Level | Examples |
|-------|----------|
| **Component** | expanded, focused, selected, local loading |
| **ViewModel** | filter, search, pagination, selected entity, load/error |
| **Application** | current user, session, navigation, notifications, connection |

Do not dump everything into one global state object.

## ViewModel rules

- Own presentation state (search, selection, lists, load status)
- Call use cases — never infrastructure implementations
- Prefer JavaFX properties + binding
- One clear responsibility — no god ViewModels that own the whole app

## Data-driven screens

Account for meaningful states: INITIAL / LOADING / SUCCESS / EMPTY / ERROR / OFFLINE.
Not every screen needs every visual, but the ViewModel must handle them.

## Refresh

```text
Repository update → Observable / use-case result → Affected ViewModel → Affected UI
```

Do not rebuild the entire application for a single table change.

## Navigation state

Navigation state holds routes, parameters, history flags — never business entities or scene graphs.
