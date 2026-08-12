# SROTS Dependency Rules Matrix

## Strict Dependency Direction
1. **Domain Isolation**: `srots-domain` MUST NEVER depend on JavaFX, Spring, Hibernate, JPA, SQLite, or REST frameworks.
2. **Presentation Isolation**: `srots-presentation` MUST NEVER depend directly on `srots-infrastructure` classes (e.g. `SqliteProductRepository`, `ApiProductRepository`).
3. **Controller Isolation**: JavaFX FXML Controllers MUST NEVER contain SQL, HTTP API calls, or business rules.
4. **ViewModel Isolation**: ViewModels MUST NOT import database drivers, JDBC, or network transport classes.
