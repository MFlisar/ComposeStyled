# Repository guidance

No need to check github status at any time. You can always work with local files!

## Build and verification

Use the Gradle wrapper from the repository root:

- Build all included projects: `./gradlew build` (`.\gradlew.bat build` in PowerShell).
- Run configured checks: `./gradlew check`.
- Run JVM tests for a module: `./gradlew :composestylecomponents:core:jvmTest`.
- Run one JVM test: `./gradlew :composestylecomponents:core:jvmTest --tests "*TestClassName"`.

Replace `core` with `implementations:foundation`, `implementations:cupertino`,
`implementations:fluent2` or `implementations:material3` to target another library
module. Test source sets follow Kotlin Multiplatform conventions; a `jvmTest`
filter matches the test class name.

## Architecture

This is a Kotlin Compose Multiplatform styled-components library. `core` defines
the public `Styled*` component APIs, their implementation contracts, theme
tokens and the component registry. The `implementations` modules supply renderers:
`foundation` provides shared Foundation-based renderers, Cupertino and Fluent 2
build on that approach, and Material 3 adapts native Material 3 components.

Theme factories such as `rememberCupertinoTheme`,
`rememberFluent2Theme` and `rememberMaterial3Theme` remember a theme provider.
Its `*ThemeDefinition` factory creates and remembers a `StyledThemeDefinition` and
executes the theme's `register*Components()` helper through its default
registration callback inside the provider root. Add default renderers to those
helpers; use the custom registration callback for overrides. `StyledTheme`
provides the active theme, tokens and registry through CompositionLocals;
public `Styled*` composables resolve their renderer from that registry.
`demo/shared` is a multiplatform consumer and usage reference, while
`demo/app/compose` contains the platform launchers. Keep public API examples and
behavior descriptions in `documentation/_partials/usage.md.partial` aligned
with component changes.

`settings.gradle.kts` uses `configs/library-config.yml` and the kmp-devtools
settings plugin to include library modules. Project paths follow the configured
library name (`:composestylecomponents:...` here). Demo projects are included
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
  adjustable defaults only where useful. Generic layouts and compounds do not
  need their own registry entry; follow the component guide below.
- Compounds must compose existing public Styled controls for equivalent themed
  parts, not rebuild them with Foundation interaction modifiers or merely copy
  their defaults. A renderer override must affect standalone and compound usage
  alike. Trace all consumers, shared bases and variants when migrating a control;
  follow the complete, automatically scoped guides linked from
  `.github/instructions/styled-component.instructions.md` for component changes.
- Theme-level customization is expressed through implementation parameters and
  `StyledRegistry`; scoped overrides can use `UpdateStyledRegistry`. Reuse
  `StyledTheme` tokens and existing shared Foundation renderers rather than
  hard-coding theme values into the core API.
- Dependency and plugin versions are managed through the catalogs in `gradle/`
  (`libs`, `app`, `deps` and `mflisar`); use catalog aliases in Gradle scripts.