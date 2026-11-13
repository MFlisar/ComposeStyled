# ComposeChangelog

The `composechangelog` module provides a Kotlin-authored changelog rendered with
Styled components. It has no Material 3 or external Changelog dependency.

## Define releases

Create releases with `changelogReleases`. Release groups use the Keep a Changelog
categories; their order in the DSL is their display order. Releases themselves
are sorted by descending `versionCode`.

```kotlin
val setup = remember {
    ChangelogDefaults.setup(
        releases = changelogReleases {
            release(name = "1.2.0", code = 10_200, date = "2024-06-01") {
                changed {
                    item("Release groups follow the order declared here.")
                }
                added {
                    item("A new feature.")
                    item("Another addition.")
                }
                fixed {
                    item("A corrected issue.")
                }
            }
        },
        textFormatter = ChangelogDefaults.textFormatter(
            color = mapOf("primary" to { StyledTheme.colorScheme.primary }),
        ),
    )
}
```

The other supported groups are `deprecated`, `removed`, and `security`.
Category colors reflect their meaning: additions are green, changes blue,
deprecations amber, removals rose, fixes teal, and security updates red. Each
color has light- and dark-theme variants. Release cards show the categories
they contain as colored dot-and-label tags; dot size scales with the tag text.
Each release uses `ChangelogVersion`, which stores the display `name` and an
`Int` `code`. The release with the highest code determines the current app
version for automatic update display, so at least one release is required.
Codes determine release ordering and update filtering; names are displayed
directly. Entry text supports nested `<b>`, `<i>`, and `<u>` tags. Named colors
from the `color` map can be written as `<primary>text</primary>` or
`<color=primary>text</color>`.
Each release may include an optional `date` string, displayed as provided.
Release names, codes, and dates are available eagerly so setup can determine
the current app version. The groups and items inside each release block are
deferred. When `ChangelogContent` first becomes visible, it creates those
groups, applies the filter and version threshold, sorts the result, and
remembers it. Its `StyledLazyColumn` then composes each release's UI as that
release enters the visible list. Wrap setup in `remember` when it is created
during composition.
`ChangelogDefaults.setup` also accepts a release `filter` and a `textFormatter`
from `String` to `AnnotatedString`.

For a header card containing formatted changelog entries, use `headerCard`
inside a release. Its `colors` argument is optional and accepts a
`CustomColors` value:

```kotlin
headerCard(
    title = "IMPORTANT INFO",
    colors = CustomColors.Primary,
) {
    item("Some info for you")
    item("And some more info...")
}
```

For arbitrary custom content, use `custom { ... }` inside a release and compose
Styled components there. The extension is scoped to the release rather than
stored in a global renderer registry. `ChangelogHeaderCard(title, colors) { ... }`
is also available as a reusable card; within `ChangelogContent`,
`ChangelogEntryText(text)` uses the provided setup to format and render an
entry without passing setup through each custom-content call.

## Display and state

`ChangelogContent` owns a scrolling `StyledLazyColumn`; give it the available
space and do not place it inside another vertical lazy list:

```kotlin
val state = rememberChangelogState()

ChangelogContent(
    modifier = Modifier.fillMaxSize(),
    state = state,
    setup = setup,
)
```

Use `state.show()` for all configured releases, `state.show(minimumVersion)`
to show releases at or above that version code, and `state.hide()` to close it.
The `rememberChangelogState(visible, minimumVisibleReleaseVersion)` overload
sets the initial manual state.
Use `setup.versionName(versionCode)` to get a configured release's display name
for a version-code label; it reports an error if that code has no release.

For automatic update display, provide the last shown version code and a suspend
callback that persists the new code:

```kotlin
val state = rememberChangelogState(
    setup = setup,
    lastShownVersion = storedVersionCode,
    saveLastShownVersion = { versionCode -> saveVersionCode(versionCode) },
)
```

On first launch, the current app version code is saved without opening the
changelog. On updates, the current code is saved and the state shows releases
from `lastShownVersion + 1`.
