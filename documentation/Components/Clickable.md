# Clickable

`StyledClickable` makes custom content act as a button while retaining the
active theme's click feedback. It applies the theme's button shape and content
padding by default, but does not impose a minimum width, so short labels stay
compact in places such as breadcrumbs.

```kotlin
StyledClickable(onClick = { navigateBack() }) {
    StyledText("Settings")
}
```

The content is rendered in a `RowScope` and keeps its inherited text and content
colors; `StyledClickable` does not provide custom content colors or text styles.
Its `shape` and `contentPadding` can be overridden per instance. The active
theme retains its button-like minimum height, click feedback and keyboard
activation. It uses button semantics and does not provide custom content colors
or text styles. Set `enabled = false` to disable interaction, or pass an
`interactionSource` to observe the interaction state.

Use `StyledButton` when the action needs a button variant or button-specific
color customization. Use `StyledClickable` when the caller provides the
complete content and only needs the theme's click treatment.
