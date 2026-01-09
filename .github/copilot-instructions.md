# Instruction for the library/core module

# Instruction for the library/themes/android module

This module implements code with the help of compose unstyled.

- Github: https://github.com/composablehorizons/compose-unstyled
- Dokumentation: https://composables.com/docs/compose-unstyled/overview

# Instrcution for the library/themes/material3 module

This module implements code with the help of compose material3.

---

## ComposeStyled – Copilot Instructions

### 0) Big picture

This repo provides a design-system API for Compose with:

- a public Core API (`library/core`) that exposes only ComposeStyled types
- internal usage of Compose Unstyled (`com.composeunstyled.theme.*`) for theme token storage / platform theme sourcing
- one or more renderer implementations (e.g. `library/themes/*`) wired through an internal renderer bridge (`LocalStyledComponents`)
- a demo app (Android) that uses the Material3 theme module.

Core goal: Keep the public API stable, while allowing internal implementations and platform adapters to evolve.

Themes are implemented in separate modules and do use following framework:

Compose Unstyled:

    * Github: https://github.com/composablehorizons/compose-unstyled
    * Dokumentation: https://composables.com/docs/compose-unstyled/overview

---

### 1) Module responsibilities

#### 1.1 `library/core`

Public API lives here. Must NOT expose:

- any `com.composeunstyled.*` types in public signatures
- Material3 types
- platform-specific theme APIs

It MAY use Compose Unstyled internally (in internal code or internal members), but the public API must remain ComposeStyled-only.

`StyledTheme` is a singleton object (no instances). Never pass `StyledTheme` as a parameter to functions. Always access via `StyledTheme.*`.

`LocalStyledComponents` (renderer bridge) is internal. The public composables call into it.

#### 1.2 `library/themes/material3`

Implements the renderer (`LocalStyledComponents`) using compose unstyled.

It tries to mimic the Material3 design system which is documented here: https://m3.material.io/components

#### 1.3 `library/themes/cupertino`

Implements the renderer (`LocalStyledComponents`) using compose unstyled.

It tries to mimic the Cupertino design system which is documented here: https://developer.apple.com/design/human-interface-guidelines/presentation

#### 1.3 `library/themes/fluent2`

Implements the renderer (`LocalStyledComponents`) using compose unstyled.

It tries to mimic the Fluent2 design system which is documented here: https://fluent2.microsoft.design/components/web/react/core/nav/usage

---

### 2) API stability rules (core)

Treat `library/core` as a published library. Maintain binary compatibility.

#### 2.1 Public composable evolution

- Keep `content: @Composable ...` as the last parameter (trailing lambda).
- You may add new parameters only before the trailing lambda, with default values.
- Never add parameters after the trailing lambda.
- Avoid renaming or reordering parameters (named args break source compatibility).
- Avoid public inline functions that depend on internal implementation details.

#### 2.2 Sealed types

Sealed types are ok because variants are controlled by the library.

But users must not rely on exhaustive `when`. Add KDoc hints where relevant.

---

### 3) Theming model (core)

We use Compose Unstyled internally as a token store:

- `ThemeProperty<T>` groups related tokens (e.g. `"button"`, `"input"`).
- `ThemeToken<T>` identifies a specific style (e.g. `"button.filled.primary"`).

In each component object (`StyledButton`, `StyledInput`, …) define:

- `internal val Property = ThemeProperty<T>("...")`
- `internal val TokenX = ThemeToken<T>("...")`
- `internal fun registerStyle(builder, colors)` to register defaults

Public API must not leak `Theme`, `ThemeBuilder`, `ThemeProperty`, `ThemeToken`.

#### 3.1 Color/state model

We represent component colors as:

- `BaseColorDef` (background/foreground/border) using direct `Color`
- `StatefulBaseColorDef` (normal/hovered/focused/pressed/error)
- `StyledResolveState` with:
  - `interaction: StyledInteractionState`
  - `enabled: Boolean`

Disabled treatment is applied centrally using `DisableFactorType` (e.g. `Default`/`Content`/`Outline`).

- Border typically uses `Outline`
- Foreground uses `Content`
- Background uses `Default`

#### 3.2 Interaction derivation (core helper)

Use the core helper:

- `rememberStyledResolveState(interactionSource, enabled, isError)`

Priority:

- Error > Pressed > Focused > Hovered > Normal

---

### 4) Component pattern (core)

Each component follows the same structure:

#### 4.1 Component object (e.g. `StyledButton`)

`object StyledButton`

- defines `Property` + `ThemeTokens` internally
- defines sealed class `Variant` with:
  - built-ins in `companion object` (e.g. `FilledPrimary`, `Outlined`, `Text`)
  - `fun custom(colorDef: StatefulBaseColorDef): Variant`
  - `internal data class Token(...) : Variant()`
  - `internal data class Custom(...) : Variant()`

`registerStyle(builder, colors)` registers defaults.

#### 4.2 Public composable (top-level)

Top-level composable lives beside the object.

Example pattern:

- resolve the `StatefulBaseColorDef` by:
  - token lookup if variant is `Token`
  - use `Custom.colorDef` otherwise
- derive `resolveState` via helper
- call `colorDef.resolve(resolveState)` to produce final colors
- delegate rendering to `LocalStyledComponents.current.<Component>(...)`

#### 4.3 Defaults object

`StyledXDefaults` provides shared defaults (padding, etc.) and must use the `StyledTheme.*` singleton.

---

### 5) Renderer bridge rules

`LocalStyledComponents` is internal and may change freely.

Renderers (Material3 etc.) implement methods like:

- `Button(colors, onClick, modifier, enabled, shape, contentPadding, interactionSource, content)`
- `Input(value, onValueChange, colors, enabled, isError, shape, contentPadding, interactionSource, ...)`

Core computes states and resolves colors unless there is a strong reason to do it in the renderer.

Prefer “core resolves” for consistent behavior across renderers.

---

### 6) Rules for all themes modules (`library/themes/*`)

- Implement `LocalStyledComponents` using compose unstyled primitives.
- Do not re-implement token/state logic here unless unavoidable.

Map:

- `background` → container color
- `foreground` → content (text/icon) color
- `border` → outline / indicator if applicable

Respect `interactionSource` passed from core (no extra sources unless required).

Do not expose compose unstyled types in core.

---

### 7) Rules for all wrapper modules (`library/wrapperthemes/*`)

Those do wrap some external theme (e.g. Material3) to provide a ready-to-use `StyledTheme`.

Do not expose the external theme types (e.g. Material3) in core.

---

### 8) Naming / style

- Public APIs: `StyledXxx` prefix
- Variants: nouns/adjectives (`FilledPrimary`, `Outlined`, `Text`)
- Tokens: stable lowercase dotted ids (`"button.filled.primary"`)
- Use KDoc on public declarations, English only.
- Prefer `data object` only if you migrate variants to sealed interface; otherwise keep the current companion approach.

---

### 9) What Copilot should do when editing/adding components

When asked to add a new component:

1. Create object `StyledX` with internal `ThemeProperty` + tokens.
2. Create Variant pattern identical to Button/Input.
3. Implement `registerStyle(...)` + `createDefaultKeyMap(colors)`.
4. Create `StyledXDefaults`.
5. Create top-level `@Composable fun StyledX(...)`.
6. Delegate to `LocalStyledComponents`.
7. Update theme registration list so defaults are registered.

When changing APIs:

- never pass `StyledTheme` as param (singleton only)
- keep binary-compat rules
