# SROTS Module Boundaries Specification

| Module Name | Artifact ID | Primary Responsibility | Allowed Dependencies |
| :--- | :--- | :--- | :--- |
| **Domain** | `srots-domain` | Pure Java business entities, value objects, rules, repository ports | `srots-shared` (Pure Java) |
| **Application** | `srots-application` | Use cases, commands, queries, DTOs, mappers | `srots-domain`, `srots-shared` |
| **Infrastructure** | `srots-infrastructure` | Database adapters (SQLite, Postgres), REST API, WebSocket, Security, Storage | `srots-application`, `srots-domain`, `srots-shared` |
| **Presentation** | `srots-presentation` | JavaFX views, FXML layouts, CSS, ViewModels, UI State, Component Library | `srots-application`, `srots-domain`, `srots-shared`, JavaFX |
| **Shared** | `srots-shared` | Cross-cutting technical primitives, exceptions, Result<T>, validation | SLF4J, JUnit |
| **App Bootstrap** | `srots-app` | Application bootstrap, `AppContainer` DI factory, JavaFX stage launcher | All 5 modules |
