# SROTS — Agent Rules & Architecture Guidelines

## Product

**SROTS** (SORTS Resource Management System) is a **native JavaFX desktop** enterprise application.

It is **not** a web/SaaS frontend. Do not introduce React, Next.js, Electron, HTML/DOM, or browser UI stacks for SROTS.

## Stack (srots-desktop)

- **Presentation**: JavaFX 21, FXML, JavaFX CSS, MVVM, reusable `Srots*` components
- **Application**: Use cases, DTOs, orchestration
- **Domain**: Entities, value objects, repository contracts
- **Infrastructure**: Mock repositories (dev), future SQLite / REST / WebSocket / PostgreSQL adapters
- **Build**: Maven multi-module under `srots-desktop/`

## Mandatory flow

```text
JavaFX View → ViewModel → Use Case → Repository Interface → Infrastructure
```

## Principles

1. Clean Architecture dependency direction: Presentation → Application → Domain ← Infrastructure
2. No database / REST / WebSocket clients inside Views, Controllers, or ViewModels
3. Navigation only via `NavigationService` (Prompt 08)
4. Reuse design system (Prompt 05–07): no inline CSS, no duplicate components
5. Mock data only behind repository interfaces (Prompt 09); production must not silently use MOCK
6. Explicit UI states (`LoadState`); no blocking the JavaFX Application Thread
7. Frontend standards: `docs/frontend/` (Prompt 10)

## Launch

Process entry: `com.srots.app.SrotsLauncher`  
Startup docs: `docs/frontend/launcher.md`  
Main window: `docs/frontend/main-window.md` (Prompt 13)  
Sidebar: `docs/frontend/navigation/sidebar.md` (Prompt 14)  
TopBar: `docs/frontend/navigation/topbar.md` (Prompt 15)  
StatusBar: `docs/frontend/shell/status-bar.md` (Prompt 16)

```text
mvn -pl srots-app org.openjfx:javafx-maven-plugin:run
```


## Note on legacy modules

`srms-backend` may exist for older Spring-based API work. Prefer `srots-desktop` for current SROTS native desktop development.
