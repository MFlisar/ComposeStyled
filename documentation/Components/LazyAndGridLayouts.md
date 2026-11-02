# Row, column, lazy and grid layouts

`StyledRow` and `StyledColumn` default to no spacing and accept explicit
`horizontalArrangement` or `verticalArrangement`. Their spacing overloads
require `itemSpacing` and also accept `horizontalAlignment` (row) or
`verticalAlignment` (column). Spacing and arrangement are alternative ways to
configure the same layout, not combined parameters.

```kotlin
StyledRow(itemSpacing = StyledTheme.spacing.medium) {
    StyledText("First")
    StyledText("Second")
}

StyledColumn(itemSpacing = StyledTheme.spacing.small) {
    StyledText("Title")
    StyledText("Description")
}

StyledRow(horizontalArrangement = Arrangement.SpaceBetween) {
    StyledText("Start")
    StyledText("End")
}
```

The former `StyledSpacedRow` and `StyledSpacedColumn` helpers are replaced by
these overloads. To preserve their default spacing, pass
`itemSpacing = StyledTheme.spacing.medium` explicitly.

`StyledFlowRow` has a `spacing` overload that applies one value to both
horizontal item gaps and vertical row gaps. Its `itemSpacing`/`rowSpacing`
overload supports different values when needed. The lazy list spacing overloads
require `itemSpacing`; the lazy grid overloads require `verticalItemSpacing` and
`horizontalItemSpacing`. These are alternative forms of the same layout APIs:
use the arrangement parameters when you need custom distribution, or the
spacing parameters when you want `Arrangement.spacedBy`.
The former `StyledSpacedFlowRow` and `StyledSpacedLazy*` helpers are replaced
by these overloads.

For a fixed, non-lazy grid, use `StyledGrid`. Its cell slot receives each cell's
row and column indices. Rows and columns divide bounded height and width
evenly; in an unbounded axis, Compose's normal weight behavior lets the grid
size naturally to its content. `spacing` controls the gaps between cells, and
`contentPadding` controls the outer inset. Both default to
`StyledTheme.spacing.medium`.

```kotlin
StyledGrid(
    columns = 3,
    rows = 2,
    modifier = Modifier.fillMaxWidth().height(180.dp),
    spacing = StyledTheme.spacing.medium,
    contentPadding = PaddingValues(StyledTheme.spacing.small),
) { row, column ->
    StyledText("Cell $row, $column")
}
```

`StyledFlowRowDefaults` exposes its default arrangements and alignments.
`StyledLazyRow` and `StyledLazyColumn` reuse `StyledRowDefaults` and
`StyledColumnDefaults` for their corresponding arrangement and alignment values.

```kotlin
StyledFlowRow(spacing = StyledTheme.spacing.medium) {
    StyledText("First")
    StyledText("Second")
}

StyledFlowRow(
    itemSpacing = StyledTheme.spacing.small,
    rowSpacing = StyledTheme.spacing.medium,
) {
    StyledText("Different gaps")
}
```

The common lazy layout helpers include `StyledLazyColumn`, `StyledLazyRow`,
`StyledLazyVerticalGrid` and `StyledLazyHorizontalGrid`. They expose the
corresponding Foundation state, content padding, layout, fling and scroll
parameters without requiring theme registration. Grid variants take `GridCells`
through `columns` or `rows`. When migrating from the former `StyledSpaced*`
helpers, explicitly pass `StyledTheme.spacing.medium` for each spacing
parameter to retain their previous defaults.

When their content overflows, lazy lists and grids display a scrollbar indicator
when `StyledTheme.behavior.showScrollbars` is enabled. This is the same global
policy used by scrollable menus, tabs and navigation. Hiding the indicator does
not disable user scrolling.

```kotlin
StyledLazyColumn(itemSpacing = StyledTheme.spacing.medium) {
    items(messages) { message ->
        StyledText(message.title)
    }
}

StyledLazyVerticalGrid(
    columns = GridCells.Adaptive(160.dp),
    verticalItemSpacing = StyledTheme.spacing.medium,
    horizontalItemSpacing = StyledTheme.spacing.medium,
) {
    items(products) { product ->
        ProductCard(product)
    }
}
```
