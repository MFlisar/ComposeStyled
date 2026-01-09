# ComposeStyled – Copilot Instructions

## 0) Big picture

This repository provides a **design-system abstraction for Compose** with a strict separation of concerns:

* **Core module (`library/core`)**

    * defines the **public ComposeStyled API**
    * defines **components, variants, tokens, and interaction models**
    * contains **no concrete visual design decisions**

* **Theme modules (`library/themes/*`)**

    * implement concrete **design systems** (Material3, Cupertino, Fluent2, …)
    * register **component styles** and **renderers**
    * are free to evolve without breaking the Core API

* **Renderer bridge**

    * Core delegates all rendering through an **internal renderer bridge** (`LocalStyledComponents`)
    * Themes provide the actual implementations

### Core goal

> **Keep the public API stable and neutral**,
> while allowing **theme modules and renderers to change freely**.

---

## 1) Module responsibilities

### 1.1 `library/core`

This is the **public API module**.

It MUST NOT expose in public APIs:

* `com.composeunstyled.*`
* Material3 APIs
* platform-specific theme APIs

It MAY:

* use Compose Unstyled internally
* use CompositionLocals internally
* change internal implementation freely

### Core invariants

* `StyledTheme` is a **singleton `object`**

    * ❌ never pass it as a parameter
    * ✅ always access via `StyledTheme.*`

* `LocalStyledComponents` is **internal**

    * public composables delegate rendering to it
    * its API may change without breaking users

---

### 1.2 `library/themes/material3`

Implements a **Material3-like design system**, but:

* **Rendering is done via Compose Unstyled**
* Material3 is used only as a **design reference**

    * [https://m3.material.io/components](https://m3.material.io/components)

This module:

* registers **Material3-style defaults** for all components
* implements `LocalStyledComponents`
* must NOT expose Material3 types to Core

---

### 1.3 `library/themes/cupertino`

Implements a **Cupertino-style design system** using Compose Unstyled.

Design reference:

* [https://developer.apple.com/design/human-interface-guidelines](https://developer.apple.com/design/human-interface-guidelines)

---

### 1.4 `library/themes/fluent2`

Implements a **Fluent 2-style design system** using Compose Unstyled.

Design reference:

* [https://fluent2.microsoft.design](https://fluent2.microsoft.design)

---

## 2) API stability rules (core)

Treat `library/core` as a **published library**.

### 2.1 Public composable evolution

* Trailing `content: @Composable ...` **must always be last**
* New parameters:

    * only **before** the trailing lambda
    * must have **default values**
* ❌ never reorder or rename parameters
* ❌ avoid public `inline` APIs that depend on internals

---

### 2.2 Sealed types

Sealed types are allowed because:

* all variants are controlled by the library

However:

* users must **not rely on exhaustive `when`**
* add KDoc warnings where applicable

---

## 3) Theming & token model (core)

Compose Unstyled is used **internally only** as a **token store**.

Core defines:

* `ThemeProperty<T>` → component category (e.g. `"button"`)
* `ThemeToken<T>` → concrete style key (e.g. `"button.filled.primary"`)

### Core rule

> **Core defines tokens and variants,
> Theme modules define the actual styles.**

Core MUST NOT:

* register concrete visual defaults
* assume any specific design system

---

## 4) Color & interaction model

### 4.1 Color definitions

* `BaseColorDef`

    * `background`
    * `foreground`
    * `border`

* `StatefulBaseColorDef`

    * `normal`
    * `hovered`
    * `focused`
    * `pressed`
    * `error`

### 4.2 Resolve state

```kotlin
StyledResolveState(
    interaction: StyledInteractionState,
    enabled: Boolean
)
```

### 4.3 Disabled handling

Disabled visuals are applied centrally using `DisableFactorType`:

| Element    | DisableFactorType |
| ---------- | ----------------- |
| Background | Default           |
| Foreground | Content           |
| Border     | Outline           |

---

## 5) Interaction derivation (core helpers)

Always use:

```kotlin
rememberStyledResolveState(
    interactionSource,
    enabled,
    isError
)
```

### Interaction priority

**Keyboard mode**

```
Error > Pressed > Focused > Hovered > Normal
```

**Pointer (mouse / touch) mode**

```
Error > Pressed > Hovered > Normal
```

> Focus styling is **keyboard-only** by design.

Decorations like focus rings must be handled **separately**, not via colors.

---

## 6) Component pattern (core)

### 6.1 Component object

Example: `StyledButton`

* defines `ThemeProperty`
* defines `ThemeToken`s
* defines sealed `Variant`

    * built-ins in `companion object`
    * `custom(colorDef)` for overrides
    * internal `Token` / `Custom` subclasses

Core defines **what exists**, not **how it looks**.

---

### 6.2 Public composable

Each component exposes a top-level composable:

Responsibilities:

1. resolve variant → `StatefulBaseColorDef`
2. derive `StyledResolveState`
3. resolve final colors
4. delegate rendering to `LocalStyledComponents`

---

### 6.3 Defaults object

`StyledXDefaults`:

* provides layout defaults (padding, spacing, icon sizes)
* must read values from `StyledTheme.*`
* must NOT hardcode design-system values

---

## 7) Renderer bridge rules

`LocalStyledComponents`:

* is internal
* may change freely
* implemented by theme modules

Renderers receive:

* **resolved colors**
* `interactionSource`
* structural parameters (shape, padding, etc.)

They must NOT:

* re-derive interaction state
* invent their own theming logic

---

## 8) Rules for all theme modules (`library/themes/*`)

* Implement renderers using **Compose Unstyled**
* Register styles via internal registration mechanisms
* Map colors consistently:

    * `background` → container
    * `foreground` → text/icon
    * `border` → outline/indicator

Theme modules may:

* animate
* decorate
* platform-optimize

They must NOT:

* leak Unstyled types into Core
* change Core APIs

---

## 9) Naming & style rules

* Public APIs: `StyledXxx`
* Variants: semantic names (`FilledPrimary`, `Outlined`, `Text`)
* Tokens: stable dotted lowercase ids
* KDoc required on all public APIs
* English only

---

## 10) Copilot instructions (very important)

### When adding a new component

Copilot MUST:

1. follow the Button/Input pattern
2. define tokens in Core
3. **not** register concrete styles in Core
4. delegate rendering to `LocalStyledComponents`
5. add style registration in each theme module

### When editing APIs

Copilot MUST:

* preserve binary compatibility
* never pass `StyledTheme` as a parameter
* keep renderer bridge internal

---