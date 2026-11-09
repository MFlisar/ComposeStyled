# List item

`StyledListItem` displays leading content, a main text/content area and independent
trailing actions. It has its own registered renderer in Cupertino, Fluent 2 and
Material 3; replacing a Card or Button renderer does not change list items.

## Semantic text and line patterns

Name `headline` to use semantic slots. Each theme supplies typography and content
colors for `headline`, optional `supporting` text below it and optional `overline`
above it. `leading` can contain a `StyledIcon`, avatar or other composable content.

```kotlin
StyledListItem(
    headline = { StyledText("Downloads") },
    supporting = { StyledText("Files available offline") },
    leading = {
        StyledIcon(
            icon = Icons.Default.Info.toIconComposable(),
            contentDescription = null,
        )
    },
    onClick = { openDownloads() },
)
```

Headline only is the one-line pattern. Headline plus either supporting or overline
is the two-line pattern; all three slots form the three-line pattern. These are
minimum-height patterns, not forced line counts: caller text may wrap and larger
fonts or taller content may increase the height. No separate `SingleLine`,
`TwoLine` or `ThreeLine` functions or line-count enum are needed.

Material 3 uses its native `ListItem` layout for semantic main-area content with
default padding and spacing. It also recognizes two-line supporting text as the three-line
pattern and supplies the corresponding native alignment and minimum height.
Its native one-/two-/three-line minimum heights are 56/72/88 dp. Cupertino uses
44/60/76 dp and Fluent 2 uses 40/56/72 dp, with their own text styles.

## Free content

Use the trailing `ColumnScope` lambda for arbitrary content. This overload uses
the same renderer, color parameters and click behavior, but does not infer text
roles or line patterns from its children.

```kotlin
StyledListItem(onClick = { openDetails() }) {
    StyledText("Custom heading", style = StyledTheme.typography.titleSmall)
    StyledText("Any additional content")
}
```

Both overloads fill the available width. Leading content and actions retain their
measured widths; the main content takes the remaining space. Put list items in a
width-constrained container; long text then wraps in that remaining space.

## Interaction and actions

`onClick` defaults to `null`. Only a non-null callback adds item interaction,
keyboard activation and click semantics. The click target and its interaction
feedback cover the whole item, including leading content and unused space in the
actions region, clipped to the item's outer `shape`. There is no forced button
appearance or selection state.

`actions` is a `RowScope` slot whose interactive controls own their callbacks and
enabled states. Clicking an action control does not also invoke the item's
callback. Blank space around those controls invokes the item callback when it is
present and the item is enabled.

```kotlin
StyledListItem(
    headline = { StyledText("A file") },
    onClick = { openFile() },
    actions = {
        StyledIconButton(
            onClick = { showFileMenu() },
            variant = StyledButton.Variant.Plain,
            icon = Icons.Default.MoreVert.toIconComposable(),
            contentDescription = "File actions",
        )
    },
)
```

`enabled = false` disables the item click and dims the main content, not the
actions. Pass their enabled state explicitly when they should also be disabled.
`interactionSource` belongs to the whole item's interaction. Keep interactive
children in `actions`, not in the main content.

## Defaults and colors

Items are flat and borderless by default, without elevation or Card variants.
Adjust `shape`, `minHeight`, `contentPadding` and `spacing` through parameters or
registered defaults. `minHeight` is an additional lower bound for either
overload, not a fixed height or a line-pattern selector. Semantic items retain
their theme's pattern minimums, and native Material items cannot shrink below
their native minimums.

Set `containerColor`, `contentColor`, `supportingColor` and `borderColor`
directly. Each defaults to `Color.Unspecified`, so the renderer keeps its theme
default when no override is supplied. If `contentColor` is set and
`supportingColor` is not, supporting and overline content use `contentColor`.
`Color.Unspecified` for `borderColor` leaves the item borderless.

```kotlin
StyledListItem(
    headline = { StyledText("Highlighted item") },
    containerColor = StyledTheme.colorScheme.primarySurface,
    contentColor = StyledTheme.colorScheme.onPrimarySurface,
    borderColor = StyledTheme.colorScheme.outline,
)
```

Material 3's native API has fixed padding and slot spacing. With custom
`contentPadding` or `spacing`, its renderer uses the shared layout with Material
typography instead; the semantic slot-based minimum heights still apply, but
native baseline positioning and wrap-based three-line classification do not.
The independent actions region uses the configured padding in both layouts.

Theme-wide defaults use `StyledTheme(customization = { ... })`:

```kotlin
update(StyledListItem.DefaultsKey) {
    copy(spacing = 20.dp, minHeight = 64.dp)
}
```

Replace the `StyledListItem.Key` implementation through `register(...)` for custom
rendering of both overloads. Register before updating its defaults. Use
`UpdateStyledRegistry` for a scoped replacement without changing Cards, Buttons
or surrounding list items.
