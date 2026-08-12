# Search Navigation

Selecting a data result:

```text
SearchResult.routeId → NavigationRouteId.valueOf → NavigationService.navigate
```

No `new FeatureView()` in click handlers. Authorization remains in navigation guards.
