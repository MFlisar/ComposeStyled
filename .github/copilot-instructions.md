# Repository guidance

No need to check github status at any time. You can always work with local files!

## Build and verification

Use the Gradle wrapper from the repository root:

- Build all included projects: `./gradlew build` (`.\gradlew.bat build` in PowerShell).
- Run configured checks: `./gradlew check`.
- Run JVM tests for a module: `./gradlew :composestyled:core:jvmTest`.
- Run one JVM test: `./gradlew :composestyled:core:jvmTest --tests "*TestClassName"`.

Replace `core` with `implementations:foundation`, `implementations:cupertino-flat`,
`implementations:fluent2` or `implementations:material3` to target another library
module. Test source sets follow Kotlin Multiplatform conventions; a `jvmTest`
filter matches the test class name.

## Architecture

This is a Kotlin Compose Multiplatform styled-components library. `core` defines
the public `Styled*` component APIs, their implementation contracts, theme
tokens and the component registry. The `implementations` modules supply renderers:
`foundation` provides shared Foundation-based renderers; Cupertino Flat,
Cupertino Liquid and Fluent 2 build on that approach, and Material 3 adapts
native Material 3 components.

Theme factories such as `rememberCupertinoFlatThemeProvider`,
`rememberCupertinoLiquidThemeProvider`,
`rememberFluent2ThemeProvider` and `rememberMaterial3ThemeProvider` remember a theme provider.
Its `*ThemeDefinition` factory creates and remembers a `StyledThemeDefinition` and
executes the theme's `register*Components()` helper inside the provider root.
Add default renderers to those helpers (one `register(...)` call per component;
defaults are registered with it). The theme factories take no registration
callbacks: `StyledTheme(customization = ...)` replaces or adds renderers
(`register(...)`) and updates component defaults through
`update(DefaultsKey) { copy(...) }` (register first, then update). `StyledTheme`
provides the active theme, tokens and registry through CompositionLocals;
public `Styled*` composables resolve their renderer from that registry.
`demo/shared` is a multiplatform consumer and usage reference, while
`demo/app/compose` contains the platform launchers. Keep public API examples and
behavior descriptions in the corresponding `documentation/Components/` files;
use `documentation/_partials/usage.md.partial` for the general usage overview.

`settings.gradle.kts` uses `configs/library-config.yml` and the kmp-devtools
settings plugin to include library modules. Project paths follow the configured
library name (`:composestyled:...` here). Demo projects are included
only when `CI` is not `"true"`.

## Repository-specific conventions

- In Kotlin source, keep structural section-comment blocks separated from
  surrounding code by a blank line before and after. Keep KDoc/function comments
  directly above their declarations without an intervening blank line.
- Keep shared UI APIs and behavior in `commonMain`; use platform source sets only
  for platform-specific code. Library target setup is centralized through the
  kmp-devtools `Targets` helpers in module build scripts.
- A themed component's public contract belongs in `core`: define its typed
  `StyledKey`, `Implementation` interface and public composable there. Add
  adjustable defaults only where useful (a `DefaultsKey` in the key section plus
  `StyledComponentWithDefaults`). Classify files by purpose, independently of
  their renderer model: structural containers (including Scaffold, dialogs and
  bottom sheets) belong in `ui/layout`; controls and content components belong
  in `ui/components`. Mirror this distinction in renderer packages. Layouts may
  have registered theme renderers; shared compositions do not need a registry
  entry solely because they live in either folder. Follow the component guides.
- Compounds must compose existing public Styled controls for equivalent themed
  parts, not rebuild them with Foundation interaction modifiers or merely copy
  their defaults. A renderer override must affect standalone and compound usage
  alike. Trace all consumers, shared bases and variants when migrating a control;
  follow the complete, automatically scoped guides linked from
  `.github/instructions/styled-component.instructions.md` for component changes.
- Theme-level customization is expressed through `StyledTheme(customization)`
  and `StyledRegistry`; scoped overrides can use `UpdateStyledRegistry`. Reuse
  `StyledTheme` tokens and existing shared Foundation renderers rather than
  hard-coding theme values into the core API.
- For visual or behavioral analysis of Material 3, Cupertino or Fluent 2
  components, consult the [design-system guide](../.guidelines/README.md) and
  the relevant platform page.
  These pages are source-linked design references, not API contracts; keep
  platform-specific guidance and documented gaps in mind.
- Dependency and plugin versions are managed through the catalogs in `gradle/`
  (`libs`, `app`, `deps` and `mflisar`); use catalog aliases in Gradle scripts.