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


# MODULES

# ComposeStyled – KI-Anweisungen (Final)

## Ziel
Stabile Core-API, klare Theme-Verantwortung und ein sauberer Exit-Path über Wrapper-Themes.

---

## 1. Grundprinzipien

### 1.1 Verantwortlichkeiten

**Core (`library/core`)**
- definiert öffentliche API
- definiert Komponenten, Varianten, Tokens, Resolve-Logik
- keine Design-Entscheidungen
- kein direktes Rendering

**Theme-Module (`library/themes/*`)**
- implementieren Renderer
- liefern konkrete Styles
- dürfen intern frei evolvieren

---

### 1.2 Zentrale Invarianten

- `StyledTheme` ist ein `object`
    - niemals als Parameter übergeben
- `LocalStyledComponents`
    - ist `internal`
    - einzige Render-Brücke

---

## 2. Varianten-Modell

### 2.1 Generische Variante (Core)

```kotlin
sealed interface Variant<ID : Enum<ID>, Data> {
    val variantId: ID

    data class Token<ID : Enum<ID>, Data>(
        override val variantId: ID
    ) : Variant<ID, Data>

    data class Custom<ID : Enum<ID>, Data>(
        override val variantId: ID,
        val data: Data
    ) : Variant<ID, Data>
}
```

Helper:

```kotlin
internal fun <ID : Enum<ID>, Data>
Variant<ID, Data>.customDataOrNull(): Data? =
    (this as? Variant.Custom<ID, Data>)?.data
```

---

### 2.2 Component-spezifische Varianten (Public API)

```kotlin
object StyledCard {

    enum class VariantId {
        Filled,
        Outlined
    }

    object Variants {
        val Filled = Variant.Token(VariantId.Filled)
        val Outlined = Variant.Token(VariantId.Outlined)

        fun custom(
            variant: VariantId,
            colorDef: StatefulBaseColorDef
        ) = Variant.Custom(variant, colorDef)
    }
}
```

Usage:

```kotlin
StyledCard(variant = StyledCard.Variants.Filled)
```

---

## 3. Token-basierte Komponenten (Non-Wrapped)

- Core resolved Variant → StatefulData → final values
- Renderer bekommt nur finale Werte
- Renderer leitet keinen State ab

---

## 4. Wrapper-Komponenten (Exit-Path)

### Ziel
- echtes Material3 Verhalten
- keine ComposeStyled-Logik nachbauen
- Refactoring-freier Ausstieg

### WrapperRequest

```kotlin
data class WrapperRequest(
    val id: VariantId,
    val customColors: StatefulBaseColorDef?
)
```

### Regeln
- Built-ins → Material Defaults
- Custom → nur Basisfarben (`normal`)
- Disabled / Pressed → Material3 regelt das

---

## 5. registerVariantStyles

Explizite Signatur:

```text
registerVariantStyles(
    filled: StatefulBaseColorDef,
    outlined: StatefulBaseColorDef
)
```

Neue Variante ⇒ Signaturänderung ⇒ Compiler zwingt Theme-Updates.

---

## 6. Naming-Konventionen

### Komponenten
- `StyledCard`
- `StyledButton`
- `StyledTextField`

### Varianten
- `StyledX.Variants.Filled`
- `StyledX.Variants.custom(...)`

### Renderer
- Token: `StyledXTokenImpl`
- Wrapper: `StyledXWrapperImpl`

---

## 7. Entscheidung: Token vs Wrapper

| Kriterium | Token | Wrapper |
|---------|-------|---------|
| Eigene Design-Logik | ✅ | ❌ |
| Native Material3 | ❌ | ✅ |
| Exit ohne Refactor | ❌ | ✅ |

---

## 8. Fazit

- Core definiert Regeln
- Themes liefern Daten oder delegieren
- Token-Themes = ComposeStyled Look
- Wrapper-Themes = Plattform-native Defaults
