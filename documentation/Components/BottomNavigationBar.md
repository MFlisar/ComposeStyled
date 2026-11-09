# Bottom navigation bar

`StyledBottomNavigationBar` replaces the former toolbar-style `StyledBottomBar`.
  Use `RowScope.StyledBottomNavigationBarItem` for equally weighted destinations
  with `selected`, `onClick`, icon, optional label, `enabled` and `alwaysShowLabel`.
  The caller owns the selected destination and navigation. Items also accept an
  interaction source; their colors are configured together on the container,
  not individually on each item. Labels are horizontally centered within each
  item.
  Supply an accessible icon description when omitting a label.
  Theme factories expose `bottomNavigationBar` instead of `bottomBar`; Scaffold's
  generic `bottomBar` slot keeps its name.

Material3 uses Compose Material3 `NavigationBar` and `NavigationBarItem` for bottom
navigation, not `BottomAppBar`, and keeps their native vertical spacing.
Its item row has a small horizontal content inset, while the bar background
remains full width.
Cupertino uses tint-based tab selection without a filled indicator and avoids
additional outer vertical content padding for its compact tab bar. Fluent2 uses
a filled selected-icon indicator and retains small theme-based vertical content
padding. Both Foundation-based themes support disabled destinations and keyboard
focus.

Bottom bars handle their safe-area insets themselves. Pass
`WindowInsets(0, 0, 0, 0)` when embedding a bar inside other content.

System-bar styling is caller-directed. From the containing Scaffold, call
`SystemBarStyleEffectIfNeeded` with this bar's `containerColor` as
`navigationBarColor`; pass the actual Scaffold container as `backgroundColor`
when it differs from the theme surface. If the bar has an explicit
`contentColor`, pass it as `navigationBarContentColor` as well. The library
leaves the iOS home indicator platform controlled.

Icon and label slots receive their own content-color scope. Nested `StyledIcon`,
`StyledText` and other Styled content inherit the foreground of their slot,
not a shared foreground for the entire item.

Without customization, Material3 and Fluent2 use the bar's `contentColor` for
labels and unselected icons. The selected icon uses the theme's content color
for the selected indicator (`primarySurface` by default). Cupertino has no
filled indicator and retains its `primary` selection tint for both the selected
icon and label; unselected content uses the bar's `contentColor`.

For a custom container, use `StyledBottomNavigationBarDefaults.customize` to
configure selected and unselected icon and text colors independently, together
with the selection indicator. Explicit colors override the theme defaults.
For example, on a `primary` container, use `onPrimarySurface` for the selected
icon on its `primarySurface` indicator, but `onPrimary` for labels and unselected
icons directly on the bar:

```kotlin
StyledBottomNavigationBar(
    containerColor = StyledTheme.colorScheme.primary,
    contentColor = StyledTheme.colorScheme.onPrimary,
    customization = StyledBottomNavigationBarDefaults.customize(
        selectedItemIconColor = ColorToken.Theme.OnPrimarySurface,
        selectedItemTextColor = ColorToken.Theme.OnPrimary,
        unselectedItemIconColor = ColorToken.Theme.OnPrimary,
        unselectedItemTextColor = ColorToken.Theme.OnPrimary,
        selectedItemIndicatorColor = ColorToken.Theme.PrimarySurface,
    ),
) {
    StyledBottomNavigationBarItem(
        selected = true,
        onClick = {},
        label = { StyledText("Home") },
    ) {
        StyledText("H")
    }
}
```

Both `ColorToken` and `Color` overloads are available. `selectedItemIconColor`
is required when calling `customize`; other fields are optional and retain their
individual theme defaults. For an indicator color outside the theme's known
background colors, supply its selected icon foreground explicitly.

This replaces the former `selectedItemContentColor` and
`unselectedItemContentColor` customization parameters. Migrate each to the
corresponding icon and text parameters; the two foregrounds need not be equal.
Side navigation's customization API is unchanged. Disabled items retain each
renderer's disabled styling.

## Optional horizontal scrolling

The default `scrollable = false` retains equally weighted destinations.
Set `scrollable = true` for a horizontally scrollable row of finite-width
destinations. This is an explicit extension, not the usual bottom-navigation
pattern for many destinations. It does not introduce headers or footers.

```kotlin
StyledBottomNavigationBar(
    items = destinations,
    selectedKey = selectedKey,
    onItemClick = { selectedKey = it.key },
    scrollable = true,
)
```

Both the list and free-content overloads accept `scrollState` for position
control and `showScrollbar` independently of scrolling. The scrollbar defaults
to the desktop/web preference; hiding it does not disable scrolling.
Safe-area insets still apply to the entire container. Material3 keeps native
`NavigationBarItem` controls, measured in finite-width hosts so their internal
weight does not collapse in an unbounded scroll row.
