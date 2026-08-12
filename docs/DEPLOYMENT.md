# SRMS Deployment & Packaging Guide

SRMS is packaged as a runnable multi-module JAR suite and native Windows desktop installer via `jpackage`.

## Target Environment Requirements
- **OS**: Windows 10 / 11 (64-bit)
- **Runtime**: Java 21 OpenJDK runtime (bundled into native executable)
- **Database**: PostgreSQL 16 server (Production) or embedded H2 engine (Offline / Standalone mode)

## Deployment Steps

### 1. Build Production Package
```powershell
.\scripts\build.ps1
```

### 2. Run Backend Service
```powershell
.\scripts\run-backend.ps1
```

### 3. Run Desktop Application Client
```powershell
.\scripts\run-srots-desktop.ps1
```

### 4. Generate Windows Desktop Executable (`SRMS.exe`)
```powershell
.\scripts\package-windows.ps1
```
The resulting installer is output to `deployment/dist/SRMS.exe`.
