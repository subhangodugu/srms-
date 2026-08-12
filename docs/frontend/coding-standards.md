# SROTS Frontend Coding Standards

Native **Java 21 + JavaFX** desktop application. Not web, React, Electron, or HTML.

## Primary flow

```text
JavaFX View → ViewModel → Use Case → Repository Interface → Infrastructure
```

## Non-negotiable rules

1. Native JavaFX only — no React/Next/HTML/Electron/Tailwind/Bootstrap.
2. MVVM must be maintained.
3. Clean Architecture boundaries must be maintained.
4. No database access from presentation.
5. No REST/WebSocket clients inside controllers or views.
6. No business logic in Views or Controllers.
7. ViewModels never access infrastructure implementations directly.
8. Navigation goes through `NavigationService`.
9. Reuse SROTS components (`Srots*`) — do not duplicate.
10. No inline CSS / raw colors for normal UI (`srots-*` style classes only).
11. No blocking work on the JavaFX Application Thread.
12. Mock data stays behind repository interfaces (Prompt 09).
13. Production must never silently use mock data.
14. Preserve Prompts 03–09 architecture unless there is a strong reason to change.
15. Prefer the simplest maintainable design — no speculative abstractions.

## Naming

- Classes: `EmployeeView`, `EmployeeViewModel`, `GetEmployeesUseCase`, `EmployeeRepository`
- Methods: `loadEmployees()`, `createEmployee()`
- Booleans: `isLoading()`, `canDelete()`
- Avoid abbreviations: `NavigationService` not `NavSvc`
- Enums for semantic states: `ReleaseStatus`, `NavigationRouteId`, `LoadState`
- Meaningful constants for domain/presentation thresholds; avoid magic numbers and strings

## Error handling

```text
Catch → Log technical details → Map to safe UI error → Show user message
```

Never swallow exceptions. Never show `SQLException` / stack traces to normal users.

Good: `Unable to load employees. Please try again.`  
Bad: raw JDBC/HTTP messages.

## Logging

Use SLF4J / Logback. Never `System.out.println` for application logging.  
Levels: TRACE / DEBUG / INFO / WARN / ERROR — use appropriately.  
Never log passwords, JWTs, API keys, secrets, or sensitive personal data.  
Remove temporary debug prints before completion.

## TODOs and comments

TODOs must state what, why, and expected resolution.  
Comments explain **why**, not obvious **what**.  
Javadoc public APIs, reusable components, and extension points — not trivial getters.

## Code format

- 4 spaces, no tabs
- Explicit imports, no unused imports, avoid wildcards unless tooling standardizes them
- Recommended class order: constants → fields → constructors → init → public → protected → private → properties

## Development loop

```text
UNDERSTAND → REUSE → DESIGN → IMPLEMENT → TEST → VERIFY
```

Never: create → copy → patch → hope.

## Related docs

| Doc | Topic |
|-----|--------|
| [architecture-rules.md](architecture-rules.md) | Layers, DI, packages, AI rules |
| [javafx-rules.md](javafx-rules.md) | Properties, threading, desktop UX |
| [fxml-rules.md](fxml-rules.md) | FXML, controllers, fx:id |
| [css-rules.md](css-rules.md) | Theme, spacing, naming |
| [component-rules.md](component-rules.md) | Reuse and new components |
| [state-management.md](state-management.md) | UI / ViewModel / app state |
| [concurrency.md](concurrency.md) | Background work, FX thread |
| [testing.md](testing.md) | Unit / integration / TestFX |
| [accessibility.md](accessibility.md) | Keyboard and a11y |

Also see: design system (`docs/design-system/`), mock data (`docs/development/mock-data/`),
navigation (Prompt 08), [launcher.md](launcher.md) (Prompt 11), [splash.md](splash.md) (Prompt 12).
