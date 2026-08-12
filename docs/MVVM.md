# SROTS MVVM Guidelines

## 1. MVVM Responsibilities

```text
View (JavaFX FXML / Controls)
  │ (Bi-directional Property Binding)
  ▼
ViewModel (UI State, Observable Properties, Commands)
  │ (Invokes Use Cases)
  ▼
Application Layer (Use Cases)
```

- **View**: JavaFX FXML, CSS, Controls, Layouts. Responsible strictly for display and capturing user interactions. Contains ZERO business logic, SQL, or network calls.
- **Controller**: Ultra-thin FXML controller. Delegates user actions to ViewModel methods.
- **ViewModel**: Owns UI state (`MainUiState`), observable properties (`StringProperty`, `ObservableList`), and coordinates Application use cases. ViewModels do NOT import `javafx.scene.control.*` or infrastructure classes.
