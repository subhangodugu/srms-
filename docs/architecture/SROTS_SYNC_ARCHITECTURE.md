# SROTS Synchronization Architecture Specification

## 1. Sync Architecture Overview
To ensure seamless desktop operation during temporary network disruptions, SROTS specifies an **Offline-First Synchronization Engine**:

```text
Local Action (Offline)
    │
    ▼
Write to SQLite Mutation Queue (Pending status)
    │
    ▼
Network Monitor Detects Connection
    │
    ▼
Sync Engine Replays Mutation Queue to Central REST API
    │
    ├── SUCCESS ──► Mark Synced & Flush Queue Item
    └── CONFLICT ─► Invoke Conflict Resolver Strategy
```

## 2. Conflict Resolution Strategies
- **Server Wins (Default for Governance/Releases)**: Server timestamp and database state override local edits.
- **Client Wins (User Drafts/Personal Notes)**: Local changes override central state.
- **Merge/Field-Level Resolution**: Non-overlapping field updates are merged automatically.
