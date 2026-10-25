---
applyTo: "**/ui/**/*.kt"
---

# Styled layouts, compounds and consistency

For Core component APIs and renderer implementation details, follow the
[Core guide](styled-component-core.instructions.md) and
[renderer guide](styled-component-renderer.instructions.md).

## Generic layouts and compounds

1. Add generic layouts under `library\core\src\commonMain\kotlin\com\michaelflisar\composestyledcomponents\ui\layout`
   with package `com.michaelflisar.composestyledcomponents.ui.layout`.
   Place compounds alongside related controls in `ui\components`. Compose
   Foundation layouts and existing Styled components directly; no
   `StyledKey`, renderer class or theme-factory registration is needed.
2. Follow the layout pairs: `StyledColumn`, `StyledRow` and `StyledFlowRow`
   accept explicit arrangements; their `StyledSpaced*` counterparts use
   `Arrangement.spacedBy` with overridable spacing defaulted from
   `StyledTheme.spacing.medium`. Flow layouts expose separate item/row spacing,
   item alignment and line/item limits. Preserve the underlying layout's
   `ColumnScope`, `RowScope` or `FlowRowScope` content receiver and forward its
   modifier and layout arguments.
   Spaced convenience functions delegate to their corresponding public Styled
   layout (`StyledSpacedRow` to `StyledRow`, `StyledSpacedFlowRow` to
   `StyledFlowRow`), changing only arrangements rather than duplicating the
   underlying Foundation call.
3. For compounds, use existing Styled controls for themed parts and Styled
   tokens for shared layout defaults. Do not branch on the theme name or import
   concrete Cupertino, Fluent 2 or Material 3 implementations into `core`.
   If a part truly requires theme-specific rendering, give that part the
   registered component contract described in the Core guide.
4. Reuse the **public Styled composable**, not just its tokens or renderer
   defaults. Reading `StyledDropdownMenuItem.Implementation.defaults` and applying
   them to a clickable `Box` is not equivalent to composing
   `StyledDropdownMenuItem`: it bypasses native rendering, interaction feedback,
   content locals and custom registry replacements. Selection dropdowns must
   compose `StyledPopupMenu` and `StyledDropdownMenuItem`, just like standalone
   menus and dropdown buttons. Keep selection, filtering and dismissal policy in
   the compound; leave item rendering and interaction feedback to the renderer.
5. Add compound-specific selection semantics through the Styled control's
   modifier without attaching a second `clickable`, `selectable` or `toggleable`
   action. Preserve radio/checkbox roles, selected/checked state, keyboard
   activation and exactly one action callback. If the public control cannot
   express a required behavior, extend its shared contract and all renderers
   rather than introducing an unthemed fallback inside the compound.
6. Foundation layouts, text primitives and modifiers are appropriate for layout
   and behavior that has no matching Styled control. Do not replace a distinct
   labeled dropdown anchor or expandable heading with an ordinary StyledButton
   merely because both are clickable; preserve their intended layout and API.

## Consistency review for component changes

Before considering a Styled component migration or renderer change complete:

- Find all consumers and helper paths, not only direct demos. Trace shared bases,
  named role/variant overloads, single/multi-select variants and default content slots.
  When a new Styled primitive replaces old rendering, migrate every equivalent
  internal implementation in the same change.
- Check composition, not just appearance: equivalent standalone and compound
  controls must reach the same public Styled API and active registry renderer.
  A custom renderer replacement must affect both; copying defaults is not enough.
- Check context mappings inside and outside the provider, nested scopes and
  delegation to default mappings for custom overrides. Ensure a dialog-action
  scope does not affect normal buttons in the dialog body or outside the dialog.
- Check exact Core and renderer section comments and ordering, mapping alias
  signatures and names, and opt-in at cross-module context local accesses.
- Keep every public composable in the single `public API` section before private
  `*Impl` dispatch functions; do not create a separate convenience-overload
  section.
- Check that `MutableState` overloads precede and delegate to their matching
  value/`onValueChange` overloads, icon parameters use `IconComposable` named
  `icon`, and role/variant dispatch stays in a private `*Impl` function.
- Check that defaults lookups use `StyledXDefaults` while public dispatch still
  resolves the active implementation. Keep registry `Key.defaults` reads only
  inside the matching `StyledXDefaults`; consumers use its accessors. Include
  every relevant configuration value in renderer and theme factory `remember`
  keys.
- Check all three themes and shared Foundation delegation. Preserve disabled,
  selection, hover/focus/press, keyboard and dismissal behavior, slot content
  locals and callback ownership. Inspect modifier order: item minimum height
  precedes outer item padding, then shape clipping and interaction feedback.
- For adjacent outlined items, verify every seam: negative spacing or offsets
  must produce the intended overlap at each boundary, not only the first one.
  Check boundaries in all relevant selected and interaction states.
- Distinguish a compound bypass from renderer implementation: a registered
  Foundation renderer may implement its own control using Foundation primitives,
  and Material 3 renderers should use their native counterparts. Sharing
  Foundation rendering between distinct registered controls does not mean that
  replacing one control's registry entry must replace the other.
- Search for remaining hand-built equivalents (`Box`/`Row` plus interaction
  modifiers), copied dimensions and hard-coded visual defaults. Distinguish
  intentional compound-specific layout from duplicated control rendering.
- Popup position providers must use the anchor bounds supplied by Compose in
  the popup's coordinate system. Do not replace these with separately measured
  `positionInWindow()` coordinates. Inspect nested Scaffold and scroll-container
  usage as well as top-level anchors when changing popup placement.
- If runtime execution is excluded, perform this review statically and explicitly
  leave visual/runtime behavior unverified; compilation alone does not prove
  renderer reuse, semantics or visual consistency.

For either model, add usage to `demo\shared` alongside the existing component
demos and update `documentation\_partials\usage.md.partial`. For registered
components, exercise every theme with default and customized rendering;
button-like controls also need their variants and interaction states covered,
and icon-like controls use the `IconComposable` input API rather than separate
vector, painter, lambda or resource overloads.

Generally do not write unit tests or similar.
