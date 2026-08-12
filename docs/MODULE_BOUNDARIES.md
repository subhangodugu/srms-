# SROTS Module Boundaries Specification

## 1. Maven Modules Summary

| Module | Artifact ID | Purpose | Allowed Dependencies |
| :--- | :--- | :--- | :--- |
| **Domain** | `srots-domain` | Pure Java business entities, value objects, domain rules, repository contracts | None (Pure Java SE + JUnit) |
| **Application** | `srots-application` | Use cases, commands, queries, DTOs, mappers | `srots-domain` |
| **Infrastructure**| `srots-infrastructure`| SQLite persistence, REST clients, filesystem, security | `srots-domain`, `srots-application` |
| **Presentation**  | `srots-presentation`  | JavaFX views, FXML layouts, CSS styles, ViewModels, UI State | `srots-domain`, `srots-application`, JavaFX |
| **App Launcher**  | `srots-app`           | Application bootstrap, `AppContainer` DI factory, `SROTSApplication` launcher | All 4 modules |
