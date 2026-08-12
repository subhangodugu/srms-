# Srots Component Library

Authoritative index of reusable **native JavaFX** controls in `com.srots.presentation.components.*`.

Components are presentation-only: **no business logic, no REST calls, no domain rules**. Screens and ViewModels compose these pieces. Styling comes from Prompt 05/06 CSS (`srots-*` classes / `SrotsStyleClasses`) — do not hard-code colors or invent parallel widgets for the same pattern.

| Doc | Package focus | Authoritative types |
|-----|---------------|---------------------|
| [layout.md](./layout.md) | `layout.app`, `layout.page`, `layout.panel`, `layout.split` | `SrotsAppShell`, `SrotsPageContainer`, `SrotsPageHeader`, `SrotsSection`, `SrotsPanel`, `SrotsSplitView` |
| [navigation.md](./navigation.md) | `navigation.sidebar`, `navigation.topbar`, `navigation.breadcrumb`, `navigation.tabs` | `SrotsSidebar`, `SrotsTopBar`, `SrotsBreadcrumb`, `SrotsTabView` |
| [buttons.md](./buttons.md) | `actions.button`, `actions.icon`, `actions.menu` | `SrotsButton`, `SrotsIconButton`, `SrotsMenu` |
| [forms.md](./forms.md) | `forms.*` | `SrotsFormField`, `SrotsTextField`, `SrotsComboBox`, … |
| [tables.md](./tables.md) | `data.table`, `data.pagination`, `data.filter`, `data.search` | `SrotsDataTable`, `SrotsPagination`, `SrotsFilterBar`, `SrotsSearchField` |
| [cards.md](./cards.md) | `information.card`, `information.kpi`, `information.metric`, `information.timeline`, `information.activity`, `information.avatar` | `SrotsCard`, `SrotsKpiCard`, `SrotsMetric`, … |
| [badges.md](./badges.md) | `information.badge` | `SrotsStatusBadge`, `SrotsStatus` |
| [dialogs.md](./dialogs.md) | `overlays.dialog`, `overlays.drawer`, `overlays.tooltip`, `overlays.command` | `SrotsDialog`, `SrotsDetailPanel`, `SrotsTooltip`, `SrotsCommandPalette` |
| [feedback.md](./feedback.md) | `feedback.*` | `SrotsAlert`, `SrotsToast`, `SrotsEmptyState`, `SrotsErrorState`, … |
| [charts.md](./charts.md) | `charts.*` | `SrotsBarChart`, `SrotsLineChart`, `SrotsAreaChart`, `SrotsDonutChart`, `SrotsProgressChart` |
| [accessibility.md](./accessibility.md) | Cross-cutting | Focus, keyboard, a11y conventions for all components |

## Shared utilities

| Type | Package | Role |
|------|---------|------|
| `SrotsStyleClasses` | `utility` | Canonical `srots-*` class name constants |
| `SrotsSize` | `utility` | `SMALL` / `STANDARD` / `LARGE` |
| `SrotsIcon` | `utility.icons` | Icon node factory |
| `SrotsDateDisplay`, `SrotsDateTimeDisplay`, `SrotsRelativeTime` | `utility.date` | Display-only date formatting |
| `SrotsNumberFormat` | `utility.formatting` | Display-only number formatting |

## Rules of use

1. **One pattern → one component** — Prefer `SrotsButton` over legacy `buttons.SrotsButtons`; prefer `information.*` over older `cards.*` / `badges.*` helpers.
2. **CSS owns look** — Apply / inherit `srots-*` classes; do not inline paint for theme colors.
3. **JavaFX only** — No HTML/CSS web stacks, no third-party UI kits for core chrome.
4. **Compose, don’t fork** — Extend behavior in ViewModels/screens; keep library components generic.

Related system docs: [overview](../overview.md) · [components catalog](../components.md) · [component styling](../component-styling.md) · [state styling](../state-styling.md).
