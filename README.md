[![Maven Central](https://img.shields.io/maven-central/v/io.github.mflisar.composestyledcomponents/core?color=blue)](https://central.sonatype.com/artifact/io.github.mflisar.composestyledcomponents/core) ![API](https://img.shields.io/badge/api-24%2B-brightgreen.svg) ![Kotlin](https://img.shields.io/github/languages/top/MFlisar/ComposeStyledComponents.svg?color=blueviolet) ![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-blue?label=Kotlin)
# ComposeStyledComponents
![Platforms](https://img.shields.io/badge/PLATFORMS-black?style=for-the-badge) ![Android](https://img.shields.io/badge/android-3DDC84?style=for-the-badge) ![iOS](https://img.shields.io/badge/ios-A2AAAD?style=for-the-badge) ![Windows](https://img.shields.io/badge/windows-5382A1?style=for-the-badge) ![macOS](https://img.shields.io/badge/macos-B0B0B0?style=for-the-badge) ![WebAssembly](https://img.shields.io/badge/wasm-624DE7?style=for-the-badge)

ComposeStyledComponents is a Kotlin Multiplatform library for Compose. It provides
themed UI components through a consistent API, making it easy to build
applications with different design systems.

**Main features**
- `Styled*` components for common controls, navigation, and layouts
- Ready-to-use themes and renderers for Material 3, Cupertino, and Fluent 2
- Shared design tokens for colors, spacing, and other theme values
- Replaceable renderers through a central component registry
- Customizable themes and targeted overrides for individual components
- Foundation-based renderers as a starting point for custom implementations
- Support for Android, iOS, Windows, macOS, and WebAssembly

> [!TIP]
> Try the interactive demo:>
> [![Open Interactive Demo](https://img.shields.io/badge/Compose_Styled_Components_Demo-2962FF?style=for-the-badge&logo=googlechrome&logoColor=white)](https://mflisar.github.io/ComposeStyledComponents/demo/)

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
| cupertino | ✅ | ✅ | ✅ | ✅ | ✅ | a cupertino implementation |
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
>
> I try to use as less experimental features as possible, but in this case the ones above are needed!

# :wrench: Setup

<details open>

<summary><b>Using Version Catalogs</b></summary>

<br>

Define the dependencies inside your **libs.versions.toml** file.

```toml
[versions]

composestyledcomponents = "<LATEST-VERSION>"

[libraries]

composestyledcomponents-core = { module = "io.github.mflisar.composestyledcomponents:core", version.ref = "composestyledcomponents" }
composestyledcomponents-foundation = { module = "io.github.mflisar.composestyledcomponents:foundation", version.ref = "composestyledcomponents" }
composestyledcomponents-cupertino = { module = "io.github.mflisar.composestyledcomponents:cupertino", version.ref = "composestyledcomponents" }
composestyledcomponents-fluent2 = { module = "io.github.mflisar.composestyledcomponents:fluent2", version.ref = "composestyledcomponents" }
composestyledcomponents-material3 = { module = "io.github.mflisar.composestyledcomponents:material3", version.ref = "composestyledcomponents" }
```

And then use the definitions in your projects **build.gradle.kts** file like following:

```java
implementation(libs.composestyledcomponents.core)
implementation(libs.composestyledcomponents.foundation)
implementation(libs.composestyledcomponents.cupertino)
implementation(libs.composestyledcomponents.fluent2)
implementation(libs.composestyledcomponents.material3)
```

</details>

<details>

<summary><b>Direct Dependency Notation</b></summary>

<br>

Simply add the dependencies inside your **build.gradle.kts** file.

```kotlin
val composestyledcomponents = "<LATEST-VERSION>"

implementation("io.github.mflisar.composestyledcomponents:core:${composestyledcomponents}")
implementation("io.github.mflisar.composestyledcomponents:foundation:${composestyledcomponents}")
implementation("io.github.mflisar.composestyledcomponents:cupertino:${composestyledcomponents}")
implementation("io.github.mflisar.composestyledcomponents:fluent2:${composestyledcomponents}")
implementation("io.github.mflisar.composestyledcomponents:material3:${composestyledcomponents}")
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
    theme = rememberMaterial3Theme(),
) {
    StyledButton(onClick = { /* Handle the action */ }) {
        StyledText("Continue")
    }
}
```

Browse the [component guides](documentation/Components/), [module overviews](documentation/Modules/)
and [advanced guides](documentation/Advanced/) for details.

# :sparkles: Demo

A [demo project]([demo/app/web, demo/app/windows, demo/shared]) is included.

# :books: API

Check out the [API documentation](https://MFlisar.github.io/ComposeStyledComponents/api/).

# :bulb: Other Libraries

You can find more of my multiplatform libraries that work well together [here](https://mflisar.github.io/Libraries/).

# :twisted_rightwards_arrows: Compatibility

When combining my libraries, you can find compatibility information [here](https://mflisar.github.io/Libraries/compatibilities/).
