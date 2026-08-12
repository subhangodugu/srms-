# SROTS Coding Standards & Architectural Constraints

> Canonical frontend detail lives in [`docs/frontend/`](frontend/coding-standards.md) (Prompt 10).

## Mandatory Rules for All Developers & Agents

1. **No Business Logic in Controllers**: JavaFX FXML controllers MUST remain ultra-thin.
2. **No Direct Database Calls in Views or Controllers**: Views and controllers MUST NEVER issue SQL queries or call JDBC/REST APIs directly.
3. **No Infrastructure Imports in ViewModels**: ViewModels MUST NOT import SQLite, REST clients, or infrastructure persistence classes.
4. **Pure Java Domain**: Domain entities MUST NOT import JavaFX, Spring, Hibernate, or database annotations.
5. **Separation of Models**: Domain Models ≠ API DTOs ≠ Database Records ≠ UI Models. Explicit mappers MUST be used.
6. **Thread Safety**: Network and disk I/O MUST execute off the JavaFX Application Thread. UI updates MUST execute on the JavaFX Application Thread.
7. **Native JavaFX only**: Do not introduce web/SaaS UI frameworks for SROTS.
8. **Reuse SROTS components and CSS**: Prefer `Srots*` controls and `srots-*` style classes; no inline CSS for normal UI.
9. **NavigationService**: Feature controllers must not call `stage.setScene`.
10. **Mock behind repositories**: Never hard-code mock datasets in Views/ViewModels.
