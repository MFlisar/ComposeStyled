[![Maven Central](https://img.shields.io/maven-central/v/io.github.mflisar.composestyled/core?color=blue)](https://central.sonatype.com/artifact/io.github.mflisar.composestyled/core) ![API](https://img.shields.io/badge/api-24%2B-brightgreen.svg) ![Kotlin](https://img.shields.io/github/languages/top/MFlisar/ComposeStyled.svg?color=blueviolet) ![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-blue?label=Kotlin)
# ComposeStyled
![Platforms](https://img.shields.io/badge/PLATFORMS-black?style=for-the-badge) ![Android](https://img.shields.io/badge/android-3DDC84?style=for-the-badge) ![iOS](https://img.shields.io/badge/ios-A2AAAD?style=for-the-badge) ![Windows](https://img.shields.io/badge/windows-5382A1?style=for-the-badge) ![macOS](https://img.shields.io/badge/macos-B0B0B0?style=for-the-badge) ![WebAssembly](https://img.shields.io/badge/wasm-624DE7?style=for-the-badge)

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
> Try the interactive demo:>
> [![Open Interactive Demo](https://img.shields.io/badge/Compose_Styled_Demo-2962FF?style=for-the-badge&logo=googlechrome&logoColor=white)](https://mflisar.github.io/ComposeStyled/demo/)

# :camera: Screenshot

# Table of Contents

- [Supported Platforms](#computer-supported-platforms)
- [Versions](#arrow_right-versions)
- [Setup](#wrench-setup)
- [Usage](#rocket-usage)
- [Demo](#sparkles-demo)
- [API](#books-api)
- [Other Libraries](#bulb-other-libraries)

# :computer: Supported Platforms

| Module | android | ios | windows | macos | wasm | Notes |
|---|---|---|---|---|---|---|
| core | ✅ | ✅ | ✅ | ✅ | ✅ | the core module |
| foundation | ✅ | ✅ | ✅ | ✅ | ✅ | a foundation implementation (can be used as a base for other implementations) |
| cupertino-flat | ✅ | ✅ | ✅ | ✅ | ✅ | a flat Cupertino implementation |
| cupertino-liquid | ✅ | ✅ | ✅ | ✅ | ✅ | a Liquid Glass Cupertino implementation |
| fluent2 | ✅ | ✅ | ✅ | ✅ | ✅ | a fluent2 implementation |
| material3 | ✅ | ✅ | ✅ | ✅ | ✅ | a material3 implementation |

# :arrow_right: Versions

| Dependency | Version |
|---|---|
| Kotlin | `2.4.21` |
| Jetbrains Compose | `1.12.2` |
| Jetbrains Compose Material3 | `1.9.0` |

> :warning: Following experimental annotations are used:
> - **OptIn**
>   - `androidx.compose.material3.ExperimentalMaterial3Api` (3x)
>   - `androidx.compose.ui.ExperimentalComposeUiApi` (1x)
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
```

And then use the definitions in your projects **build.gradle.kts** file like following:

```java
implementation(libs.composestyled.core)
implementation(libs.composestyled.foundation)
implementation(libs.composestyled.cupertino.flat)
implementation(libs.composestyled.cupertino.liquid)
implementation(libs.composestyled.fluent2)
implementation(libs.composestyled.material3)
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

[`StyledDialog`](documentation/Components/Dialog.md) uses one state and
three-action button API with dialog, toolbar-dialog, fullscreen and bottom-sheet
styles. Its default style is registry-configurable; `StyledAdaptive.dialog(platform)`
selects a workspace style from the window size. Embedded `StyledBottomSheet`
remains a separate non-modal overlay with the same state and button model.

# :sparkles: Demo

A [demo project]([demo/app/web, demo/app/windows, demo/shared]) is included.

# :books: API

Check out the [API documentation](https://MFlisar.github.io/ComposeStyled/api/).

# :bulb: Other Libraries

You can find more of my multiplatform libraries that work well together [here](https://mflisar.github.io/Libraries/).

# :twisted_rightwards_arrows: Compatibility

When combining my libraries, you can find compatibility information [here](https://mflisar.github.io/Libraries/compatibilities/).
