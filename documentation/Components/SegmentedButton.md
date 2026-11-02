`StyledSegmentedButton` represents one segment. `StyledSegmentedButtonRow` and
`StyledSegmentedButtonRowMultiSelect` are the convenient item-list APIs for
single- and multi-selection. Both rows use the same item content lambda
`(item, selected) -> Unit`, independent of dropdown render contexts. Use
`StyledSegmentedButtonRowDefaults.defaultRenderer()` for a text label, or supply
a custom renderer when a segment needs additional content such as an icon.
All segmented-button APIs accept `showSelectedIcon`, which defaults to `true`.
Set it to `false` to hide the built-in selected-state check icon without
affecting selected semantics or caller-provided content.

The row APIs support selected values or indices, stateful and controlled
overloads, enabled state, item shape, item content padding, minimum item width
and original-item index callbacks. No manually composed-segment overload is
needed on the rows. `itemShape` controls each segment's shape within the row;
`itemContentPadding` applies inside each segment. Each theme ships fixed
`StyledSegmentedButton.Defaults` and `StyledSegmentedButtonRow.Defaults`
(`StyledSegmentedButton.DefaultsKey` / `StyledSegmentedButtonRow.DefaultsKey`), adjustable via
`StyledTheme(customization = { update(StyledSegmentedButtonRow.DefaultsKey) { copy(...) } })`.
`minItemWidth` defaults to
the M3 minimum width (58 dp). Segments use equal weights; without an explicit
fill/fixed width, the row measures intrinsically while respecting that minimum.
Use `Modifier.fillMaxWidth()` to make segments divide the full available width.
Every segment in a row automatically matches the height of the tallest segment,
so wrapped labels share a consistent row height. This uses intrinsic content
measurement rather than a text-line-count setting; custom content must support
intrinsic measurement. The default text renderer wraps long labels instead of
clipping them.
Foundation-based themes reserve the check-indicator slot in both selection
states and animate the label from its centered inactive position to beside the
check, so selecting an item does not resize the segment. When
`showSelectedIcon` is `false`, the indicator slot and label animation are
disabled.

```kotlin
val items = listOf("List", "Grid", "Gallery")
val content = StyledSegmentedButtonRowDefaults.defaultRenderer<String>()

StyledSegmentedButtonRow(
    items = items,
    selected = selected,
    minItemWidth = 58.dp,
    content = content,
)
StyledSegmentedButtonRowMultiSelect(
    items = items,
    selected = selectedItems,
    minItemWidth = 58.dp,
    content = content,
)

StyledSegmentedButton(selected = isSelected, onClick = { isSelected = !isSelected }) {
    StyledText("One segment")
}
```

Material 3 delegates to its native single- and multi-choice segmented-button
rows through one registered row renderer, which composes the registered
`StyledSegmentedButton` for each item. It uses native item shapes, colors, check
indicators and item content padding by default. Cupertino and Fluent 2 use the
shared Foundation item and row renderers with Styled theme tokens; their selected
segments use `secondarySurface` and `onSecondarySurface`, matching Material 3's
`secondaryContainer` and `onSecondaryContainer` defaults. An
out-of-range index displays no single selection; set `forceSelection = false`
to clear the active segment when clicked again.
