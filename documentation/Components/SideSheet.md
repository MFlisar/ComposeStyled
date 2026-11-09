# Side sheet

`StyledSideSheet` is a modal container that stays hidden until expanded. It can
be opened by dragging inward from a narrow region at the selected parent edge.
It is a generic container, not a navigation drawer; use
[side navigation](SideNavigation.md) for persistent compact or expanded
destinations.

Place it last in a bounded `Box` so it draws above the content:

```kotlin
val expanded = rememberSaveable { mutableStateOf(false) }

Box(Modifier.fillMaxSize()) {
    MainContent()
    StyledSideSheet(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it },
        side = StyledSideSheet.Side.Start,
        expandedWidth = StyledSideSheet.Width.Fraction(0.8f),
        triggerWidth = 24.dp,
    ) {
        StyledColumn(Modifier.fillMaxSize().padding(16.dp)) {
            StyledText("Sheet content")
            StyledButton(onClick = { expanded.value = false }) {
                StyledText("Close")
            }
        }
    }
}
```

The `MutableState<Boolean>` overload remembers saveable state by default and
starts collapsed. Use the controlled overload to open it from another control
or to coordinate its state. `side` is logical: `Start` follows the layout
direction and `End` is its opposite. A scrim appears while expanded; tapping it
or pressing Back/Escape closes the sheet.

## Width and edge gestures

`expandedWidth` accepts either a fixed dp width or a fraction of the available
parent width:

```kotlin
expandedWidth = StyledSideSheet.Width.Fixed(320.dp)
expandedWidth = StyledSideSheet.Width.Fraction(0.8f)
```

Fractions must be greater than zero and at most `1f`; fixed widths must be
finite and positive. The sheet is clamped to the available width.

`triggerWidth` defaults to `24.dp`. It is an invisible, full-height hit region
at the selected parent edge, not part of the sheet's visible content. Set it to
`0.dp`, or set `gesturesEnabled = false`, to disable edge opening. While open,
the sheet can be dragged toward the edge to close when gestures are enabled;
tapping the scrim remains available regardless of gesture settings.

On release, a slow drag settles according to whether the sheet crossed halfway.
A fling at or above `400.dp/s` settles in its fling direction, matching the
Material 3 navigation drawer threshold.

The parent must provide bounded width and height. The component does not apply
system-bar insets to its content; apply the insets appropriate to the host.

## Appearance

The sheet is a shared layout composition and reuses the active
`StyledSurface` renderer. Its `shape` defaults to `StyledSurfaceDefaults.shape`,
its `containerColor` to `StyledTheme.colorScheme.surface`, and its content color
is derived from that container. Customize appearance directly:

```kotlin
StyledSideSheet(
    shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
    containerColor = StyledTheme.colorScheme.surfaceVariant,
    shadowElevation = 12.dp,
) {
    // ...
}
```

The default scrim is black at 32% opacity in every theme, matching Material 3's
scrim behavior. It is independent of `contentColor`; override `scrimColor` when
a different modal overlay is needed.

All themes share the same edge-drag and dismissal behavior. Their registered
`StyledSurface` renderer continues to control the panel's surface treatment.
