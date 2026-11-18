# Row, column, lazy and grid layouts

Use Foundation `Row`, `Column` and `FlowRow` for ordinary non-lazy layouts.
They expose Compose's standard arrangement and alignment parameters. For
theme-based spacing, pass `StyledTheme.spacing` to `Arrangement.spacedBy`:

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium)) {
    StyledText("First")
    StyledText("Second")
}

Column(verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small)) {
    StyledText("Title")
    StyledText("Description")
}

FlowRow(
    horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
    verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
) {
    StyledText("First")
    StyledText("Second")
}
```

The Styled lazy layouts remain useful beyond Foundation's layout APIs: when
content overflows, they display a platform scrollbar indicator when
`StyledTheme.behavior.showScrollbars` is enabled. Hiding the indicator does not
disable user scrolling.

`StyledLazyColumn` and `StyledLazyRow` expose the corresponding Foundation
state, content padding, arrangement, fling and scroll parameters. Their
`itemSpacing` overloads set `Arrangement.spacedBy(itemSpacing)`. Lazy grid
variants take `GridCells` through `columns` or `rows`, and their spacing
overloads require both `verticalItemSpacing` and `horizontalItemSpacing`.

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
