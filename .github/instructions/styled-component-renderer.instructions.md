---
applyTo: "**/implementations/**/*.kt"
---

# Styled component renderers

For Core contracts and public API structure, follow
[the Core guide](styled-component-core.instructions.md). For compounds, layouts
and the consistency checklist, follow
[the compound guide](styled-component-compounds.instructions.md).

## Theme renderers

1. Add `<Theme>X : StyledX.Implementation` under the corresponding
   implementation module's `commonMain` package, for Cupertino, Fluent 2 and
   Material 3. Follow the internal-constructor/public-composable-`create(...)`
   pattern. Keep renderer instances stable at each composition call site:
   parameterless factories use `remember { ... }`; configurable factories use
   `remember(...)` with every constructor parameter as a key, including defaults
   and role, variant or context mappings. Evaluate composable defaults before the
   `remember` calculation. Pass a `StyledThemeProvider` to `StyledTheme`.
   Styled token locals are provided first, then `provider.Root` is applied.
   Only inside that root does `provider.definition()` call `xThemeDefinition()`,
   so factory defaults can read both Styled tokens and native theme locals.
   Keep renderer creation inside the composable `registerDefaultComponents`
   callback, not the provider constructor. The provider's definition factory
   runs the default and custom registration callbacks through
   `StyledRegistry.createComposable`; `StyledThemeDefinition` holds only name
   and registry. `rememberXTheme()` remembers the provider, keyed by its root
   configuration and both registration callbacks, without creating renderers.
   Do not add per-component implementation parameters to theme factories for
   registry replacement. Register default renderers through the theme's
   `registerXComponents()` helper; callers can replace entries through
   `registerCustomComponents`. Keep any actual theme-factory configuration
   values in the factory's own `remember` keys.
   Material 3 renderers adapt native Material 3 controls. Cupertino
   and Fluent 2 may implement distinct Foundation-based behavior, as their
   buttons do, or delegate identical rendering to a shared Foundation class,
   as `CupertinoIcon` and `Fluent2Icon` do with `FoundationIcon`. Mark shared
   renderer APIs with `InternalComposeStyledApi` as existing Foundation APIs do.
2. Keep visual and interaction behavior in the renderer: role/variant/context
   mapping, disabled/pressed/hover/focus states, semantics and theme-specific
   dimensions. Forward the caller's modifier, callbacks, interaction source and
   slots. Provide styled content color/text style for descendants using
   `ProvideCustomStyledContentLocals`; inside native Material 3 content slots,
   use `CopyMaterialThemeLocals` like `Material3Button` so nested `StyledText`
   and `StyledIcon` inherit the control's actual styling.
3. Wire the component into **all three** theme registration helpers:
   `registerCupertinoComponents()`, `registerFluent2Components()` and
   `registerMaterial3Components()`. Each helper registers its theme renderer
   with    `register(ThemeX.create())`; the implementation contract supplies its own
   key. Do not add a separate
   component implementation parameter to the `remember*Theme` factories;
   callers can replace defaults through `registerCustomComponents`, which runs
   after the default registration callback. Missing registrations fail at
   lookup. Scoped replacements use `UpdateStyledRegistry` and inherit other
   entries from the parent registry.
4. Follow Cupertino and Fluent 2 design guidelines in their respective
   renderers. Foundation renderers must not branch on theme flags or theme
   identities. Calculate theme-specific colors, dimensions and interaction
   styling in the theme implementation and pass the resolved values to shared
   Foundation code.
5. Foundation renderers should be theme-neutral and not reference any specific
   theme. If necessary, use parameters to customize the behavior of the
   renderer, but make them specific; do not use `isCupertino` or `isFluent2`
   flags.

## Shared Foundation adapters

Cupertino and Fluent 2 theme-definition functions must register their own
theme-named implementation classes, not reference `FoundationX` renderers
directly. When both themes share Foundation rendering, add a thin
`CupertinoX`/`Fluent2X` adapter in that theme's `components` package and delegate
to the shared renderer through its supported public constructor or composable
factory. Prefer the renderer's `create(...)` factory when available. Keep
theme-specific defaults on the adapter; the theme definition should use only
the adapter's `create(...)`. This keeps registry configuration theme-owned
while allowing rendering to be shared.

Shared renderers may resolve theme-neutral `StyledTheme` tokens. When matching a
native renderer's defaults, use the corresponding semantic token rather than
choosing a primary token by habit, and preserve explicit customization as the
higher-priority value.

## Renderer structure

Use `Material3Button` as the structural reference for theme implementations.
Apply the following exact companion section comments, numbering, separator
lengths and blank-line layout across Material 3, Cupertino and Fluent 2
implementations. Omit unused sections without renumbering the remaining ones.

```kotlin
class ThemeX internal constructor(
    val roleMapping: RoleMapping,
    override val defaults: StyledX.Defaults,
) : StyledX.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        // -------------
        // 2) mapping
        // -------------

        // -------------
        // 3) defaults
        // -------------

    }

}
```

Fill the sections with the composable `create(...)` factory, mapping typealias
and default mapping, and composable `defaults(...)` factory, respectively.
After the companion, declare concrete renderer variants, then `Render`
overloads, then any remaining private implementation helpers. File-level
helpers follow the class. Do not add these declarations when unnecessary.
Name private helper functions in component renderer files without a `Styled`
prefix; keep names concise and specific to the renderer context. This does not
apply to public Styled APIs or shared helpers.
The template shows ordering, not a complete implementation.

Use `RoleMapping`/`roleMapping`/`DefaultRoleMapping` for role-based contracts and
`VariantMapping`/`variantMapping`/`DefaultVariantMapping` for variant-based
contracts. Concrete renderer styles may still be named `Variant` even when the
Core API uses roles.

Prefer a non-composable function typealias for mappings, not a mapping data
class or a mandatory `ContextMapping` wrapper. Its inputs are the Core role or
variant and, only when needed, the Core context; its output is the concrete
renderer variant. Define the alias directly under `// 2) mapping` inside the
renderer companion, followed by the default mapping. For example, inside
`Material3Button.Companion`:

```kotlin
// -------------
// 2) mapping
// -------------

typealias RoleMapping = (StyledButton.Role, StyledContext) -> Variant

val DefaultRoleMapping: RoleMapping = { role, context ->
    when {
        context == StyledSnackbar.Context.SnackbarAction ||
            context == StyledDialog.Context.DialogAction -> Variant.Text
        else -> when (role) {
            StyledButton.Role.Primary -> Variant.Primary
            StyledButton.Role.Secondary -> Variant.FilledTonal
            StyledButton.Role.Tertiary -> Variant.Text
        }
    }
}
```

Keep `RoleMapping` and `VariantMapping` aliases in this mapping section, not at
file level. Verify nested typealias support in the configured Kotlin compiler
and language settings. If unsupported, report the blocker rather than silently
moving the alias or changing language settings.

Expose the mapping through `create(...)`, store it on the implementation and
include it in `remember(mapping, defaults, ...)` using the actual parameter name.
Keep the default lambda in a `val` for a reusable instance. Mappings should be
pure functions of their inputs and captured configuration. Custom mappings can
delegate to the default mapping for cases they do not override.

## Roles, variants and context

Roles describe intent, not appearance. For buttons, the target semantic API uses
`Role.Primary`, `Role.Secondary` and `Role.Tertiary`; the renderer maps each
role to a concrete style. Styles such as Material 3 `FilledTonal` or `Text`
belong to the renderer, not the semantic role enum. Other components may expose
theme-neutral variants when distinct styles are part of their intended API.
Do not add a renderer mapping solely because a component exposes variants.
Variants with theme-neutral meaning, such as control size or title alignment,
must be consumed directly by each renderer; add a mapping only when themes need
to translate the same public variant into different renderer-specific styles.

The repository may still contain button `Variant` APIs and their convenience
functions. Treat those as existing APIs, not the target role model. Replacing
them is an explicit migration: update the public contract, every renderer,
convenience function, compound, consumer, demo and documentation in scope.
Do not introduce role and variant parameters with overlapping meanings or
silently migrate unrelated controls.

Introduce context only when the same control needs different rendering based on
where it is used. Separate component types, such as navigation items and tabs,
do not need a context merely to distinguish them.
Do not confuse renderer-mapping contexts with component data/state contexts.
For example, a dropdown's `RenderContext.Selection` and
`RenderContext.DropdownItem` describe the content being rendered; they are not
theme contexts and must not be added to a role-mapping signature.

For a context-aware component:

- Declare an open `StyledContext` contract in Core and public context values on
  their source components so separate renderer modules can use them in mapping
  signatures. Use precise values such as `SnackbarAction`, rather than a broad
  `Snackbar` value. Handle known contexts explicitly and fall back to the normal
  role/variant mapping for unknown contexts.
- Expose the Core context local across modules with `@InternalComposeStyledApi`,
  not Kotlin `internal`. Renderers read it directly inside `Render` with
  `@OptIn(InternalComposeStyledApi::class)` and pass its current value to the
  mapping alongside the role or variant. Do not add a context parameter to the
  public composable or `Implementation.Render` solely to transport this local.
  Keep provider helpers internal when Core compounds own the scope.
- Provide the context only around the relevant slots, such as dialog action
  buttons, not around all dialog content. Siblings outside the provider retain
  their existing context; nested providers use the nearest supplied value.
- Handle context and role or variant together in the mapping lambda. Make all
  known contexts explicitly and provide a normal role/variant fallback because
  `StyledContext` is open to future source components. A context may map every
  role to the same style, such as `DialogAction` always returning `Text`. Treat
  `StyledContext.Default` and unknown contexts as normal rendering; do not
  require separate mapping classes or implicit merges.
- Keep mappings configurable through renderer factories and include them in
  `remember` keys. A renderer may supply a design-system-specific default,
  such as mapping every dialog-action role to Material 3 `Text`; changing
  existing defaults is an intentional behavior change that must be documented.
- Keep explicit customization separate from semantic roles and mapping.
  Resolve color tokens during composition and preserve the renderer's
  documented handling of disabled states and supported overrides.
