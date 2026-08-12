# Architecture Rules (Frontend)

## Dependency direction

```text
Presentation → Application → Domain
                     ↑
              Infrastructure
```

Infrastructure implements contracts defined by inner layers.

Forbidden:

```text
View → PostgreSQL / SQLite / JDBC / JPA
View / Controller → HttpClient / REST / WebSocket
Controller → Hibernate / business calculations / authorization authority
```

## Layer responsibilities

| Layer | Owns | Does not own |
|-------|------|--------------|
| **Presentation** | Views, FXML, thin controllers, ViewModels, components, navigation UI, formatting | SQL, REST, domain rules, auth authority |
| **Application** | Use cases, workflows, DTOs/app models, orchestration | JavaFX nodes, persistence engines |
| **Domain** | Entities, value objects, domain rules/services | JavaFX, Spring, JDBC, mock packages |
| **Infrastructure** | Mock / SQLite / REST / WebSocket / Postgres adapters | UI layout, ViewModels |

## Feature development order

```text
Requirement → Domain (if needed) → Use Case → Repository contract
→ Mock implementation → ViewModel → View → Components → Navigation → Tests
```

## Screen development order

Do not start with a giant FXML. First define:

1. Screen purpose
2. State model
3. Required components
4. ViewModel state
5. Use cases
6. Route / navigation
7. Loading / empty / error / offline handling
8. Permissions (UX only; backend remains authoritative)
9. Tests

## Dependency injection

Inject ViewModels, use cases, repositories, navigation, services, factories.

Avoid service locators (`ApplicationContext.getInstance()...`) and constructing deep graphs inside controllers.

## Singletons

Allowed only for controlled composition roots (e.g. app bootstrap).
Do not make every ViewModel, repository, or component a singleton.

## Configuration

Centralize ports, timeouts, paths, data mode (`MockConfiguration`, env properties).
Do not scatter `localhost` / magic paths through feature code.

## Mapping and DTOs

```text
REST/Infra DTO → Mapper → Application/Domain model → ViewModel → View
```

Never bind infrastructure DTOs directly to JavaFX controls.
Keep complex mapping out of controllers.

## Packages

Use architectural packages:

```text
com.srots.presentation
com.srots.application
com.srots.domain
com.srots.infrastructure
```

Forbidden: `misc`, `stuff`, `helpers2`, `temp`, `new`.

## Utilities

No giant `Utils.java`. Prefer focused helpers (`DateFormatter`, `CurrencyFormatter`).
Create a helper only when it represents a real shared responsibility.

## AI agent rules

Must:

- Read existing architecture before changing code
- Reuse components, services, CSS, ViewModels, navigation
- Avoid duplicate implementations and unnecessary files
- Run `mvn clean verify` after meaningful changes
- Preserve boundaries

Must not:

- Introduce web frameworks
- Hard-code mock data into Views
- Bypass ViewModel / Use Case
- Access databases from UI
- Add inline CSS for normal styling
- Rewrite working modules without justification
- Leave `System.out` debug noise
