[![Maven Central](https://img.shields.io/maven-central/v/io.github.mflisar.composestyledcomponents/core?style=for-the-badge&color=blue)](https://central.sonatype.com/artifact/io.github.mflisar.composestyledcomponents/core) ![API](https://img.shields.io/badge/api-24%2B-brightgreen.svg?style=for-the-badge) ![Kotlin](https://img.shields.io/github/languages/top/MFlisar/ComposeStyledComponents.svg?style=for-the-badge&amp;color=blueviolet) ![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-blue?style=for-the-badge&amp;label=Kotlin)
# ComposeStyledComponents
![Platforms](https://img.shields.io/badge/PLATFORMS-black?style=for-the-badge) ![Android](https://img.shields.io/badge/android-3DDC84?style=for-the-badge) ![iOS](https://img.shields.io/badge/ios-A2AAAD?style=for-the-badge) ![Windows](https://img.shields.io/badge/windows-5382A1?style=for-the-badge) ![macOS](https://img.shields.io/badge/macos-B0B0B0?style=for-the-badge) ![WebAssembly](https://img.shields.io/badge/wasm-624DE7?style=for-the-badge)

> [!NOTE]
> This library currently is for my personal usage only.
> I do use it in production, but have not yet documented it properly.

# Table of Contents

- [Supported Platforms](#computer-supported-platforms)
- [Versions](#arrow_right-versions)
- [Setup](#wrench-setup)
- [Usage](#rocket-usage)
- [Demo](#sparkles-demo)
- [API](#books-api)
- [Other Libraries](#bulb-other-libraries)

# :computer: Supported Platforms

| Module | android | iOS | windows | macOS | wasm | Notes |
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
>   - `com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi` (84x)
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

## Lazy and grid layouts

The common layout helpers include `StyledLazyColumn`, `StyledLazyRow`,
`StyledLazyVerticalGrid` and `StyledLazyHorizontalGrid`. They expose the
corresponding Foundation state, content padding, layout, fling and scroll
parameters without requiring theme registration. Their `StyledSpaced*`
counterparts use `StyledTheme.spacing.medium` as the default item spacing.
Grid variants take `GridCells` through `columns` or `rows`.

```kotlin
StyledSpacedLazyColumn {
    items(messages) { message ->
        StyledText(message.title)
    }
}

StyledSpacedLazyVerticalGrid(columns = GridCells.Adaptive(160.dp)) {
    items(products) { product ->
        ProductCard(product)
    }
}
```

## Scrollbar containers

`StyledVerticalScrollbarContainer` and `StyledHorizontalScrollbarContainer`
share their scrolling, alignment and overlap behavior across platforms. Android
uses a custom draggable scrollbar from `featureCustomScrollbarMain`. iOS,
Windows (JVM), macOS and Wasm use Compose Foundation scrollbars from
`featureComposeScrollbarMain`, selected through an internal `expect`/`actual`
function. The public container APIs are identical on all targets.

## Buttons

`StyledButton` requires a semantic `role`: `Primary`, `Secondary` or `Tertiary`.
`StyledPrimaryButton`, `StyledSecondaryButton` and `StyledTertiaryButton` provide
convenience functions. Concrete styles are selected by each theme's configurable
`roleMapping(role, context)` lambda, not by a visual variant in the Core API.
Context is owned by its source component and exposed through the open
`StyledContext` contract. `StyledSnackbar.Context.SnackbarAction` is provided
only around the snackbar action slot; `StyledDialog.Context.DialogAction` is
available for dialog action scopes. Clickable `StyledGroupBox` content maps to
each theme's outlined button style. Input leading/trailing slots provide their
own contexts; buttons preserve their standard role mapping there.

| Theme | Primary | Secondary | Tertiary | SnackbarAction |
|-------|---------|-----------|----------|---------------|
| Material 3 | Primary | FilledTonal | Text | Text for all roles |
| Cupertino | Filled | Tinted | Plain | Plain for all roles |
| Fluent 2 | Primary | Secondary | Subtle | Transparent for all roles |

Cupertino Secondary uses a translucent accent background with accent-colored
text and icons, not `onPrimary` (which belongs to the opaque Primary button).
An explicit `filledForeground` override remains supported for the tinted style.

Cupertino buttons retain `bodyMedium` typography and use compact vertical padding.
Buttons and icon buttons have a minimum 44 dp footprint, increased when the
platform's minimum interactive size is larger, subject to caller constraints.
Button content padding is inside that minimum size, not added on top of it.
Larger content and explicit caller sizes can still expand the surface.
This also applies to dropdown anchors and dialog/snackbar actions, which reuse
the same button renderers. Floating action buttons retain their Small, Standard
and Large sizes.

The snackbar source supplies its context through an internal-API local; normal
button mapping remains active outside that source scope. All three renderer
factories accept a custom mapping; delegate to `DefaultRoleMapping` for cases
that should retain the theme defaults:

```kotlin
Material3Button.create(
    roleMapping = { role, context ->
        if (context == StyledContext.Default &&
            role == StyledButton.Role.Secondary
        ) {
            Material3Button.Variant.Outlined
        } else {
            Material3Button.DefaultRoleMapping(role, context)
        }
    },
)
```

## Icon buttons

`StyledIconButton` is a separately registered control with the semantic roles
`Primary`, `Secondary` and `Tertiary`, matching `StyledButton`. Each theme maps
these roles to its own icon-button styles. It supports `shape`, `interactionSource`
and `StyledIconButtonDefaults.customize(CustomColors, border)`, and is registered by all three built-in
themes. The content overload accepts an arbitrary composable slot; the
`IconComposable` overload applies the configured icon size and optional padding.
Use `StyledPrimaryIconButton`, `StyledSecondaryIconButton` or
`StyledTertiaryIconButton` as convenience functions.

| Theme | Primary | Secondary | Tertiary |
|-------|---------|-----------|----------|
| Material 3 | Filled | Outlined | Standard |
| Cupertino | Filled | Filled tonal | Standard |
| Fluent 2 | Filled | Neutral secondary | Standard |

Fluent 2 buttons and icon buttons share an internal color resolver for default,
hover, pressed and disabled states. Secondary uses a neutral surface and a
contrasting neutral foreground in both controls, rather than a Primary-tinted
icon surface with Primary-colored content. The icon renderer's existing
`FilledTonal` variant name is retained and now selects this neutral secondary
appearance. Both controls honor `filledBackground`/`filledForeground` overrides
for Primary and Secondary, and `textForeground` for transparent/outlined styles.

```kotlin
StyledIconButton(
    icon = Icons.Default.Add.toIconComposable(),
    contentDescription = "Add item",
    onClick = { addItem() },
)
```

Use `CustomColors` for role-aware customization: filled styles use
`filledBackground`/`filledForeground`, while standard or outlined styles use
`textForeground`; `border` optionally overrides the outline.

## Chips

`StyledChip` is clickable, while `StyledSelectableChip` also exposes a
`selected` state. Both accept optional leading and trailing `IconComposable`
values. `StyledChipDefaults.colors(...)` customizes the unselected `container`,
`content` and `border`, with optional `selectedContainer`, `selectedContent` and
`selectedBorder` overrides for selected chips. Omitted colors retain each
renderer’s defaults. Theme renderers map these two generic behaviors to native
controls;
Material 3 defaults to `ElevatedAssistChip` and `ElevatedFilterChip` to avoid
the hover-background issue in its flat chip variants. Override that mapping by
registering a configured `Material3Chip.create(...)` through
`registerCustomComponents` with `rememberMaterial3Theme(...)`.

## Segmented buttons

`StyledSegmentedButton` represents one segment. `StyledSegmentedButtonRow` and
`StyledSegmentedButtonRowMultiSelect` are the convenient item-list APIs for
single- and multi-selection. Both rows use the same item content lambda
`(item, selected) -> Unit`, independent of dropdown render contexts. Use
`StyledSegmentedButtonRowDefaults.defaultRenderer()` for a text label, or supply
a custom renderer when a segment needs additional content such as an icon.

The row APIs support selected values or indices, stateful and controlled
overloads, enabled state, item shape, item content padding, minimum item width
and original-item index callbacks. No manually composed-segment overload is
needed on the rows. `itemShape` controls each segment's shape within the row;
`itemContentPadding` applies inside each segment. `minItemWidth` defaults to
the M3 minimum width (58 dp). Segments use equal weights; without an explicit
fill/fixed width, the row measures intrinsically while respecting that minimum.
Use `Modifier.fillMaxWidth()` to make segments divide the full available width.
The default text renderer wraps long labels instead of clipping them.
Foundation-based themes reserve the check-indicator slot in both selection
states and animate the label from its centered inactive position to beside the
check, so selecting an item does not resize the segment.

```kotlin
val items = listOf("List", "Grid", "Gallery")
val content = StyledSegmentedButtonRowDefaults.defaultRenderer<String>()

StyledSegmentedButtonRow(
    items = items,
    selected = selected,
    minItemWidth = 58.dp,
    content = content,
)
StyledSegmentedButtonRowMultiSelect(
    items = items,
    selected = selectedItems,
    minItemWidth = 58.dp,
    content = content,
)

StyledSegmentedButton(selected = isSelected, onClick = { isSelected = !isSelected }) {
    StyledText("One segment")
}
```

Material 3 delegates to its native single- and multi-choice segmented-button
rows through one registered row renderer, which composes the registered
`StyledSegmentedButton` for each item. It uses native item shapes, colors, check
indicators and item content padding by default. Cupertino and Fluent 2 use the
shared Foundation item and row renderers with Styled theme tokens; their selected
segments use `secondarySurface` and `onSecondarySurface`, matching Material 3's
`secondaryContainer` and `onSecondaryContainer` defaults. An
out-of-range index displays no single selection; set `forceSelection = false`
to clear the active segment when clicked again.

## Picker

`StyledPicker` displays non-editable text with a clickable end icon. It is
composed from `StyledInput` and `StyledInputAction`, so it uses the active theme
without a separate renderer. Provide an `IconComposable` (for example, with
`Icons.Default.ArrowDropDown.toIconComposable()`),
and handle the action with `onStartPicker`. The picker accepts a controlled
string and supports both input variants. Set
`clickOnIconOnly = false` to make the entire field clickable instead of only its
icon. The default comes from `StyledPickerDefaults.clickOnIconOnly` and is `true`.

```kotlin
StyledPicker(
    value = selectedOption,
    onStartPicker = { openOptions() },
    icon = Icons.Default.ArrowDropDown.toIconComposable(),
    label = "Selection",
)
```

## Number picker

`StyledNumberPicker` is a compound control built from `StyledInput` and
`StyledTertiaryIconButton`, so it follows the active theme without a separate
renderer registration. Supply a `NumberParser` for parsing, formatting and step
changes, and a `NumberValidator` for input validation. It supports controlled
values, a `MutableState` overload, optional increment/decrement buttons and
wrapping between the values in `overrun`. Inputs use `StyledInput` for the default style and `StyledAlternativeInput` for
the alternative style. Number pickers provide matching
`StyledNumberPicker` and `StyledAlternativeNumberPicker` functions, each with
controlled and `MutableState` value overloads.

```kotlin
val value = remember { mutableStateOf(0) }
val parser = rememberNumberParser(sample = 0, stepSize = 1)
val validator = rememberNumberValidator(value.value) { candidate ->
    if (candidate == null || candidate !in 0..100) {
        ValidationResult.Invalid("Enter a value between 0 and 100")
    } else {
        ValidationResult.Valid
    }
}

StyledNumberPicker(value, validator, parser)
```

## Progress indicators

`StyledProgressIndicator` supports circular and linear renderings. The
`CircularProgressIndicator` and `LinearProgressIndicator` convenience functions
select the matching type. A `null` progress value displays an indeterminate
indicator; values from `0f` to `1f` display determinate progress. Values outside
that range are clamped.

```kotlin
CircularProgressIndicator()
LinearProgressIndicator(progress = 0.65f, modifier = Modifier.fillMaxWidth())
```

## Labeled components

`StyledGroupBox` draws a border with a floating label around its content. Pass
`onClick` to make it interactive; it uses the active theme's outlined-button
renderer for button semantics, press/hover feedback and colors. The `content`
slot is not a `ColumnScope`. `contentPadding` is forwarded around the content in
both clickable and non-clickable group boxes and defaults to zero; the outlined
button's own content padding and border are disabled. The group box always draws
its border and label cutout; the outlined button supplies the theme-specific
interaction and content styling.
`StyledLabeledInformation` and `StyledLabeledInformationHorizontal` provide
vertical and horizontal label/value layouts; both offer a string value overload
and a composable value slot. These generic compounds use Styled text, layout,
spacing, padding and card renderers.

```kotlin
StyledGroupBox(label = "Email", onClick = { openEmail() }) {
    StyledText("name@example.com")
}

StyledLabeledInformationHorizontal(label = "Status", info = "Ready")
```

## Scaffold components

### Tooltips

`StyledTooltip` wraps its anchor and shows a tooltip on the platform's usual
input gestures. User input is enabled by default: the Foundation renderer
supports hover, focus and touch/stylus long-press, while Material 3 delegates
these interactions to its native tooltip components. The shared state defaults
to initially hidden and non-persistent, matching Material 3.

```kotlin
val tooltipState = rememberStyledTooltipState()

StyledTooltip(
    state = tooltipState,
    tooltip = { StyledText("A short description for this control.") },
) {
    StyledButton(onClick = {}) {
        StyledText("Hover or long-press")
    }
}
```

Customize the tooltip with `shape`, `containerColor`, `contentColor`,
`maxWidth`, `shadowElevation` and `contentPadding`. Its defaults are supplied
by the active theme. `rememberStyledTooltipState(initialIsVisible, isPersistent)`
controls its initial visibility and timeout behavior; `state.show()` suspends
until dismissal and `state.dismiss()` hides it. Set `enableUserInput = false`
to disable automatic hover, focus and long-press triggers.

### Standalone dropdown menus

`StyledPopupContainer` is the single anchored-overlay container. It measures
the anchor and supplies a scope for its `popup` slot. It does not own visibility
or dismissal; compose either `StyledPopup` or `StyledPopupMenu` in the slot.
Generic popups, menus and future tooltips use this same container.

```kotlin
val infoExpanded = remember { mutableStateOf(false) }

StyledPopupContainer(
    popup = {
        StyledPopup(
            visible = infoExpanded,
            content = {
                StyledText(
                    modifier = Modifier.padding(StyledTheme.padding.medium),
                    text = "A short explanation of this option.",
                )
            },
        )
    },
    content = {
        StyledButton(onClick = { infoExpanded.value = true }) {
            StyledText("About")
        }
    },
)
```

`StyledPopupMenu` is the menu-specific registered component and has
`ColumnScope` content. Dismissal does not automatically select or close items,
so callbacks update the caller-owned `expanded` state:

```kotlin
val menuExpanded = remember { mutableStateOf(false) }

StyledPopupContainer(
    popup = {
        StyledPopupMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
        ) {
            StyledDropdownMenuItem(
                text = { StyledText("Action") },
                onClick = { menuExpanded.value = false },
            )
        }
    },
    content = {
        StyledButton(onClick = { menuExpanded.value = true }) {
            StyledText("Open menu")
        }
    },
)
```

`StyledPopupMenu.WidthConfig.Auto(maxWidth = 280.dp, limitToContainerWidth = false)`
wraps content with an optional width cap. `MatchParent(maxWidth = null)` matches
the measured container width exactly, optionally capped; unlike the legacy
minimum-width behavior it cannot grow beyond that width. Existing `MatchAnchor`,
`AtLeastAnchor`, `WrapContent` and `Fixed` options are also available.
Customize `offset`, `scrollState`, `maxHeight`, `shape`, `containerColor`,
`contentColor`, `shadowElevation` and `border`. Material-only `tonalElevation`
is replaced by explicit Styled colors; no tonal overlay is applied.
Menu defaults are centralized in `StyledPopupMenuDefaults`, including width
configuration, size limits, offset, shape, colors, shadow, border and content
padding. Theme-dependent defaults are read from the current Styled locals.

Use `StyledDropdownMenuItem(text = { StyledText("Action") }, onClick = { ... })`
inside the menu. It supports `leadingIcon`, `trailingIcon`, `enabled`,
`interactionSource`, `contentPadding`, outer `itemPadding` and `shape`.
`StyledDropdownMenuItemDefaults.colors()` configures text and both icon colors,
including independent disabled colors. Slots inherit Styled content locals.
Items provide keyboard activation and hover/focus/press feedback.
Single- and multi-select dropdowns use this same menu component.
The menu's nullable `header` slot defaults to `null`. Material3 passes the supplied
`scrollState` directly to its native `DropdownMenu`; header and items scroll
together without an inner scroll container. Cupertino and Fluent2 keep the header
above their scrollable list. Dropdown filters and multi-selection actions use
that slot. `contentPadding`
controls padding within the scrollable section.

`StyledDropdownButton(menuContent = ..., expanded = ...) { StyledText("Actions") }`
combines a themed `StyledButton`, an animated expand icon and a Styled dropdown
menu. `expanded` is optional caller-owned state; by default it is remembered.
The `menuContent` slot has `ColumnScope` and receives a `dismiss` callback.
Compose `StyledDropdownMenuItem`, `StyledHorizontalDivider` or arbitrary content
directly; there is no separate entry model. Call `dismiss()` after an action to
close the menu, or omit it to keep the menu open.
The menu is hidden and closed when the button is disabled.
An `onClick` overload performs a direct action without opening a menu.

Use `role` for any Styled button role (default: `Primary`), or
`StyledDropdownPrimaryButton`, `StyledDropdownSecondaryButton` and
`StyledDropdownTertiaryButton` as convenience overloads. Each has both an
`onClick` and a `menuContent` overload. Shared button parameters follow
`StyledButton` ordering: role, action, modifier, enabled, interactionSource,
shape, customization and content, with menu state/configuration where applicable.
Button customization uses `shape`,
`customization = StyledButtonDefaults.customize(...)` and `interactionSource` instead of
Material button colors/elevation types. Menu width, shape, colors, offset and
maximum height are separately adjustable with the `menu*` parameters.

```kotlin
StyledDropdownButton(
    menuContent = { dismiss ->
        StyledDropdownMenuItem(
            text = { StyledText("Save") },
            onClick = {
                save()
                dismiss()
            },
        )
    },
) {
    StyledText("Actions")
}
```

All theme definition factories expose `popup`, `popupMenu` and `dropdownMenuItem`
renderer overrides. `popup` customizes generic overlays; Foundation-based
menus in Cupertino and Fluent 2 delegate through it, while Material 3 menus use
the `popupMenu` renderer and native `DropdownMenu`. Menu-item renderers expose
`defaults(minHeight, itemPadding,
itemShape)` via their `create(defaults = ...)` factories; individual items can
override these through `minHeight`, `itemPadding` and `shape`.
Single- and multi-select dropdowns compose `StyledDropdownMenuItem` directly,
inheriting the active renderer's styling and interaction behavior. Their
radio-button and checkbox selection semantics are preserved.
Foundation renderers contain no theme identity flags;
Cupertino and Fluent 2 supply their own menu-item interaction styling.

Pass a `StyledThemeProvider` to `StyledTheme`. Styled token locals are provided
first, followed by the provider's native root. The provider then calls
`*ThemeDefinition()` inside that root, allowing renderer defaults to read both
Styled and native theme locals. Finally, registry and theme name are provided
to the app content.

```kotlin
StyledTheme(
    platform = platform,
    theme = rememberFluent2Theme(),
) {
    StyledText("Hello")
}
```

For custom components, register them after the theme's default components:

```kotlin
val provider = rememberMaterial3Theme(
    customizeColorScheme = { it.copy(primary = Color.Red) },
    registerCustomComponents = {
        register(Material3Card.create())
    },
)
```

`remember*Theme()` remembers the provider using its configuration as keys;
renderer creation happens inside the provider's composition. The
`registerDefaultComponents` callback can replace the complete set of theme
defaults; `registerCustomComponents` runs afterward, so it can add components
or override a default registration, as in the example.

Registered components implement `StyledComponent<T>` and provide their
`StyledKey<T>` through `key`. Register the component instance directly with
`register(component)`; the same API is used for custom components and theme
renderers.

All themes register `StyledTopBar`, `StyledBottomNavigationBar`, `StyledNavigationRail`,
`StyledSnackbar`, `StyledFloatingActionButton`, `StyledSurface`, `StyledCheckbox`,
`StyledRadioButton`, `StyledSwitch`, `StyledInput` and `StyledProgressIndicator`. To replace one of
these implementations while keeping the other defaults, register the replacement
in `registerCustomComponents`; no additional `remember` is needed in the
registration lambda.

```kotlin
val scope = rememberCoroutineScope()
val snackbarState = remember { StyledSnackbarHostState() }
var selectedDestination by remember { mutableStateOf(0) }

StyledScaffold(
    topBar = {
        StyledTopBar(title = { StyledText("My app") })
    },
    bottomBar = {
        StyledBottomNavigationBar {
            StyledBottomNavigationBarItem(
                selected = selectedDestination == 0,
                onClick = { selectedDestination = 0 },
                label = { StyledText("Home") },
            ) {
                StyledIcon(icon = Icons.Default.Home.toIconComposable(), contentDescription = null)
            }
            StyledBottomNavigationBarItem(
                selected = selectedDestination == 1,
                onClick = { selectedDestination = 1 },
                label = { StyledText("Settings") },
            ) {
                StyledIcon(icon = Icons.Default.Settings.toIconComposable(), contentDescription = null)
            }
        }
    },
    snackbarHost = {
        StyledSnackbarHost(snackbarState)
    },
    floatingActionButton = {
        StyledFloatingActionButton(
            onClick = {
                scope.launch {
                    val result = snackbarState.showSnackbar(
                        message = "Saved",
                        actionLabel = "Undo",
                        withDismissAction = true,
                    )
                    if (result == StyledSnackbarResult.ActionPerformed) {
                        // Undo the operation.
                    }
                }
            },
        ) {
            StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = "Add")
        }
    },
) { innerPadding ->
    StyledSurface(Modifier.padding(innerPadding)) {
        StyledText("Content", Modifier.padding(StyledTheme.padding.content))
    }
}
```

### Customization

- Top bars support `Standard` and `CenterAligned` variants, navigation and action
  slots, colors and window insets. Cupertino centers titles by default.
- `StyledBottomNavigationBar` replaces the former toolbar-style `StyledBottomBar`.
  Use `RowScope.StyledBottomNavigationBarItem` for equally weighted destinations
  with `selected`, `onClick`, icon, optional label, `enabled` and `alwaysShowLabel`.
  The caller owns the selected destination and navigation. Items support selected
  and unselected content colors, indicator color and an interaction source.
  Supply an accessible icon description when omitting a label.
  Theme factories expose `bottomNavigationBar` instead of `bottomBar`; Scaffold's
  generic `bottomBar` slot keeps its name.
- Floating action buttons support `Small`, `Standard` and `Large`, plus shape,
  colors and an interaction source. Give icon-only actions a content description.
- Snackbars support action/dismiss slots and an action on a separate line.
  Cupertino and Fluent 2 expose `contentPadding` in their renderer `defaults(...)`
  factories (by default, `padding.large` horizontally and `padding.small` vertically).
  For example, configure the theme with
  `snackbar = CupertinoSnackbar.create(defaults = CupertinoSnackbar.defaults(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)))`.
  This controls the snackbar's inner padding, including host-created snackbars;
  host outer padding is unchanged. Material 3 retains its native snackbar padding.
  The standard dismiss action uses `StyledTertiaryIconButton`. Cupertino and
  Fluent 2 derive the minimum content-row height directly from the larger of
  `StyledTheme.sizes.minimumInteractiveSize` and `iconSize`, keeping single-line
  messages equally tall with or without that icon. Multiline text and larger
  custom slots can grow.
  `StyledSnackbar(data)` provides standard actions; `StyledSnackbarHost` also
  accepts a custom snackbar renderer. Pass localized dismiss and pane labels to
  the host when needed.
- Surfaces are non-interactive containers with shape, fill/content colors,
  optional border and shadow elevation. They do not insert content padding.
- `StyledCard` exposes only `Standard` (default) and `Alternative` variants.
  `StyledAlternativeCard` delegates to the alternative variant. Cards dispatch
  through `StyledCard.Key` and add
  `StyledCardDefaults.contentPadding` as `PaddingValues` by default (16 dp on
  all sides for Material 3 and Cupertino, 12 dp for Fluent 2),
  configured through each card renderer's `defaults(contentPadding = ...)`.
  Override `contentPadding`, for example with
  `PaddingValues(horizontal = 20.dp, vertical = 12.dp)`, per card or use `applyContentPadding = false` for
  caller-managed padding. Card padding is no longer part of `StyledPadding`.
  All support
  `shape` and optional `StyledCardDefaults.customize(colors, border, elevation)`.
  An optional `onClick` enables button semantics, keyboard activation and an
  interaction source; `enabled = false` disables the action.
  Pass the variant, adjustable defaults and optional customization directly to
  `StyledCard`; there is no separate style wrapper.
  Filled styles use `CustomColors.filledBackground` and
  `CustomColors.filledForeground`; outlined styles use `textForeground` and
  the optional border color. Elevation is part of customization because it is
  style-specific: Material 3 uses a supplied elevation for filled cards and
  otherwise retains its native default; outlined cards and Cupertino/Fluent 2
  renderers use their own elevation behavior.
  Material 3 maps Standard to `Card` and Alternative to `OutlinedCard`, selecting
  the clickable overload only when `onClick` is supplied. Cupertino and Fluent 2
  map Alternative to their outlined appearance and share Foundation rendering.
  All theme factories expose a `card` implementation parameter.
  Card APIs now live in the `components` package, rather than `common`.
- Switches use a 51 x 31 dp Cupertino track with a shadowed thumb, or a
  40 x 20 dp Fluent 2 track with an outlined off state and hover/pressed colors.
  Both animate state changes, support RTL and keyboard focus, and retain `tint`
  for the checked track and `onTint` for the checked thumb.

Explicit color parameters follow `StyledScaffold`; shape defaults follow the
implementation's `Defaults` pattern. No additional generic emphasis variants or
duplicate customization objects are required for these containers.

`StyledPadding.content` also uses `PaddingValues`, with 16 dp on all sides on
mobile and 12 dp on desktop by default. Pass it directly to
`Modifier.padding(StyledTheme.padding.content)`. For horizontal-only padding,
resolve start/end with `calculateStartPadding`/`calculateEndPadding` using the
current `LocalLayoutDirection`; resolve individual vertical edges with
`calculateTopPadding`/`calculateBottomPadding`. The `small`, `medium` and `large`
tokens remain `Dp`.

### Snackbar lifecycle

`showSnackbar` suspends until action or dismissal and queues concurrent calls.
Cancelling its coroutine removes that message. Without an action, duration
defaults to `Short`; with an action it defaults to `Indefinite`, so provide a
dismiss action or dismiss programmatically. Explicit `Short` or `Long` durations
are also supported. Finite durations honor accessibility timeout recommendations.
The host keeps outgoing text visible during the exit animation. Material 3
delegates timing, accessibility and transitions to its native `SnackbarHost`.
Cupertino and Fluent 2 share a Foundation host that fades and scales the snackbar
from its center, without animating its measured layout size. All theme definition
factories expose a `snackbarHost`
implementation parameter; scoped overrides use `StyledSnackbarHost.Key`.
The built-in hosts forward the custom snackbar slot; default content still uses
the registered `StyledSnackbar` renderer in every theme.

Keep the host state and coroutine scope above theme switches. Scaffold passes
its slots directly to the renderer, without `movableContentOf`, to avoid moving
them between Scaffold subcompositions when the renderer changes. Keep state that
must survive such changes above Scaffold as well; remembered state inside its
slots can reset when switching implementations. Only `StyledTheme` wraps its
content in remembered `movableContentOf` to preserve the caller subtree when
the theme root changes. All component slots are forwarded directly; their
remembered state can reset when a renderer replaces the slot subtree.
Internal implementation state is not shared.

### Theme behavior

Material3 uses Compose Material3 `NavigationBar` and `NavigationBarItem` for bottom
navigation, not `BottomAppBar`. Cupertino uses tint-based tab selection without
a filled indicator; Fluent2 uses a filled selected-icon indicator. Both support
disabled destinations and keyboard focus.

Material3 uses native Compose Material3 controls. Cupertino and Fluent2 reuse
internal Foundation renderers with their own sizing/alignment/shape defaults.
The shared layouts use styled padding, spacing and typography tokens. Floating
action buttons and snackbars are adaptive extensions for Cupertino, not native
UIKit controls.

Top and bottom bars handle their respective safe-area insets themselves; disable
them with `WindowInsets(0, 0, 0, 0)` when embedding a bar inside other content.
Always apply Scaffold's supplied `PaddingValues` to the body. The Foundation
Scaffold also incorporates the styled horizontal content padding.

### Navigation rail

Supply `navigationRail` to `StyledScaffold` to place navigation at the logical
start (left in LTR, right in RTL), between the top and bottom bars. The supplied
body padding includes the measured rail width. Continue applying that padding
to the body; the rail needs no additional system-bar padding inside Scaffold.
FABs and snackbars remain Scaffold-wide overlays.
Choose rail versus bottom navigation explicitly, for example using your own
window-size breakpoint. Scaffold does not switch automatically.

```kotlin
StyledScaffold(
    navigationRail = {
        StyledNavigationRail(header = { StyledText("My app") }) {
            StyledNavigationRailItem(
                selected = selectedDestination == 0,
                onClick = { selectedDestination = 0 },
                label = { StyledText("Home") },
            ) {
                StyledIcon(icon = Icons.Default.Home.toIconComposable(), contentDescription = null)
            }
        }
    },
) { innerPadding ->
    StyledText("Content", Modifier.padding(innerPadding))
}
```

Rail items offer the same selection, enabled, label, color and interaction-source
options as bottom navigation items. Material3 uses `NavigationRail` and
`NavigationRailItem`; Cupertino and Fluent2 share an internal Foundation renderer.
The Cupertino rail is an adaptive sidebar, not a native UIKit component.

### Shared navigation items

Define destinations once with `StyledNavigationItem<K>` and pass the same list
to either container. Each item contains a stable unique `key`, composable `label`
and `icon`, and `enabled` (default `true`). Selection and navigation remain
caller-controlled; a null `selectedKey` leaves all destinations unselected.
Items are composed by key, so reordering retains their slot state within the
same container. Switching between rail and bottom navigation does not transfer
item-local remembered state; hoist shared state above both containers.

```kotlin
val items = remember {
    listOf(
        StyledNavigationItem(
            key = "home",
            label = { StyledText("Home") },
            icon = { StyledIcon(icon = Icons.Default.Home.toIconComposable(), contentDescription = null) },
        ),
        StyledNavigationItem(
            key = "settings",
            label = { StyledText("Settings") },
            icon = { StyledIcon(icon = Icons.Default.Settings.toIconComposable(), contentDescription = null) },
        ),
    )
}
var selectedKey by remember { mutableStateOf("home") }

StyledScaffold(
    navigationRail = {
        if (useRail) {
            StyledNavigationRail(
                items = items,
                selectedKey = selectedKey,
                onItemClick = { selectedKey = it.key },
            )
        }
    },
    bottomBar = {
        if (!useRail) {
            StyledBottomNavigationBar(
                items = items,
                selectedKey = selectedKey,
                onItemClick = { selectedKey = it.key },
            )
        }
    },
) { innerPadding ->
    StyledText("Content", Modifier.padding(innerPadding))
}
```

Both list overloads support container colors and `alwaysShowLabel`; bottom
navigation also accepts window insets, and the rail accepts a header.
Colors, indicators and layout remain renderer concerns rather than item data.
The existing content-slot APIs remain available for per-item customization.

### Dividers

`StyledHorizontalDivider` and `StyledVerticalDivider` are generic Foundation
components in `com.michaelflisar.composestyledcomponents.common`; they work with
all themes without an additional registry entry. Both accept `modifier`,
`thickness` (default `1.dp`) and `color` (default
`StyledTheme.colorScheme.outlineVariant`). Use `Dp.Hairline` for a
one-physical-pixel divider. Thickness must be finite and non-negative.

```kotlin
StyledHorizontalDivider()
StyledHorizontalDivider(
    modifier = Modifier.padding(
        start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
        end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
    ),
    thickness = 2.dp,
    color = StyledTheme.colorScheme.primary,
)
Row(Modifier.height(IntrinsicSize.Min)) {
    StyledText("Left")
    StyledVerticalDivider()
    StyledText("Right")
}
```

Horizontal dividers fill the available width; vertical dividers fill the
available height. Constrain a vertical divider with `Modifier.height(...)`,
or use `IntrinsicSize.Min` on a wrapping row as above. Dividers add no spacing;
apply padding through the modifier or arrange spacing in the parent.

### Form controls

`StyledCheckbox` and `StyledSwitch` are controlled Boolean components with
`checked`, nullable `onCheckedChange`, `enabled`, `tint`, `onTint` and an optional
interaction source. Give interactive controls accessible labels through caller
semantics. With a null callback the control is passive, without its own toggle
action or semantics, so a containing `toggleable` row can own the interaction.
`StyledRadioButton` has controlled `selected` state, a nullable `onClick`, `enabled`,
`tint` and an optional interaction source. A null callback leaves interaction and
semantics to a parent, such as a selectable row.

```kotlin
var checked by remember { mutableStateOf(false) }
var selectedOption by remember { mutableStateOf("first") }
var text by remember { mutableStateOf("") }

StyledCheckbox(checked = checked, onCheckedChange = { checked = it })
StyledSwitch(checked = checked, onCheckedChange = { checked = it })
StyledRadioButton(
    selected = selectedOption == "first",
    onClick = { selectedOption = "first" },
)
StyledRadioButton(
    selected = selectedOption == "second",
    onClick = { selectedOption = "second" },
)
StyledInput(
    value = text,
    onValueChange = { text = it },
    label = { StyledText("Name") },
    placeholder = { StyledText("Enter your name") },
    supportingText = { StyledText("Shown on your profile") },
)
```

`StyledInput` exposes `Default` and `Alternative` variants; each renderer maps
them to its own field appearances. It also supports enabled/read-only modes,
single-line or multiline input (`singleLine = false`, `minLines`, `maxLines`),
label, placeholder, leading/trailing icons and supporting text. Shape defaults
follow the implementation; use `StyledInputDefaults.customize(CustomColors, border)`
to customize colors consistently across the transparent and filled styles.
Filled input uses `filledBackground`/`filledForeground`; the default outlined
input uses `textForeground` and the optional border color.
Keyboard options/actions, visual transformation and interaction source are
forwarded to the underlying text field. Leading and trailing slots provide an
input-owned context so Styled buttons and icon buttons in those slots use the
button pointer cursor rather than inheriting the text input cursor.

For passwords combine `PasswordVisualTransformation()` with
`KeyboardOptions(keyboardType = KeyboardType.Password)`. For errors set `isError`,
provide localized `errorMessage` for accessibility and render the message in
`supportingText` when desired. Error text is not inserted automatically.

Keep controlled values above theme switches and outside Scaffold slots. Input
decoration slots are forwarded directly; their remembered state, focus,
cursor/selection and implementation animations are not shared between renderers.
Material3 uses its native controls;
Cupertino and Fluent2 share Foundation controls with theme-specific defaults.

### Tabs

`StyledTabRow` and `StyledTab` provide themed tab navigation. Choose a primary
or secondary row, with either fixed or horizontally scrollable tabs. Selection
is controlled by the caller, and tab content can be any composable:

```kotlin
val selectedTab = remember { mutableStateOf(0) }
val tabs = listOf("Overview", "Details")

StyledPrimaryTabRow(selectedTabIndex = selectedTab.value) {
    tabs.forEachIndexed { index, title ->
        StyledTab(
            selected = selectedTab.value == index,
            onClick = { selectedTab.value = index },
        ) {
            StyledText(title)
        }
    }

    StyledSecondaryTabRow(selectedTabIndex = selectedTab.value) {
        tabs.forEachIndexed { index, title ->
            StyledTab(
                selected = selectedTab.value == index,
                onClick = { selectedTab.value = index },
            ) {
                StyledText(title)
            }
        }
    }

    // For tabs that exceed the available width:
    StyledPrimaryScrollableTabRow(selectedTabIndex = selectedTab.value) {
        tabs.forEachIndexed { index, title ->
            StyledTab(
                selected = selectedTab.value == index,
                onClick = { selectedTab.value = index },
            ) {
                StyledText(title)
            }
        }
    }
    // Use StyledSecondaryScrollableTabRow for the scrollable secondary variant.
}
```

`StyledPager` uses these components for its tab row, so theme overrides affect
both standalone tabs and pager navigation.

### Dropdowns

The dropdown compounds live in
`com.michaelflisar.composestyledcomponents.common`. Their anchors compose
`StyledButton` with the same `Primary`, `Secondary` and `Tertiary` roles,
alongside `StyledIcon`, `StyledText`, `StyledInput`, `StyledCheckbox` and styled
action buttons. They do not own a separate theme-specific renderer and work
with all registered themes, without Material 3 dependencies in core.

```kotlin
val fruits = listOf("Apple", "Banana", "Cherry")
val selected = remember { mutableStateOf(fruits.first()) }
val selectedIndices = remember { mutableStateOf(emptyList<Int>()) }
val filter = remember {
    StyledDropdown.Filter<String>("Filter fruit") { query, item ->
        item.contains(query, ignoreCase = true)
    }
}

StyledSecondaryDropdown(
    items = fruits,
    selected = selected,
    title = "Fruit",
    filter = filter,
    popupWidthConfig = StyledPopupMenu.WidthConfig.AtLeastAnchor,
    content = StyledDropdownDefaults.defaultRenderer(
        mapper = { it.uppercase() },
        mapperDropdown = { "Choose $it" },
    ),
)
StyledDropdownMultiSelectIndex(
    items = fruits,
    selectedIndices = selectedIndices,
    title = "Several fruits",
    filter = filter,
    sortSelectionByItems = false,
    actions = StyledDropdownMultiSelectDefaults.defaultActions(
        selectAllLabel = "Select all fruits",
        clearSelectionLabel = "Clear",
    ),
)
```

`StyledDropdown` and `StyledDropdownMultiSelect` accept selected values;
`StyledDropdownIndex` and `StyledDropdownMultiSelectIndex` accept original list
indices. Each has controlled value/callback and `MutableState` overloads. State
overloads update the state before invoking the optional callback. Single
selection closes the popup; multi-selection toggles items while keeping it open.
Callbacks always receive original indices, including when a filter hides items.
An absent single value or out-of-range single index displays no selection.
Multi-selection values must exist in `items`, and indices must be valid;
otherwise the component throws `IllegalArgumentException`. Repeated selected
indices are treated as one selection. Value-based selection uses the first
equal item; use index overloads to distinguish equal entries.

`StyledDropdown` and `StyledDropdownMultiSelect` use the primary button role.
Use the corresponding `StyledSecondary...` or `StyledTertiary...` composable
when another semantic button role is needed; role selection is not a public
parameter.

`sortSelectionByItems` defaults to `true`, ordering rendered selections and
callback results by original index. Set it to `false` to preserve selection
order. Filtering affects only visible entries: select-all and clear operate on
the complete list. The filter and scroll position reset on reopening. Disabling
an open dropdown dismisses it. The focusable popup dismisses through the platform
back/Escape and outside-click handling.

All dropdown role functions accept `shape` and button `customization`; titles
render inside the button anchor. Popup colors inherit the styled surface tokens.
`popupWidthConfig` supports `MatchAnchor` (default),
`AtLeastAnchor`, `WrapContent` and `Fixed(width)`. Width is constrained by the
window. `popupMaxHeight` defaults to `320.dp`; the filter and multi-selection
actions remain above the scrollable entries. Popups align to the logical start,
prefer opening below the anchor, and move above it when space permits.

Dropdown `role` accepts the `StyledButton.Role` values directly; its
`RenderContext.Selection` and `RenderContext.DropdownItem` are content data
contexts, not theme-mapping contexts. Renderer helpers retain separate
selection and item customization: text
mappers, selected/unselected colors and font weights, line limits, leading
content and the multi-selection formatter. To replace the complete rendering,
use the `content`/`dropdownContent` overload of `defaultRenderer`, or handle
`StyledDropdown.RenderContext.Selection` and `DropdownItem` yourself.
Multi-selection entries use passive styled checkboxes; the row owns the toggle
semantics. The multi-selection `actions` slot can replace the complete header;
its context provides `canSelectAll`, `canClear`, `selectAll` and
`clearSelection`. Supply localized action labels with `defaultActions`.

The former `MyDropdown`, `MyDropdownIndex`, `MyDropdownMultiSelect`,
`MyDropdownMultiSelectIndex` and their defaults/context types have been replaced
by the corresponding `Styled*` APIs. No `My*` compatibility aliases are retained.

# :sparkles: Demo

A full [demo](/demo) is included inside the demo module, it shows nearly every usage with working examples.

# :books: API

Check out the [API documentation](https://MFlisar.github.io/ComposeStyledComponents/).

# :bulb: Other Libraries

You can find more of my multiplatform libraries that work well together [here](https://mflisar.github.io/Libraries/).

# :twisted_rightwards_arrows: Compatibility

When combining my libraries, you can find compatibility information [here](https://mflisar.github.io/Libraries/compatibilities/).
