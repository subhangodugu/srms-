# Route Registry

`NavigationRegistry` / `DefaultNavigationRegistry` owns route metadata and hierarchy.

## Responsibilities

- Register routes from feature providers at startup
- Resolve metadata (label, icon, parent, permission, breadcrumb path)
- Fail fast on duplicate route registration
- Feed ViewFactory resolution and chrome (sidebar selection, breadcrumbs, titles)

## Typed routes

Use `NavigationRouteId` (enum). Prefer:

```text
navigation.navigate(NavigationRouteId.PROJECTS)
```

Never:

```text
navigate("/projects")
navigate("projectPage")
```

## Module registration

```text
ApplicationBootstrap
  → CoreNavigationProvider
  → FeatureNavigationProvider(s)
  → DefaultNavigationRegistry
  → NavigationService
```

Feature modules register route + metadata + view factory; they must not mutate Sidebar/TopBar/StatusBar internals.
