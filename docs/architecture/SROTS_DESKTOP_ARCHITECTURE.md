# SROTS Desktop Architecture Specification

## 1. Overview
SROTS Desktop is a high-performance native desktop application built using **Java 21 LTS**, **JavaFX 21**, **FXML**, and **JavaFX CSS**. It implements **MVVM (Model-View-ViewModel)** in the presentation layer and adheres to **Clean Architecture** principles throughout the application stack.

## 2. Desktop Layering & Clean Architecture Stack

```text
┌─────────────────────────────────────────────────────────────┐
│                 PRESENTATION LAYER (JavaFX)                 │
│   FXML Views ──► ViewModels (Property Bindings/Commands)    │
└──────────────────────────────┬──────────────────────────────┘
                               │ (Calls Use Cases)
┌──────────────────────────────▼──────────────────────────────┐
│                      APPLICATION LAYER                      │
│      Use Cases / Interactors ──► DTOs / Query Handlers      │
└──────────────────────────────┬──────────────────────────────┘
                               │ (Depends on Interfaces)
┌──────────────────────────────▼──────────────────────────────┐
│                        DOMAIN LAYER                         │
│     Entities ──► Value Objects ──► Repository Interfaces    │
└──────────────────────────────▲──────────────────────────────┘
                               │ (Implements Interfaces)
┌──────────────────────────────┴──────────────────────────────┐
│                    INFRASTRUCTURE LAYER                     │
│  SQLite (Local DB) │ REST Client (Spring Boot) │ System API │
└─────────────────────────────────────────────────────────────┘
```

## 3. Desktop Native Integration Services
SROTS Desktop integrates with operating system services across Windows, Linux, and macOS:

1. **Native Windowing & Multi-Screen**: Resizable windows with custom title bars, full-screen support, state preservation across restarts, and multi-monitor positioning.
2. **System Tray & Desktop Notifications**: Native system tray icon with status menu, badge indicators, and native OS notification integration via `java.awt.SystemTray` / JavaFX Notifications.
3. **Local Secure Storage**: Secure hardware-backed storage for JWT tokens and encryption keys (Windows DPAPI, macOS Keychain, Linux Secret Service API via Keyring).
4. **File System Integration**: Native JavaFX `FileChooser` / `DirectoryChooser` for document exports (PDF, CSV) and drag-and-drop file imports.
5. **Background Thread Management**: Custom `AsyncTaskExecutor` leveraging Java 21 Virtual Threads (`Executors.newVirtualThreadPerTaskExecutor()`) ensuring the JavaFX Application Thread (`Platform.runLater()`) is never blocked by network or disk I/O.

## 4. MVVM Data Binding Strategy
- **Views**: Defined in FXML or Java code. Views observe `ViewModel` properties (JavaFX `ObservableValue`, `Property`, `ObservableList`) and bind UI controls bi-directionally.
- **ViewModels**: Expose reactive properties, command execution handlers (`Runnable` / `Consumer`), and validation states. Contain ZERO UI imports (`javafx.scene.control.*` strictly prohibited inside ViewModels).
- **Controllers**: Thin controllers that only bind FXML elements to ViewModels and delegate events.
