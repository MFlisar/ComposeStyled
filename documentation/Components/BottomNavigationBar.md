# Bottom navigation bar

`StyledBottomNavigationBar` replaces the former toolbar-style `StyledBottomBar`.
  Use `RowScope.StyledBottomNavigationBarItem` for equally weighted destinations
  with `selected`, `onClick`, icon, optional label, `enabled` and `alwaysShowLabel`.
  The caller owns the selected destination and navigation. Items also accept an
  interaction source. Item foregrounds inherit the bar's `contentColor`; the
  selected appearance is controlled by the bar's `indicatorTint`, not by
  per-item color parameters. Labels are horizontally centered within each item.
  Supply an accessible icon description when omitting a label.
  Theme factories expose `bottomNavigationBar` instead of `bottomBar`; Scaffold's
  generic `bottomBar` slot keeps its name.

Material3 uses Compose Material3 `NavigationBar` and `NavigationBarItem` for bottom
navigation, not `BottomAppBar`, and keeps their native vertical spacing.
Its item row has a small horizontal content inset, while the bar background
remains full width.
Material3 renders the selection tint as a subtle filled indicator and applies
the tint to the selected icon. Fluent2 renders it as a short indicator below
the item content, animating its width horizontally on selection changes.
Cupertino has no separate indicator and uses the tint for the
selected icon and label. Unselected items use the bar's `contentColor`.
Cupertino avoids additional outer vertical content padding for its compact tab
bar. Foundation-based themes support disabled destinations and keyboard focus.

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

`indicatorTint` defaults to `StyledTheme.colorScheme.primary`. It represents the
selection accent; each renderer adapts it to its design rather than using the
same rendered color value. Material3 uses a translucent indicator background
and selected-icon tint, Fluent2 uses a horizontally animated indicator, and Cupertino uses a
selected-content tint without a separate indicator. The default `contentColor`
continues to follow `containerColor`.

Set `indicatorTint` to use a different selection accent. For example, the
primary-surface bar below keeps its foreground colors derived from that surface,
while the selection tint remains independently configurable:

```kotlin
StyledBottomNavigationBar(
    containerColor = StyledTheme.colorScheme.primarySurface,
    contentColor = StyledTheme.colorScheme.onPrimarySurface,
    indicatorTint = StyledTheme.colorScheme.primary,
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

The selection tint does not replace the bar's content color: labels and
unselected icons still inherit `contentColor`, while renderers apply the tint
according to their selection indicator. Side navigation uses the same
`indicatorTint` API, adapted to its pill, start-side stroke or selected-row
background. Disabled items retain each renderer's disabled styling.

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
