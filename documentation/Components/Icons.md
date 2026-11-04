# Icons

`StyledIcons` exposes the common semantic icons supplied by the active theme's
registry. Use them with `StyledIcon` when an icon should follow the selected
theme implementation:

```kotlin
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.components.StyledIcon

StyledIcon(
    icon = StyledIcons.Search,
    contentDescription = "Search",
)
```

The Core set contains `Add`, `NavigateBack`, `Cancel`, `Check`, `Clear`,
`Close`, `Delete`, `Dropdown`, `Edit`, `Menu`, `More`, `Remove`, `Search`,
`NavigateForward`, `Submenu`, `Refresh`, `Save`, `Home`, `Settings`, `Account`,
`Notifications`, `Info`, `Help`, `Favorite`, `Share`, `Filter`, `Sort`, `Copy`,
`Paste` and `Cut`. The IDs are semantic: `Remove` is a minus action while
`Delete` is destructive deletion. `StyledIcon` still accepts any
`IconComposable`, so app-specific vectors, painters and resources can be used
directly without registering them.

Ready-to-use icon families live in separate modules:

| Provider | Icon style |
|---|---|
| `Material3FilledIconSet`, `Material3OutlinedIconSet` | `iconset-material3` |
| `Fluent2RegularIconSet`, `Fluent2FilledIconSet` | `iconset-fluent2` |
| `SFSymbolIconSet` | `iconset-sfsymbols` |

Each theme provider supplies a required `defaultIconSet`: Material 3 uses filled,
Fluent 2 uses regular, and Cupertino uses `SFSymbolIconSet`. `StyledTheme`
registers that default before applying the customization block, so registering
another `StyledIconSet` there replaces it. Icon-family modules depend on Core;
they are not bundled in the Core module.

`SFSymbolIconSet` is available from common code. It uses Apple's native SF
Symbols API on iOS and the separate Material filled icon set on other targets.
No SF Symbol SVGs are bundled with the library. Apple's system symbol API is
available starting with iOS 13. See [Apple's SF Symbols overview](https://developer.apple.com/sf-symbols/).

The iOS mapping is:

| Core icon | SF Symbol |
|---|---|
| `Add` | `plus` |
| `NavigateBack` | `chevron.left` |
| `NavigateForward` | `chevron.right` |
| `Submenu` | `chevron.right` |
| `Cancel` | `xmark.circle` |
| `Check` | `checkmark` |
| `Clear` / `Close` | `xmark` |
| `Delete` | `trash` |
| `Dropdown` | `chevron.down` |
| `Edit` | `pencil` |
| `Menu` | `line.3.horizontal` |
| `More` | `ellipsis` |
| `Remove` | `minus` |
| `Search` | `magnifyingglass` |
| `Refresh` | `arrow.clockwise` |
| `Save` | `square.and.arrow.down` |
| `Home` | `house` |
| `Settings` | `gear` |
| `Account` | `person.crop.circle` |
| `Notifications` | `bell` |
| `Info` | `info.circle` |
| `Help` | `questionmark.circle` |
| `Favorite` | `heart` |
| `Share` | `square.and.arrow.up` |
| `Filter` | `line.3.horizontal.decrease` |
| `Sort` | `arrow.up.arrow.down` |
| `Copy` | `doc.on.doc` |
| `Paste` | `doc.on.clipboard` |
| `Cut` | `scissors` |

SF Symbols has no dedicated save icon; `square.and.arrow.down` is the closest
system symbol, but can also communicate downloading or exporting.

Replace the active set through the same registry key:

```kotlin
import com.michaelflisar.composestyled.ui.icons.material3.Material3OutlinedIconSet

StyledTheme(
    themeProvider = themeProvider,
    customization = {
        register(Material3OutlinedIconSet)
    },
) {
    // App content
}
```

`SFSymbolIconSet` is in the `iconset-sfsymbols` module and can be registered
with another theme from common code:

```kotlin
import com.michaelflisar.composestyled.ui.icons.sfsymbols.SFSymbolIconSet

StyledTheme(
    themeProvider = themeProvider,
    customization = {
        register(SFSymbolIconSet)
    },
) {
    // App content
}
```

## Replacing the core set

Replace the complete Core set by registering another `StyledIconSet`
under the same key. A provider may delegate unchanged symbols to one of the
included sets:

```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIconSet
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.icons.material3.Material3FilledIconSet

private val appSearch = Icons.Default.Search.toIconComposable()

class AppCoreIcons : StyledIconSet {
    private val defaults = Material3FilledIconSet

    override fun resolve(icon: StyledIcons.Name): IconComposable =
        when (icon) {
            StyledIcons.Name.Search -> appSearch
            else -> defaults.resolve(icon)
        }
}

StyledTheme(
    themeProvider = themeProvider,
    customization = {
        register(AppCoreIcons())
    },
) {
    // App content
}
```

The provider can delegate icons it does not replace to
an included icon set. Registry replacements are scoped to this theme
composition; use `UpdateStyledRegistry` for a local override.

## Adding app-specific icons

For icons outside the semantic Core set, define an app-owned `StyledKey` and
`StyledComponent` provider, then register it in the same customization block.
This keeps app-specific icon names out of the core API and lets the app choose
different providers for different theme implementations.

The Material icon vectors are distributed under Apache 2.0; Fluent UI System
icons are distributed under the MIT license. Their Compose vector source files
are downloaded from [Composables](https://composables.com/icons) and retain
their respective license notices.
