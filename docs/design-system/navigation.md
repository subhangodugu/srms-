# Navigation System

Consistent sidebar, topbar, breadcrumbs, tabs, and status bar for the SROTS shell.

## Sidebar

| Property | Value |
|----------|--------|
| Style | `.srots-sidebar` |
| Background | `-srots-surface` |
| Border | Right 1px `-srots-border` |
| Expanded width | 240–280 px |
| Collapsed width | 64–72 px |

### Section model

```text
Overview
My Workspace
Company → Employees, Teams, Departments
Work → Projects, Tasks, Issues
Products → SROTS, COMPTY
Engineering
Releases
Sales
Support
Knowledge
Analytics
Settings
```

- Use **collapsible groups**; do not expand every submenu permanently.
- Group titles: `.srots-nav-group-title` (11 px, muted, bold).
- Items: `.srots-nav-item` (secondary text; hover → hover surface + primary text).

### Active navigation

Active state must be obvious but subtle:

```text
▌ Products
```

Implementation pattern:

- Soft primary fill (`-srots-primary-soft`)
- Primary text color
- **Left indicator** bar in `-srots-primary`
- Semibold / bold weight

Avoid huge bright blocks.

```css
.srots-nav-item:selected {
    -fx-background-color: -srots-primary-soft;
    -fx-text-fill: -srots-primary;
    -fx-font-weight: bold;
}
```

Collapsed mode shows icons only; tooltips provide labels.

## Topbar

Style: `.srots-topbar`

Contents:

| Zone | Content |
|------|---------|
| Left | Breadcrumb + page context |
| Center / Right | Global search |
| Right | Notifications, connection status, user profile |

Example:

```text
Products / COMPTY / Releases          Search    🔔    ● Online    Subhan ▾
```

Use professional vector icons (not emoji) for notifications and profile.

## Breadcrumbs

- Reflect hierarchy: `Products > COMPTY > Releases > v1.9.0 > Gates`
- Support deep linking / back-forward stack at the navigation layer
- Truncate middle segments on narrow widths with tooltip for full path

## Tabs

Style: `.srots-tabs` / `.srots-tab`

Use for **closely related** content under one entity:

```text
COMPTY
Overview | Requirements | Teams | Versions | Releases
```

| State | Appearance |
|-------|------------|
| Default | Secondary label |
| Hover | Primary text |
| Selected | Elevated surface + **2 px bottom** primary border + primary label |

Do not use tabs for unrelated workflows (use nav instead).

## Status bar

Style: `.srots-statusbar`

Shows system-level information:

```text
● Online · Last synchronized: 2 min ago · Version: SROTS 0.1.0
```

Connectivity states (see [state-styling.md](./state-styling.md)):

| State | Class | Cue |
|-------|-------|-----|
| Online | `.srots-online` | ● + success color |
| Syncing | `.srots-syncing` | ◐ + info color |
| Offline | `.srots-offline` | ○ + warning color |
| Sync error | `.srots-sync-error` | ⚠ + danger color |

Always pair color with text/symbol.

## Global vs scoped search

| Type | Placement | Prompt example |
|------|-----------|----------------|
| Global | Topbar | Search… / Ctrl+K |
| Feature | Toolbar / FilterBar | Search employees… |

Visually distinguish global (topbar) from page-scoped search (filter row). Future global index may include Employees, Teams, Projects, Tasks, Products, Issues, Releases, Customers, Documents.

## Filters

Reusable filter bar pattern:

```text
[ Search... ] [ Team ▼ ] [ Status ▼ ] [ Priority ▼ ] [ Date ▼ ]  [Clear Filters]
```

Behavior must be identical across modules. See Data components in [components.md](./components.md).

## RBAC navigation

- Build the nav tree from permissions, not from a static full menu with blank destinations.
- Disabled items (if shown) use muted/disabled text and are non-activatable.
- Never reveal admin-only routes in the Employee Portal shell.

## CSS source

`navigation.css`, `tabs.css`
