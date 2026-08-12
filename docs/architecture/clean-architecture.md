# SROTS Clean Architecture Specification

## Layer Responsibilities & Boundaries

1. **Domain**: Business Entities (`Product`, `Release`), Value Objects (`ProductId`, `VersionNumber`), Repository Contracts (`ProductRepository`), and Domain Rules (`ReleaseReadinessRule`).
2. **Application**: Use Cases (`GetProductsUseCase`), Commands (`CreateProductCommand`), Queries (`GetProductsQuery`), and DTOs (`ProductDTO`).
3. **Infrastructure**: Database Persistence (`SqliteProductRepository`, `PostgresProductRepository`), REST API Clients, WebSocket Clients, Security, and File Storage.
4. **Presentation**: JavaFX FXML, CSS Theme Tokens, Controllers, ViewModels, and Reusable UI Components.
5. **App**: `AppContainer` DI Factory and JavaFX Application Stage Launcher.
