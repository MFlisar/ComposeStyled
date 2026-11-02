# Split Panel

`StyledSplitPanel` displays two content panes separated by a draggable divider.
The divider line is interrupted around three centered grip dots aligned with
the divider; no background track is drawn behind them. It is a shared layout
and works with every theme without a registry entry.

```kotlin
val splitState = rememberStyledSplitPanelState(initialFirstPaneSize = 280.dp)

StyledSplitPanel(
    modifier = Modifier.fillMaxSize(),
    state = splitState,
    firstPaneMinSize = 180.dp,
    firstPaneMaxSize = 420.dp,
    secondPaneMinSize = 240.dp,
    firstPaneContent = { StyledText("Navigation") },
    secondPaneContent = { StyledText("Details") },
)
```

The orientation uses `androidx.compose.foundation.gestures.Orientation`.
The default is `Orientation.Horizontal`, which
places the panes side by side. Use `Orientation.Vertical` to
stack them. The layout must have a bounded width for a horizontal split or a
bounded height for a vertical split.

`StyledSplitPanelState.firstPaneSize` is the requested size of the first pane
along the split axis. Dragging updates it, and callers can also change it
directly. The default state starts at `240.dp`; pass a state created with
`rememberStyledSplitPanelState(initialFirstPaneSize = ...)` to choose another
starting position or to retain the state across recompositions and supported
host recreation. The saved first-pane size is in `Dp`; the rendered size is
still clamped to the available space and pane constraints.

Without explicit size limits, the first pane starts at `240.dp` and the second
pane receives the remaining width or height after subtracting the handle's
`24.dp` hit area. This is not a 50/50 or content-based split. For example, a
horizontal layout that is `800.dp` wide starts with panes of `240.dp` and
`536.dp`. If the available pane space is smaller than the requested first-pane
size, the first pane is clamped to that space and the second pane can shrink to
zero.

```kotlin
splitState.firstPaneSize = 320.dp
```

Set `firstPaneMinSize`, `firstPaneMaxSize`, `secondPaneMinSize` and
`secondPaneMaxSize` to constrain each pane in `Dp`. Maximum sizes default to
`Dp.Infinity`. When both minimum sizes cannot fit in the available pane space,
the layout divides that space in proportion to the two minimum sizes. If both
maximum sizes are smaller than the available space, the remaining space is left
unused. If the requested state is outside the current constraints, the rendered
size is clamped; resizing or assigning a new state updates the stored size.

The draggable handle hit area defaults to `24.dp`. The interrupted divider and
grip dots use the active theme's `outlineVariant` color. `dividerThickness`
controls the dot size and defaults to `4.dp`; the divider line itself is `1.dp`
thick and stops before the outer grip dots with the same visible gap as the gap
between adjacent dots. On JVM desktop, Android and web, hovering or dragging
the divider shows a horizontal or vertical resize cursor matching the panel
orientation. The cursor stays active throughout the drag, even if the pointer
moves ahead of or outside the divider. iOS and native macOS currently use the
platform's default cursor.
