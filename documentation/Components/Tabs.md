# Tabs

`StyledTabRow` and `StyledTab` provide themed tab navigation. Choose a primary
or secondary row, with either fixed or horizontally scrollable tabs. Selection
is controlled by the caller, and the `text` slot can be any composable:

```kotlin
val selectedTab = remember { mutableStateOf(0) }
val tabs = listOf("Overview", "Details")

StyledPrimaryTabRow(selectedTabIndex = selectedTab.value) {
    tabs.forEachIndexed { index, title ->
        StyledTab(
            selected = selectedTab.value == index,
            onClick = { selectedTab.value = index },
            text = { StyledText(title) },
        )
    }
}

StyledSecondaryTabRow(selectedTabIndex = selectedTab.value) {
    tabs.forEachIndexed { index, title ->
        StyledTab(
            selected = selectedTab.value == index,
            onClick = { selectedTab.value = index },
            text = { StyledText(title) },
        )
    }
}

// For tabs that exceed the available width:
val tabScrollState = rememberScrollState()
StyledPrimaryScrollableTabRow(
    selectedTabIndex = selectedTab.value,
    scrollState = tabScrollState,
    edgePadding = 24.dp,
    minTabWidth = 96.dp,
    divider = { StyledHorizontalDivider() },
) {
    tabs.forEachIndexed { index, title ->
        StyledTab(
            selected = selectedTab.value == index,
            onClick = { selectedTab.value = index },
            text = { StyledText(title) },
        )
    }
}
// Use StyledSecondaryScrollableTabRow for the scrollable secondary variant;
// it accepts the same scroll, width, padding, and divider parameters.
```

Fixed rows expose the `divider` slot. Scrollable rows also accept a caller-owned
`ScrollState`, `edgePadding`, and `minTabWidth`; their defaults are provided by
`StyledTabRowDefaults`. Primary rows default to `primarySurface` /
`onPrimarySurface`, while secondary rows use `secondarySurface` /
`onSecondarySurface`.

Scrollable tab rows display a scrollbar indicator when their contents overflow
and `StyledTheme.behavior.showScrollbars` is enabled. Disabling that global
setting hides the indicator but leaves tab scrolling and selected-tab
auto-scrolling unchanged.

`StyledPager` uses these components for its tab row, so theme overrides affect
both standalone tabs and pager navigation.

Tabs inside a row inherit its selection palette. With the default row colors,
the selected tab and indicator use the primary color, while unselected tabs use
the row's content color. With custom row colors, the selected tab and indicator
use the row's content color and unselected tabs use a subdued version. Explicit
`selectedContentColor` and `unselectedContentColor` values on `StyledTab`
override the inherited colors; standalone tabs use their renderer's defaults.
