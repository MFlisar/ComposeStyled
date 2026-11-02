# Collapsible Header

`StyledCollapsibleHeader` is a theme-neutral layout that places a collapsible
header and an optional sticky header above content. It handles scroll input
itself, so its content can be scrollable or static. It does not need a theme
renderer or registry entry.

```kotlin
val headerState = rememberStyledCollapsibleHeaderState()

StyledCollapsibleHeader(
    modifier = Modifier.fillMaxSize(),
    state = headerState,
    header = { StyledText("Large header") },
    stickyHeader = { StyledText("Pinned while scrolling") },
) {
    StyledLazyColumn {
        items(messages) { message ->
            StyledText(message)
        }
    }
}
```

Scrollable children, such as `StyledLazyColumn`, participate in nested scrolling:
the default `ExitUntilCollapsed` behavior collapses the header before the content
scrolls and expands it when downward scrolling reaches the content's start.
`EnterAlways` expands the header as soon as the user scrolls down, even while the
content is still scrolled. With static content, scroll input controls the header
directly because there is no scrollable child to consume it.

`StyledCollapsibleHeaderState.collapsedFraction` is `0f` when expanded and `1f`
when fully collapsed. It is saveable across recreation. Call the suspend
functions `collapse()` or `expand()` to animate it programmatically:

```kotlin
scope.launch {
    headerState.collapse()
}
```

Both functions animate over 250 ms. A user scroll interrupts the current
programmatic animation and takes over the state.
