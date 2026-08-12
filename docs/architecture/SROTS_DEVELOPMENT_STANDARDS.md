# SROTS Development Standards & Rules Specification

## 1. Architectural Rules (Mandatory)
1. **Desktop App Only**: SROTS is strictly a native desktop application (Java 21 + JavaFX).
2. **No Web Frameworks**: React, Next.js, HTML, CSS web portals, SaaS web frontends, and Electron are strictly prohibited for SROTS desktop UI.
3. **MVVM Pattern**: UI views must not contain direct business logic or direct database/REST API calls.
4. **Clean Architecture**: Dependency direction points inward: `Presentation -> Application -> Domain <- Infrastructure`.
5. **Thread Safety**: Long-running network or disk operations MUST run on background threads using `AsyncTaskExecutor` / Virtual Threads; UI updates MUST execute on JavaFX Application Thread.
6. **Zero Placeholders**: Functional enterprise code logic for all modules.

## 2. Prohibited Technologies Matrix

| Category | Allowed Stack | PROHIBITED Stack |
| :--- | :--- | :--- |
| UI Framework | **JavaFX 21 + FXML + JavaFX CSS** | React, Next.js, Angular, Vue, HTML, Electron |
| Language | **Java 21 LTS** | JavaScript / TypeScript browser scripts for UI |
| Architecture | **MVVM + Clean Architecture** | Monolithic View-Controller coupling |
| Database | **SQLite (Local) / PostgreSQL (Central)** | Direct DB access from JavaFX Views |
| Build Tool | **Apache Maven** | Gradle, npm (for desktop UI) |
