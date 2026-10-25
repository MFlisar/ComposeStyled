---
applyTo: "**/core/src/**/*.kt"
---

# Core APIs for Styled components

Choose the component model before implementing it:

- **Theme-specific rendering:** follow `StyledButton` and `StyledIcon` when
  themes need different renderers, native controls or visual behavior.
- **Generic layout or compound:** follow `StyledColumn`, `StyledRow` and
  `StyledFlowRow` when the same composition works across themes. A compound
  composes existing Styled components and inherits their registered renderers.

For renderer implementation and mapping requirements, follow
[the renderer guide](styled-component-renderer.instructions.md). For compounds,
layouts and the consistency checklist, follow
[the compound guide](styled-component-compounds.instructions.md).

## Theme-specific component contract

1. Add `StyledX.kt` under `library\core\src\commonMain\kotlin\com\michaelflisar\composestyledcomponents\ui\components`
   with package `com.michaelflisar.composestyledcomponents.ui.components`.
   Define `object StyledX` containing a single
   `val Key = StyledKey<Implementation>("StyledX")` and an `Implementation`
   interface that implements `StyledComponent<Implementation>`, exposes `Key`
   through its `key` property and defines the required
   `@Composable fun Render(...)` overloads. Reuse this key for lookups; keys
   are identity-based, not name-based. Register implementation instances with
   `register(implementation)`.
2. Define only the contract the component needs. `StyledButton` demonstrates
   optional `Defaults` and `Customization` value types exposed through the
   implementation contract. A `Defaults` type used by theme renderer modules
   must be publicly constructible across module boundaries; it is implementation
   configuration, not a `StyledXDefaults` convenience API. Renderer
   `defaults(...)` factories construct it directly. Keep `Customization`
   construction internal and expose its public factory functions on
   `StyledXDefaults`, not `StyledX`. Do not make these value types data classes.
   Use `@Stable` for defaults that may contain caller-supplied stable interfaces
   such as `Shape`, and `@Immutable` for customization whose immutable fields
   are guaranteed. Icon-taking APIs use `IconComposable` and name the parameter
   `icon`; do not expose direct `ImageVector`, `Painter`, raw composable-lambda
   or resource overloads. Do not require every component to copy the full
   button structure.
3. Add the public `@Composable fun StyledX(...)` outside the object. Resolve
   `LocalStyledRegistry.current[StyledX.Key]` and forward arguments to `Render`.
   Keep theme-neutral work here: `StyledIcon` applies optional sizing to the
   modifier before dispatch and defaults tint to `LocalStyledContentColor`.
   Pass the supplied `icon: IconComposable` through to the renderer. Preserve
   content receiver scopes such as `RowScope` where needed.
4. For adjustable renderer defaults, expose composable accessors in a dedicated
   `StyledXDefaults` object and use them in public parameter defaults. These
   accessors read the active implementation's defaults; each renderer exposes a
   composable `defaults(...)` factory. Centralize defaults lookups, not all
   registry access: the public component still resolves its implementation from
   the registry to call `Render`.
   Prefer semantic roles for intent and use theme-neutral variants only when
   alternative styles are part of the component's contract. Keep concrete
   renderer styles in the implementation. Keep role- or variant-taking
   dispatch functions private and name them `StyledXImpl`; named public
   role/variant functions preconfigure the role or variant and delegate to that
   implementation without duplicating dispatch or rendering.
   Follow the roles and context rules in the
   [renderer guide](styled-component-renderer.instructions.md).
   If button-style color customization is needed, expose `customize(...)`
   overloads from `StyledXDefaults`, not from `StyledX`. Keep the
   `Customization` constructor internal, store tokens in it, and resolve them
   during composition so token colors follow the active theme.
   Whenever a component supports both filled and transparent/text/outlined
   renderer styles, express color customization through `CustomColors` and an
   optional border color token. Map `filledBackground`/`filledForeground` to
   filled styles and `textForeground` to transparent, text or outlined styles.
   This also applies when roles share one renderer implementation, including
   icon buttons, cards and inputs. State-driven components whose colors vary
   by selection state, such as chips, use separate unselected and selected
   color overrides instead. Dropdown anchors reuse
   `StyledButton.Customization`. Direct container/content color parameters are
   appropriate only for components without a transparent style, such as
   Scaffold, bottom navigation, FAB and Surface.
   Renderer-specific style controls, such as card elevation that only some
   variants or themes support, belong in `Customization` rather than the
   component's general parameter list. Renderers use their native defaults
   when no override is supplied and may ignore customization fields that do
   not apply to a style.
   Roles, variants, defaults and customization are optional; omit unused elements.

## Component structure and public API

Keep the following order for `StyledX` component files, including only
applicable sections. Do not add empty sections or unrelated parameters solely
to match a template.

- `object StyledX`, when the component needs a registry contract.
- Inside `object StyledX`: key, defaults, roles/variants/context, customization,
  implementation contract.
- A context local marked with `@InternalComposeStyledApi`, when needed.
- Outside the object: `StyledXDefaults` when needed, all public API overloads,
  private API implementation (`*Impl`) when needed, then private helper
  functions.

Use the exact section comments and ordering below for registered component
files. Keep spelling, numbering, separator lengths and blank-line layout
consistent. Omit sections that do not apply, retaining the original numbers of
the remaining sections. Do not add empty sections or unnecessary API elements.
Put every public composable in one `public API` section, including named
role/variant functions such as `StyledPrimaryButton`; do not create a separate
`public API - convenient overloads` section. Put all private dispatch
implementations after the public API section.

```kotlin
object StyledX {

    // -------------
    // 1) key
    // -------------

    // -------------
    // 2) defaults
    // -------------

    // -------------
    // 3) roles / variants / context
    // -------------

    // -------------
    // 4) customization
    // -------------

    // -------------
    // 5) implementation
    // -------------

}

// local context
@InternalComposeStyledApi
val LocalStyledXContext = staticCompositionLocalOf { StyledX.Context.Default }

// -------------------------
// defaults
// -------------------------

// -------------------------
// public API
// -------------------------

// -------------------------
// private API
// -------------------------

// -------------------------
// private functions
// -------------------------
```

Replace `StyledX` with the component name and fill each applicable section with
its declarations. The example is a structural template, not a complete component.
When creating or refactoring a registered component file, use this layout rather
than inventing alternate headings. Do not reformat unrelated files solely to
normalize comments.

Expose only parameters required by the control. `enabled`, `interactionSource`,
customization and content slots are not mandatory for every component. If a
composable accepts both `value` and `onValueChange`, provide a `MutableState`
overload first; it must delegate to the `value`/`onValueChange` overload, which
follows it and remains the single state-change implementation. Icon parameters
must be named `icon` and use `IconComposable`; do not add overloads accepting
`ImageVector`, `Painter`, a raw composable lambda or a resource directly.
Preserve content receiver scopes and dispatch paths.
Name private helper functions in component files without a `Styled` prefix.
These helpers are local to the component and should use concise,
context-specific names. This does not apply to public component APIs or shared
helpers.
When a component uses a role or variant to choose its renderer behavior, keep
that selector on a private `StyledXImpl` function rather than exposing it on
public composables. Do not add a combined `Style` wrapper or style-builder
functions that package adjustable parameters together.
For button-like public APIs, put the action and standard interaction parameters
(`modifier`, `enabled`, `interactionSource`) first. Add
`// defaults / customisation` before adjustable defaults and the optional
customization parameter, then `// content` before the content slot. Keep the
same grouping and order in named role/variant APIs. A private `StyledXImpl`
function that takes a role or variant keeps that selector first, followed by
the action and standard interaction parameters, defaults/customization, and
content, using the same grouping comments.
