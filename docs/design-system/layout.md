# Layout System

Desktop-first application shell and page structure for SROTS. Use JavaFX layout panes (`BorderPane`, `HBox`, `VBox`, `StackPane`, `GridPane`, `SplitPane`) — **not** browser flexbox/grid CSS.

## Application shell

```text
┌─────────────────────────────────────────────────────────────┐
│ Topbar                                                       │
├──────────────┬──────────────────────────────────────────────┤
│              │                                              │
│ Sidebar      │ Main Content                                 │
│              │                                              │
│              │                                              │
├──────────────┴──────────────────────────────────────────────┤
│ Status Bar                                                   │
└─────────────────────────────────────────────────────────────┘
```

| Region | Style class | Notes |
|--------|-------------|-------|
| Shell root | `.srots-shell` | Background `-srots-bg` |
| Sidebar | `.srots-sidebar` | Surface + right border |
| Topbar | `.srots-topbar` | Surface + bottom border |
| Content | `.srots-content` / `.srots-app` | Background `-srots-bg`, padding **24** |
| Status bar | `.srots-statusbar` | Surface + top border |

The shell must remain consistent across Admin Portal, Employee Portal, and all modules.

## Sidebar widths

| Mode | Width |
|------|-------|
| Expanded | **240–280 px** |
| Collapsed | **64–72 px** |

At smaller window widths, collapse the sidebar automatically. At larger widths, keep expanded and optionally show detail panels.

## Content padding

| Screen size | Content padding |
|-------------|-----------------|
| Default desktop | **24 px** |
| Large desktop | **32 px** (optional) |

## Page structure

Every major screen follows:

```text
Page
│
├── Page Header
│   ├── Breadcrumb
│   ├── Title
│   ├── Description
│   └── Primary Action (+ secondary actions)
│
├── Toolbar / Filters
│
├── Main Content
│
└── Supporting Content (optional)
```

Do not invent a unique chrome per module.

### Page header example (pattern only)

```text
COMPTY Releases

Manage product versions, release gates and deployment status.

                                        [Create Release]
```

## Surfaces & elevation

| Class | Role |
|-------|------|
| `.srots-surface` | Flat chrome surfaces |
| `.srots-surface-elevated` | Cards, panels (8 px radius) |
| `.srots-bordered` | Standard border |
| `.srots-bordered-strong` | Stronger outline |
| `.srots-focused` | Focus border color |

Prefer **borders + surface contrast** over heavy drop shadows. Dialogs may use subtle elevation if needed.

## Border radius

| Element | Radius |
|---------|--------|
| Small controls / buttons | **6 px** (`-srots-radius-sm`) |
| Cards / tables | **8–10 px** (`-srots-radius-md` ≈ 8) |
| Dialogs | **10–12 px** (`-srots-radius-lg` ≈ 12) |
| Pills / badges | **999 px** (`-srots-radius-pill`) |

Do not round every surface heavily.

## Density modes

| Mode | Use |
|------|-----|
| Compact | Dense operational tables |
| **Standard** (default) | Most enterprise screens |
| Comfortable | Dashboards, review / executive views |

Future user preference may toggle table density (36 / 44 / 52). Default remains Standard.

## Responsive desktop behavior (JavaFX)

| Width behavior | Response |
|----------------|----------|
| Narrow | Collapse sidebar; hide secondary columns / panels |
| Wide | Show more columns; optional detail drawer |

Rules:

- No overlapping controls.
- No browser media-query layouts.
- Use bindable constraints and `SplitPane` dividers.

## Split / detail layouts

Prefer a main table + side **Drawer / Detail Panel** over extra navigation hops. See [dialogs.md](./dialogs.md).

## Release & COMPTY layout language

- **Release gates:** vertical timeline of stages (Development → QA → Security → Database → Approval → Deployment → Production) with status glyphs — same tokens, no special palette.
- **COMPTY workspace:** nested under Products → COMPTY; same shell/tabs patterns with subtle product context in breadcrumb/title only — **not** a separate visual language.

## Permission-aware layout (RBAC UI)

Hide or disable actions and nav items by permission. Do not leave empty chrome that implies access. See [accessibility.md](./accessibility.md#role-based-ui).

## CSS source

`layout.css`, `navigation.css`
