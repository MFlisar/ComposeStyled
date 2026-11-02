[![Maven Central](https://img.shields.io/maven-central/v/io.github.mflisar.composestyled/core?color=blue)](https://central.sonatype.com/artifact/io.github.mflisar.composestyled/core) ![API](https://img.shields.io/badge/api-24%2B-brightgreen.svg) ![Kotlin](https://img.shields.io/github/languages/top/MFlisar/ComposeStyled.svg?color=blueviolet) ![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-blue?label=Kotlin)
# ComposeStyled
![Platforms](https://img.shields.io/badge/PLATFORMS-black?style=for-the-badge)

ComposeStyled is a Kotlin Multiplatform library for Compose. It provides
themed UI components through a consistent API, making it easy to build
applications with different design systems.

**Main features**
- `Styled*` components for common controls, navigation, and layouts
- Ready-to-use themes and renderers for Material 3, Cupertino Flat, Cupertino Liquid, and Fluent 2
- Shared design tokens for colors, spacing, and other theme values
- Replaceable renderers through a central component registry
- Customizable themes and targeted overrides for individual components
- Foundation-based renderers as a starting point for custom implementations
- Support for Android, iOS, Windows, macOS, and WebAssembly

> [!TIP]
> Try the interactive demo:
> 
> [![Open Interactive Demo](https://img.shields.io/badge/Compose_Styled_Demo-2962FF?style=for-the-badge&logo=googlechrome&logoColor=white)](https://mflisar.github.io/ComposeStyled/demo/)

# Table of Contents

- [Supported Platforms](#computer-supported-platforms)
- [Versions](#arrow_right-versions)
- [Setup](#wrench-setup)
- [Usage](#rocket-usage)
- [Demo](#sparkles-demo)
- [API](#books-api)
- [Other Libraries](#bulb-other-libraries)

# :computer: Supported Platforms

| Module | Notes |
|---|---|
| core | the core module |
| foundation | a foundation implementation (can be used as a base for other implementations) |
| cupertino-flat | a flat Cupertino implementation |
| cupertino-liquid | a Liquid Glass Cupertino implementation |
| fluent2 | a fluent2 implementation |
| material3 | a material3 implementation |
| iconset-material3 | Material icon sets |
| iconset-fluent2 | Fluent 2 icon sets |
| iconset-sfsymbols | SF Symbols icon set |
| theme-cupertino | cupertino color themes |
| theme-fluent2 | fluent2 color themes |
| theme-material3 | material3 color themes |
| theme-pastel | pastel color themes |
| theme-nature | nature color themes |

# :arrow_right: Versions

| Dependency | Version |
|---|---|
| Kotlin | `2.4.21` |
| Jetbrains Compose | `1.12.2` |
| Jetbrains Compose Material3 | `1.9.0` |

> :warning: Following experimental annotations are used:
> - **OptIn**
>   - `androidx.compose.foundation.ExperimentalFoundationApi` (3x)
>   - `androidx.compose.material3.ExperimentalMaterial3Api` (5x)
>   - `androidx.compose.ui.ExperimentalComposeUiApi` (4x)
>   - `dev.chrisbanes.haze.ExperimentalHazeApi` (1x)
>   - `kotlinx.cinterop.ExperimentalForeignApi` (3x)
>
> I try to use as less experimental features as possible, but in this case the ones above are needed!

# :wrench: Setup

<details open>

<summary><b>Using Version Catalogs</b></summary>

<br>

Define the dependencies inside your **libs.versions.toml** file.

```toml
[versions]

composestyled = "<LATEST-VERSION>"

[libraries]

composestyled-core = { module = "io.github.mflisar.composestyled:core", version.ref = "composestyled" }
composestyled-foundation = { module = "io.github.mflisar.composestyled:foundation", version.ref = "composestyled" }
composestyled-cupertino-flat = { module = "io.github.mflisar.composestyled:cupertino-flat", version.ref = "composestyled" }
composestyled-cupertino-liquid = { module = "io.github.mflisar.composestyled:cupertino-liquid", version.ref = "composestyled" }
composestyled-fluent2 = { module = "io.github.mflisar.composestyled:fluent2", version.ref = "composestyled" }
composestyled-material3 = { module = "io.github.mflisar.composestyled:material3", version.ref = "composestyled" }
composestyled-iconset-material3 = { module = "io.github.mflisar.composestyled:iconset-material3", version.ref = "composestyled" }
composestyled-iconset-fluent2 = { module = "io.github.mflisar.composestyled:iconset-fluent2", version.ref = "composestyled" }
composestyled-iconset-sfsymbols = { module = "io.github.mflisar.composestyled:iconset-sfsymbols", version.ref = "composestyled" }
composestyled-theme-cupertino = { module = "io.github.mflisar.composestyled:theme-cupertino", version.ref = "composestyled" }
composestyled-theme-fluent2 = { module = "io.github.mflisar.composestyled:theme-fluent2", version.ref = "composestyled" }
composestyled-theme-material3 = { module = "io.github.mflisar.composestyled:theme-material3", version.ref = "composestyled" }
composestyled-theme-pastel = { module = "io.github.mflisar.composestyled:theme-pastel", version.ref = "composestyled" }
composestyled-theme-nature = { module = "io.github.mflisar.composestyled:theme-nature", version.ref = "composestyled" }
```

And then use the definitions in your projects **build.gradle.kts** file like following:

```java
implementation(libs.composestyled.core)
implementation(libs.composestyled.foundation)
implementation(libs.composestyled.cupertino.flat)
implementation(libs.composestyled.cupertino.liquid)
implementation(libs.composestyled.fluent2)
implementation(libs.composestyled.material3)
implementation(libs.composestyled.iconset.material3)
implementation(libs.composestyled.iconset.fluent2)
implementation(libs.composestyled.iconset.sfsymbols)
implementation(libs.composestyled.theme.cupertino)
implementation(libs.composestyled.theme.fluent2)
implementation(libs.composestyled.theme.material3)
implementation(libs.composestyled.theme.pastel)
implementation(libs.composestyled.theme.nature)
```

</details>

<details>

<summary><b>Direct Dependency Notation</b></summary>

<br>

Simply add the dependencies inside your **build.gradle.kts** file.

```kotlin
val composestyled = "<LATEST-VERSION>"

implementation("io.github.mflisar.composestyled:core:${composestyled}")
implementation("io.github.mflisar.composestyled:foundation:${composestyled}")
implementation("io.github.mflisar.composestyled:cupertino-flat:${composestyled}")
implementation("io.github.mflisar.composestyled:cupertino-liquid:${composestyled}")
implementation("io.github.mflisar.composestyled:fluent2:${composestyled}")
implementation("io.github.mflisar.composestyled:material3:${composestyled}")
implementation("io.github.mflisar.composestyled:iconset-material3:${composestyled}")
implementation("io.github.mflisar.composestyled:iconset-fluent2:${composestyled}")
implementation("io.github.mflisar.composestyled:iconset-sfsymbols:${composestyled}")
implementation("io.github.mflisar.composestyled:theme-cupertino:${composestyled}")
implementation("io.github.mflisar.composestyled:theme-fluent2:${composestyled}")
implementation("io.github.mflisar.composestyled:theme-material3:${composestyled}")
implementation("io.github.mflisar.composestyled:theme-pastel:${composestyled}")
implementation("io.github.mflisar.composestyled:theme-nature:${composestyled}")
```

</details>

# :rocket: Usage

#### Basic usage

Add the `core` module and the implementation module for the theme you want to use.
Wrap your UI in `StyledTheme`, then use the shared `Styled*` components. The active
theme supplies their appearance and behavior.

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = rememberMaterial3ThemeProvider(),
) {
    StyledButton(onClick = { /* Handle the action */ }) {
        StyledText("Continue")
    }
}
```

Browse the [component guides](documentation/Components/), [module overviews](documentation/Modules/)
and [advanced guides](documentation/Advanced/) for details.

Scrollbar indicators default to the platform policy: visible on desktop and web,
hidden on mobile. The same `behavior` setting controls indicators on scrollbar
containers, menus, scrollable tabs and lazy layouts; hiding an indicator does
not disable scrolling. To suppress them throughout a themed subtree, set
`behavior` on `StyledTheme`:

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = rememberMaterial3ThemeProvider(),
    behavior = StyledTheme.Behavior(showScrollbars = false),
) {
    // Styled content
}
```

[`StyledDialog`](documentation/Components/Dialog.md) uses one state and
three-action button API with dialog, toolbar-dialog, fullscreen and bottom-sheet
styles. Its default style is registry-configurable; the adaptive utilities in
`com.michaelflisar.composestyled.adaptive` expose an observable dialog style
from the host window size; read `.value` for the current style. Embedded
`StyledBottomSheet` remains a separate non-modal overlay with the same state and
button model. See the
[Adaptive layouts guide](documentation/Components/Adaptive.md) for its
Scaffold and master/detail layouts, decision policies and Navigation 3 boundary.

# :sparkles: Demo

A [demo project](demo) is included.

# :books: API

Check out the [API documentation](https://MFlisar.github.io/ComposeStyled/api/).

# :bulb: Other Libraries

You can find more of my multiplatform libraries that work well together [here](https://mflisar.github.io/Libraries/).

# :twisted_rightwards_arrows: Compatibility

When combining my libraries, you can find compatibility information [here](https://mflisar.github.io/Libraries/compatibilities/).
