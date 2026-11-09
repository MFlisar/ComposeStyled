# Chip

`StyledChip` is clickable, while `StyledSelectableChip` also exposes a
`selected` state. Both accept `Auto`, `Filled` and `Outlined` variants. `Auto`
is the default and lets the theme choose the rendering. Material 3 maps `Auto`
and `Outlined` to standard assist/filter chips, and `Filled` to filled
non-elevated variants. Its elevated variants remain available for custom
mappings, but are not used by default because of their shadow artifacts.

Use `StyledOutlinedChip` or `StyledOutlinedSelectableChip` to fix the outlined
variant. Both chip APIs accept optional leading and trailing `IconComposable`
values.

`StyledChipDefaults.colors(...)` customizes the unselected `container`,
`content` and `border`, with optional `selectedContainer`, `selectedContent` and
`selectedBorder` overrides for selected chips. Omitted colors retain each
renderer's defaults. To customize Material 3's variant mapping, register a
configured `Material3Chip.create(...)` through
`StyledTheme(customization = ...)`.
