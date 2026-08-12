# Design System Component Catalog

Reusable JavaFX controls and layouts for SROTS Desktop.

**Package:** `com.srots.presentation.components`  
**Styles:** `srots-presentation/src/main/resources/css/`

This catalog matches Prompt 05 §59. Components are the visual foundation — **do not invent business screens here**; modules compose these pieces later.

## Universal component states

Every reusable component must define (as applicable):

```text
Default · Hover · Pressed · Focused · Disabled · Selected · Loading · Error
```

Components must not invent private state palettes — use tokens from `tokens.css` / [state-styling.md](./state-styling.md).

---

## Layout

| Component | Role |
|-----------|------|
| `AppShell` | Topbar + Sidebar + Content + StatusBar host |
| `PageContainer` | Standard page padding (24) + scroll host |
| `Section` | Titled content block with section spacing |
| `Stack` | Vertical/horizontal stack with scale spacing |
| `Grid` | Dashboard / KPI grid helper |
| `SplitPane` | Master–detail and resizable regions |

---

## Navigation

| Component | Role |
|-----------|------|
| `Sidebar` | Expanded/collapsed nav rail |
| `NavigationItem` | Single nav entry + active indicator |
| `Breadcrumb` | Hierarchy path |
| `Tabs` | Related entity sections |
| `Topbar` | Search, notifications, profile, connection |

---

## Data

| Component | Role |
|-----------|------|
| `Table` | Themed `TableView` (`.srots-table`) |
| `TableRow` | Density-aware row behavior |
| `Pagination` | Page controls under tables |
| `FilterBar` | Search + filter controls + clear |
| `Search` | Scoped or embeddable search field |
| `Sort` | Header sort affordances |

Densities: Compact 36 / Standard 44 / Comfortable 52.

---

## Feedback

| Component | Role |
|-----------|------|
| `Toast` | Transient notification |
| `Alert` | Inline semantic banner |
| `EmptyState` | Explains empty data + CTA |
| `LoadingState` | Page/table loading copy + indicator |
| `ErrorState` | Failure message + Retry |
| `Progress` | Determinate/indeterminate progress |

Also cover offline / sync messaging via status classes (`.srots-online`, `.srots-offline`, `.srots-syncing`, `.srots-sync-error`).

---

## Form

| Component | Role |
|-----------|------|
| `TextField` | Single-line input |
| `PasswordField` | Masked input |
| `ComboBox` | Selection |
| `DatePicker` | Date entry |
| `CheckBox` | Boolean |
| `RadioButton` | Exclusive choice |
| `Toggle` | On/off switch |
| `TextArea` | Multiline |

Anatomy: label → input → helper → validation message.

---

## Actions

| Component | Role |
|-----------|------|
| `PrimaryButton` | Primary CTA |
| `SecondaryButton` | Cancel / secondary |
| `DangerButton` | Destructive |
| `IconButton` | Icon-only (tooltip required) |
| `SplitButton` | Primary + overflow menu |

Tertiary text buttons use `.srots-tertiary-button`.

---

## Information

| Component | Role |
|-----------|------|
| `KpiCard` | Metric tile (label, value, trend) |
| `InfoCard` | Summary / status card |
| `StatusBadge` | Semantic status pill |
| `Metric` | Compact metric display |
| `Timeline` | Sequential stages (e.g. release gates) |
| `ActivityItem` | Activity feed row |

### Release gate visual language

`Timeline` stages (pattern):

```text
Development ✓
      │
QA ✓
      │
Security ✓
      │
Database ⚠
      │
Approval ○
      │
Production ○
```

Use semantic colors + symbols; same tokens as the rest of SROTS.

### COMPTY product workspace

COMPTY screens reuse this catalog under Products → COMPTY (Overview, Roadmap, Requirements, Team, Engineering, Versions, Releases, Customers, Analytics). Subtle product identity via breadcrumb/title only — **not** a separate component set.

---

## Overlay

| Component | Role |
|-----------|------|
| `Dialog` | Modal shell |
| `ConfirmationDialog` | Confirm / cancel |
| `Drawer` | Side detail panel |
| `Tooltip` | Hover help |
| `Popover` | Anchored lightweight panel |

---

## Icons

Single professional vector family. Sizes: **12 / 16 / 20 / 24**. No emoji as core UI icons.

---

## Quality rules

All future screens must:

1. Use existing design tokens  
2. Use existing reusable components  
3. Use existing typography  
4. Use existing spacing  
5. Use existing status colors  
6. Use existing button styles  
7. Use existing form styles  
8. Use existing table styles  
9. Use existing dialog styles  
10. Use existing loading / error / empty states  

If a pattern is missing, extend this catalog first.

## Prohibited practices

Do **not**:

- Use inline styling everywhere (`node.setStyle(...)`)
- Duplicate button / card / table styles per module
- Use arbitrary colors or spacing
- Use emoji as core UI icons
- Ship giant sparse dashboard cards
- Overuse gradients or glassmorphism
- Use random border radii
- Create inconsistent navigation
- Put business logic into UI styling
- Create browser-specific layouts (HTML/React/Tailwind)

## Showcase

An internal Design System showcase screen (dev/QA) should demonstrate colors, typography, buttons, forms, tables, cards, KPI cards, badges, tabs, dialogs, notifications, progress, charts, and loading/empty/error/offline states. Not shown to normal employees.
