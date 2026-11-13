# Sheet

`StyledSheet` is a modal container that stays hidden until expanded. It can be
opened by dragging inward from a narrow region at any of the four parent edges.
It is a generic container, not a navigation drawer; use
[side navigation](SideNavigation.md) for persistent compact or expanded destinations.

Place it last in a bounded `Box` so it draws above the content:

```kotlin
val sheetState = rememberStyledSheetState()

Box(Modifier.fillMaxSize()) {
    MainContent()
    StyledSheet(
        state = sheetState,
        edge = StyledSheet.Edge.Start,
        expandedSize = StyledSheet.Size.Fraction(0.8f),
        triggerSize = 24.dp,
    ) {
        StyledColumn(Modifier.fillMaxSize().padding(16.dp)) {
            StyledText("Sheet content")
            StyledButton(onClick = { sheetState.collapse() }) {
                StyledText("Close")
            }
        }
    }
}
```

`rememberStyledSheetState()` remembers saveable state and starts collapsed.
Call `expand()`, `collapse()` or `set(StyledSheet.Value)` to control it;
`currentValue` exposes its expanded/collapsed value and `progress` exposes the
animated position from `0f` (collapsed) to `1f` (expanded). `Start` and `End`
are logical and follow the layout direction; `Top` and `Bottom` are physical
edges. A scrim appears while expanded; tapping it or pressing Back/Escape
closes the sheet.

## Size and edge gestures

`expandedSize` accepts either a fixed dp size or a fraction of the available
axis size. For `Start` and `End`, this is the parent width; for `Top` and
`Bottom`, it is the parent height:

```kotlin
edge = StyledSheet.Edge.Top
expandedSize = StyledSheet.Size.Fixed(320.dp)
expandedSize = StyledSheet.Size.Fraction(0.8f)
```

Fractions must be greater than zero and at most `1f`; fixed sizes must be
finite and positive. The sheet is clamped to the available size.

`triggerSize` defaults to `24.dp`. It is an invisible hit region along the
selected parent edge, not part of the sheet's visible content: full height on
the side edges and full width on the top and bottom edges. Set it to `0.dp`, or
set `gesturesEnabled = false`, to disable edge opening. While open, the sheet
can be dragged toward its edge to close when gestures are enabled; tapping the
scrim remains available regardless of gesture settings.

While the sheet is fully collapsed, clicks and hover in the trigger region also
reach the content underneath it. A drag inward still opens the sheet. The
expanded sheet and its scrim remain modal.

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
StyledSheet(
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
