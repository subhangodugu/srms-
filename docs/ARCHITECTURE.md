# SROTS Clean Architecture Master Guide

## 1. Architecture Overview
SROTS Desktop enforces strict **Clean Architecture** combined with **MVVM (Model-View-ViewModel)** across a multi-module Maven structure.

```text
                 PRESENTATION (JavaFX, Views, ViewModels, FXML, CSS)
                      │
                      ▼
                 APPLICATION (Use Cases, Commands, Queries, DTOs)
                      │
                      ▼
                    DOMAIN (Entities, Value Objects, Domain Rules, Repository Ports)
                      ▲
                      │ (Implements Interfaces)
                 INFRASTRUCTURE (SQLite, REST API, Filesystem, Security)
```

## 2. Unidirectional Dependency Rule
The Domain layer is the innermost core and depends on ZERO frameworks or external libraries. Dependencies point inward toward the Domain. Infrastructure implements interfaces defined by Domain and Application layers. Presentation depends on Application and Domain, but NEVER directly on Infrastructure.
