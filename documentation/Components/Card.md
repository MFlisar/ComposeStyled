`StyledCard` accepts `Auto`, `Filled` and `Outlined` variants. `Auto` is the
default and maps to the filled Material 3 card; platform renderers can adapt its
appearance to their conventions. `StyledOutlinedCard` fixes the variant to
`Outlined`, while `StyledCard(variant = ...)` can select any variant directly.

Cards dispatch through `StyledCard.Key` and add
`StyledCardDefaults.contentPadding` as `PaddingValues` by default (16 dp on all
sides for Material 3 and Cupertino Flat, 18 dp for Cupertino Liquid, 12 dp for
Fluent 2). Change the theme
default with
`StyledTheme(customization = { update(StyledCard.DefaultsKey) { copy(contentPadding = ...) } })`.
Override `contentPadding`, for example with
`PaddingValues(horizontal = 20.dp, vertical = 12.dp)`, per card or use
`applyContentPadding = false` for caller-managed padding. Card padding is not
part of `StyledPadding`.

All variants support `shape` and optional
`StyledCardDefaults.customize(colors, border, elevation)`. The `colors` argument
is optional, so border and/or elevation can be customized without overriding
colors. An optional `onClick`
enables button semantics, keyboard activation and an interaction source;
`enabled = false` disables the action. Filled styles use
`CustomColors.filledBackground` and `CustomColors.filledForeground`; outlined
styles use `textForeground` and the optional border color. Elevation is part of
customization because it is style-specific: Material 3 uses a supplied
elevation for filled cards and otherwise retains its native default. Cupertino
cards use no shadow by default. Fluent 2 uses 2 dp for standard cards and 0 dp
for alternative cards by default; a supplied elevation overrides either
default.
Material 3 maps filled cards to `Card` and outlined cards to `OutlinedCard`,
selecting the clickable overload only when `onClick` is supplied. Cupertino
and Fluent 2 adapt the same variants to their card appearance and share
Foundation rendering. Replace a theme's component mapping or renderer through
`StyledTheme(customization = ...)`.
