# SROTS Master Architecture Overview

SROTS (SORTS Resource & Operations Control System) is an enterprise-grade cross-platform desktop application built using:
- **Java 21 LTS**
- **JavaFX 21**
- **Maven Multi-Module Architecture**
- **MVVM Presentation Pattern**
- **Clean Architecture Principles**

## 6-Module Architecture Matrix

```text
srots-domain              (Pure Java SE Core — No Frameworks)
      ▲
      │
srots-application         (Use Cases, Commands, Queries, DTOs)
      ▲
      ├─── srots-infrastructure (SQLite, PostgreSQL, REST API, WebSocket, Security, Storage)
      │
      ├─── srots-presentation   (JavaFX Views, ViewModels, FXML, CSS, UI Components)
      │
srots-shared              (Cross-Cutting Primitives, Exceptions, Results)

srots-app                 (Bootstrap Launcher & Dependency Wiring Container)
```
