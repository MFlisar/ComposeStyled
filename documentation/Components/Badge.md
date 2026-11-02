# Badge

`StyledBadge` overlays a badge slot on a content slot. The badge can contain
arbitrary composables, such as a number, while the content slot typically holds
an icon or control. The default position is `Alignment.TopEnd`; use `alignment`
to choose a different position.

The badge's **center** sits on the chosen alignment point of the content:
`TopEnd` centers it on the top-end corner, `BottomStart` on the bottom-start
corner, and `Center` on the content's center. Start/end positions follow the
layout direction, including RTL.

Only the content determines the layout size and text baselines. The badge is
drawn on top and can extend outside those bounds; changing its label does not
reserve more layout space. Use the outer `modifier` to provide clearance from
neighbors or clipping ancestors. Padding on this modifier surrounds the layout
without changing the badge's position relative to its content. It does not
automatically reserve space based on the badge's dimensions.

Use `offset: DpOffset` (default `DpOffset.Zero`) to fine-tune the badge's position
after alignment. Positive x moves toward end (right in LTR, left in RTL);
positive y moves downward. The offset affects only the badge, not the content,
layout size or text baselines. Reserve any additional clearance using `modifier`.

```kotlin
StyledBadge(
    badge = { StyledText("3") },
    content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
    modifier = Modifier.padding(12.dp),
    offset = DpOffset(2.dp, (-2).dp),
)
```

A `null` badge slot renders a dot. An empty non-null lambda still selects a
content badge, not a dot:

```kotlin
StyledBadge(
    badge = null,
    content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
    alignment = Alignment.BottomEnd,
)
```

The component does not format numeric values or impose a maximum count. Provide
the desired label through `badge` and select the position with `alignment`.

## Badge-specific sizing

`StyledBadgeDefaults` provides `minSize`, `dotSize`, `textStyle`, `iconSize`
and `contentPadding`. The built-in themes default to a 16 dp minimum content
badge, a 6 dp dot, 11 sp text with a 16 sp line height, 10 dp icons and 4 dp
horizontal padding. Font family and weight come from the theme's `labelSmall`;
badge font size and line height are independent of its general text sizes.

The badge can grow for longer text, larger content and system font scaling;
`minSize` is not a fixed height and text is not automatically shrunk to fit.
Individual calls can override `minSize`, `dotSize`, `textStyle` and
`contentPadding`.

Within the `badge` slot, `StyledIconDefaults.size` resolves to
`StyledBadgeDefaults.iconSize` through `StyledBadge.Context.BadgeContent`.
The anchor's `content` slot and surrounding icons keep their existing context.
Explicit `StyledIcon(size = ...)` overrides the contextual default; `size = null`
still omits the automatic size modifier.

```kotlin
StyledBadge(
    badge = { StyledIcon(icon = Icons.Default.Add.toIconComposable()) },
    content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
)
```

Configure defaults for a subtree with `UpdateStyledRegistry`, or use the same
`update` inside `StyledTheme(customization = { ... })`:

```kotlin
UpdateStyledRegistry(
    update = {
        update(StyledBadge.DefaultsKey) {
            copy(
                minSize = 20.dp,
                iconSize = 12.dp,
                textStyle = textStyle.copy(fontSize = 12.sp, lineHeight = 18.sp),
            )
        }
    },
) {
    StyledBadge(
        badge = { StyledText("99+") },
        content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
    )
}
```
