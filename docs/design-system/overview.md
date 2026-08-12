# SROTS Desktop Design System — Overview

The **SROTS Design System** is the single visual source of truth for the native SROTS desktop application. Every Admin Portal, Employee Portal, and module screen (Company, Projects, Products, COMPTY, Engineering, Releases, Sales, Support, Knowledge, Analytics, Settings) must consume these tokens, layouts, and components.

## Platform

| Item | Value |
|------|--------|
| Runtime | Java 21 |
| UI toolkit | JavaFX 21 |
| Markup | FXML |
| Styling | JavaFX CSS (looked-up colors, `-fx-*` properties) |
| Architecture | MVVM |
| Module | `srots-desktop/srots-presentation` |

This is a **desktop** design system. Do **not** treat it as a web, React, Next.js, HTML/CSS, SaaS marketing, or mobile UI.

## Visual character

```text
Professional · Technical · Modern · Calm · Precise
Information-rich · Minimal · High-quality · Enterprise
```

Avoid: flashy gradients, heavy glassmorphism, decorative illustrations, gaming/consumer UI, neon accents, huge typography, excessive animation, emoji as core icons.

## Design principles

1. **Enterprise** — Suitable for a professional technology company operating system.
2. **Information dense** — Useful content without wasted empty space.
3. **Clear hierarchy** — Users always know: Where am I? What am I looking at? What needs attention? What can I do? What changed?
4. **Consistent** — Same component looks and behaves the same everywhere.
5. **Predictable** — Navigation, buttons, forms, tables, dialogs, and status indicators share one interaction model.
6. **Accessible** — Readable contrast, keyboard use, visible focus, non-color status cues.
7. **Desktop first** — Designed for large screens; still usable when resized.
8. **Resizable** — Layouts use JavaFX containers/constraints, not browser responsive CSS.

### Supported window sizes

```text
1280×720 · 1366×768 · 1440×900 · 1600×900 · 1920×1080 · 2560×1440
```

## Hierarchy of the system

```text
SROTS DESIGN SYSTEM
        │
        ├── Design Tokens (colors, type, spacing, radius, motion)
        ├── Layout System (AppShell, page structure, grid)
        ├── Typography
        ├── Navigation
        ├── Components
        ├── Interaction States
        ├── Accessibility
        └── Patterns
                 │
                 ▼
          APPLICATION SCREENS
                 │
       ┌─────────┼─────────┐
       ▼         ▼         ▼
    Company   Products   Engineering
       │         │         │
       ▼         ▼         ▼
    Employees  COMPTY    Releases
```

## Theme at a glance

| Role | Hex | Looked-up token |
|------|-----|-----------------|
| Background | `#0B0F14` | `-srots-bg` |
| Surface | `#111827` | `-srots-surface` |
| Surface Elevated | `#172033` | `-srots-surface-elevated` |
| Surface Hover | `#1D2738` | `-srots-surface-hover` |
| Border | `#273244` | `-srots-border` |
| Border Strong | `#344155` | `-srots-border-strong` |
| Text Primary | `#F8FAFC` | `-srots-text-primary` |
| Text Secondary | `#CBD5E1` | `-srots-text-secondary` |
| Text Muted | `#94A3B8` | `-srots-text-muted` |
| Text Disabled | `#64748B` | `-srots-text-disabled` |
| Primary | `#6D5DFB` | `-srots-primary` |
| Success | `#22C55E` | `-srots-success` |
| Warning | `#F59E0B` | `-srots-warning` |
| Danger | `#EF4444` | `-srots-danger` |
| Info | `#38BDF8` | `-srots-info` |

> **JavaFX note:** Tokens are **looked-up colors** (`-srots-*` on `.root`), not web CSS custom properties (`--srots-*`). Always reference them as `-srots-primary`, never `var(--srots-primary)`.

## App shell (constant)

```text
┌─────────────────────────────────────────────────────────────┐
│ Topbar                                                       │
├──────────────┬──────────────────────────────────────────────┤
│ Sidebar      │ Main Content (padding 24px)                   │
│ 240–280 px   │                                               │
│ (64–72 coll.)│                                               │
├──────────────┴──────────────────────────────────────────────┤
│ Status Bar                                                   │
└─────────────────────────────────────────────────────────────┘
```

## CSS location

```text
srots-presentation/src/main/resources/css/
├── theme.css          ← import hub (load this only)
├── tokens.css
├── base.css
├── typography.css
├── layout.css
├── navigation.css
├── buttons.css
├── forms.css
├── cards.css
├── tables.css
├── tabs.css
├── dialogs.css
├── feedback.css
├── charts.css
├── states.css
└── components.css     ← shared aliases / badges
```

Load `theme.css` once at the application root. Do not invent per-screen stylesheets for one-off colors.

## Documentation map

| Doc | Topic |
|-----|--------|
| [colors.md](./colors.md) | Palette, semantic usage, looked-up names |
| [color-tokens.md](./color-tokens.md) | Full token inventory |
| [typography.md](./typography.md) | Scale, weights, classes |
| [spacing.md](./spacing.md) | Spacing scale & usage |
| [layout.md](./layout.md) | Shell, page structure, density, radius |
| [navigation.md](./navigation.md) | Sidebar, topbar, breadcrumbs, tabs |
| [buttons.md](./buttons.md) | Button variants & states |
| [forms.md](./forms.md) | Fields, validation |
| [tables.md](./tables.md) | Density, actions, badges |
| [cards.md](./cards.md) | Cards & KPI rules |
| [dashboards.md](./dashboards.md) | Dashboard grid |
| [dialogs.md](./dialogs.md) | Dialogs & drawers |
| [notifications.md](./notifications.md) | Toasts, alerts, tooltips |
| [charts.md](./charts.md) | Chart standards |
| [accessibility.md](./accessibility.md) | A11y, keyboard, focus, RBAC UI |
| [animation.md](./animation.md) | Motion 120–220 ms |
| [components.md](./components.md) | Component catalog (Prompt 05 §59) |
| [css-architecture.md](./css-architecture.md) | File load order & naming |
| [javafx-css-rules.md](./javafx-css-rules.md) | JavaFX CSS constraints |
| [component-styling.md](./component-styling.md) | Style class reference |
| [state-styling.md](./state-styling.md) | Interactive & app states |

## Quality rules (summary)

Future screens **must** use existing tokens, components, typography, spacing, status colors, buttons, forms, tables, dialogs, and loading/empty/error states.

If a pattern is missing, add it to the design system first — do not ship one-off module styling.

## Out of scope for the design-system foundation

Do **not** invent full business screen implementations in these docs (Employees, Products, COMPTY CRUD, Sales, Releases backend flows, auth, database). Document visual patterns only; modules consume them later.

## Related implementation

- Package: `com.srots.presentation.components`
- Stylesheets: `srots-presentation/src/main/resources/css/`
- Showcase (dev/QA): internal Design System showcase screen — not shown to normal employees
