# SROTS Clean Architecture Principles

## Layer Isolation Principles
1. **Domain Layer (`srots-domain`)**: Pure Java business concept models (`Product`, `ReleaseGate`), strongly typed value objects (`ProductId`, `VersionNumber`), and repository ports (`ProductRepository`). Pure Java — NO framework annotations or imports.
2. **Application Layer (`srots-application`)**: Business use cases (`GetProductsUseCase`), application DTOs (`ProductDTO`), commands, and queries.
3. **Infrastructure Layer (`srots-infrastructure`)**: SQLite database persistence, REST API integration, filesystem storage, and security. Implements repository interfaces defined in `srots-domain`.
4. **Presentation Layer (`srots-presentation`)**: JavaFX UI controls, ViewModels, FXML layouts, and JavaFX CSS tokens.
5. **App Bootstrap (`srots-app`)**: Main launcher (`SROTSApplication`) and dependency injection factory (`AppContainer`).
