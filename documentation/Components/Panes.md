# Panes and Translatable Content

`StyledPaneContainer` lays out optional collapsible panes on the left and right
of a main content area. It is a shared composition that uses Styled controls
and theme tokens, so it works with every theme without a registry entry.

```kotlin
val navigationPane = rememberStyledSidePane("Navigation") {
    StyledText("Navigation items")
}
val detailsPane = rememberStyledSidePane("Details", expanded = true) {
    StyledText("Selected item details")
}

StyledPaneContainer(
    modifier = Modifier.fillMaxSize(),
    left = navigationPane,
    right = detailsPane,
) {
    StyledText("Main content")
}
```

Each pane is optional and its width is capped by the corresponding
`maxWidthLeft` or `maxWidthRight` value (both default to
`StyledPaneWidthLimit.Percentage(20f)`, or `20%` of the container width).
Use `StyledPaneWidthLimit.Fixed(200.dp)` to cap a pane at a fixed width instead.
`rememberStyledSidePane` provides the pane's
mutable expanded state; set `pane.expanded.value` to control it programmatically.
The expanded/collapsed choice is saved across supported host recreation.
Collapsed panes can be reopened from their vertical label. The pane header's
icon button and divider use the active Styled theme. Pass an `icon` slot to
`rememberStyledSidePane` to customize that button; its default is the semantic
`StyledIcons.Menu` icon rendered through `StyledIcon`.
The header reserves the theme's minimum interactive height, while the icon
button keeps its native theme size. The title stays on one line and truncates
with an ellipsis when it does not fit.
The slot is composed in `StyledSidePane.Context.IconSlot`. Buttons with the
default role use the theme's text/transparent button style, while icon buttons
use their standard icon-button style. Explicit roles are not changed.

```kotlin
val navigationPane = rememberStyledSidePane(
    label = "Navigation",
    icon = {
        StyledText("N")
    },
) {
    StyledText("Navigation items")
}
```

For a pane outside `StyledPaneContainer`, use `StyledPaneSide` directly and
provide a `StyledPaneSide.Side`, label, and `MutableState<Boolean>`. Its `icon`
slot also defaults to `StyledIcons.Menu` rendered through `StyledIcon`. Set
`collapsible = false` to make the side non-collapsible, or
`showLabelWhenCollapsed = false` to omit the collapsed vertical label.

## Window insets

Both `StyledPaneContainer` and `StyledPaneSide` accept `windowInsets`, defaulting
to zero. Opt into `WindowInsets.safeDrawing` when panes reach the window edges:

```kotlin
StyledPaneContainer(
    modifier = Modifier.fillMaxSize(),
    left = navigationPane,
    windowInsets = WindowInsets.safeDrawing,
) {
    StyledScaffold { padding ->
        Box(Modifier.padding(padding).consumeWindowInsets(padding)) {
            StyledText("Main content")
        }
    }
}
```

The container protects each pane's top, bottom and outer horizontal edge
(start for `left`, end for `right`, following layout direction). Padding covers
the header icon and title, the expanded content and the collapsed label;
the divider remains full-height. Insets count toward `maxWidthLeft` and
`maxWidthRight`, leaving less usable content width when system padding is needed.
Standalone `StyledPaneSide` applies all sides of its supplied `windowInsets`.

Inset padding is consumed only within the corresponding pane subtree and
respects insets already consumed by ancestors. The main content is a sibling:
it receives no additional padding and retains responsibility for handling its
own insets, for example through `StyledScaffold`. The container recalculates
consumption at the main area's actual bounds, so a pane's width already
separating it from a window edge does not produce redundant horizontal
system padding. This geometry recalculation also applies with default pane
insets; it does not add padding or consume top and bottom insets when the main
area spans the full window height.

## Translatable content

`StyledTranslatableContent` clips a content slot while scaling its visible size
along one axis. `translation = 0f` hides it, and `translation = 1f` displays
the full measured size. `Side.Left` and `Side.Right` affect horizontal size;
`Side.Top` and `Side.Bottom` affect vertical size. Use the slot scope's
`Modifier.stretch(true)` to fill the perpendicular axis.

```kotlin
StyledTranslatableContent(
    side = StyledTranslatableContent.Side.Left,
    translation = 0.5f,
) {
    StyledText(
        modifier = Modifier.stretch(true),
        text = "Half-width content",
    )
}
```
