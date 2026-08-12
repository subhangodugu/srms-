# ADR-0001: Native JavaFX Multi-Module Desktop Architecture

## Context
SROTS is the central internal operating and control platform for enterprise operations, managing products (including COMPTY), releases, teams, employees, sales, and analytics.

## Decision
Build SROTS as a **native desktop application** using Java 21 LTS, JavaFX 21, and a 6-module Maven Clean Architecture layout (`srots-domain`, `srots-application`, `srots-infrastructure`, `srots-presentation`, `srots-shared`, `srots-app`).

## Consequences
- Guarantees strict compile-time dependency boundaries.
- Prevents lazy coupling between UI controllers, business logic, and databases.
- Ensures cross-platform execution on Windows, Linux, and macOS.
