# Shared navigation items

Define destinations once with `StyledNavigationItem<K>` and pass the same list
to either container. Each item contains a stable unique `key`, composable `label`
and `icon`, and `enabled` (default `true`). Selection and navigation remain
caller-controlled; a null `selectedKey` leaves all destinations unselected.
Items are composed by key, so reordering retains their slot state within the
same container. Switching between side and bottom navigation does not transfer
item-local remembered state; hoist shared state above both containers.

```kotlin
val items = remember {
    listOf(
        StyledNavigationItem(
            key = "home",
            label = { StyledText("Home") },
            icon = { StyledIcon(icon = Icons.Default.Home.toIconComposable(), contentDescription = null) },
        ),
        StyledNavigationItem(
            key = "settings",
            label = { StyledText("Settings") },
            icon = { StyledIcon(icon = Icons.Default.Settings.toIconComposable(), contentDescription = null) },
        ),
    )
}
var selectedKey by remember { mutableStateOf("home") }

StyledScaffold(
    sideNavigation = {
        if (useSideNavigation) {
            StyledSideNavigation(
                items = items,
                selectedKey = selectedKey,
                onItemClick = { selectedKey = it.key },
            )
        }
    },
    bottomBar = {
        if (!useSideNavigation) {
            StyledBottomNavigationBar(
                items = items,
                selectedKey = selectedKey,
                onItemClick = { selectedKey = it.key },
            )
        }
    },
) { innerPadding ->
    StyledText("Content", Modifier.padding(innerPadding))
}
```

Both list APIs support container colors and optional scrolling. Bottom navigation
accepts window insets and `alwaysShowLabel`. Side navigation has modes,
`compactLabelVisibility`, an optional title, and expanded-only fixed header/footer.
Items do not expose individual colors. Configure the selected and unselected
item content and indicator colors on `StyledBottomNavigationBar` or
`StyledSideNavigation` with their `customization` parameter. This keeps the
palette consistent across destinations and lets each theme renderer apply it
to its native or shared presentation. If omitted, renderer defaults are used.
