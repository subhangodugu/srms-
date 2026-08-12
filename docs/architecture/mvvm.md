# SROTS MVVM Presentation Pattern

## Presentation Component Flow

```text
View (.fxml + CSS + Control)
   │ (Property Binding & Event Triggers)
   ▼
Controller (Thin Event Dispatcher)
   │ (Invokes ViewModel methods)
   ▼
ViewModel (UI State, Observable Properties, Async Coordination)
   │ (Executes Use Case)
   ▼
Application Layer (Use Cases & DTOs)
```

- ViewModels own explicit UI State objects (`ProductUiState`, `MainUiState`).
- Controllers set up bindings and pass user actions to ViewModels.
