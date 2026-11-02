# Adaptive layouts

ComposeStyled's adaptive APIs make layout decisions from the space available to
the UI. They do not own destination selection, a back stack or navigation
behavior. All Adaptive APIs live in
`com.michaelflisar.composestyled.adaptive`.

There are two UI composables:

- `StyledAdaptiveScaffold` chooses bottom or side navigation and provides
  scaffold content.
- `StyledAdaptiveMasterDetail` arranges a master and optional detail pane.

`StyledAdaptive` is the utility for decisions. Its composable queries return
Compose `State` values; reading `.value` makes the current composition react to
size, platform and policy changes. Pure overloads accept an explicit
`StyledAdaptiveContext` for use outside a composable.

## Decisions and policies

```kotlin
val masterDetailMode = StyledAdaptive.masterDetail()
val showBothPanes = masterDetailMode.value == StyledAdaptiveMasterDetailMode.SideBySide
```

`StyledAdaptiveSize` contains width and height in dp, plus a width class:
`Compact` below 600 dp, `Medium` from 600 dp to below 840 dp, and `Expanded`
from 840 dp. Width classes describe space; the policy separately decides the
presentation. An unbounded width is treated conservatively as compact.

The default policy keeps these thresholds:

| Decision | Default |
|---|---|
| Navigation | Below 600 dp: bottom; below 840 dp: compact side; below 1200 dp on desktop/web: expandable side; otherwise expanded side |
| Master/detail | Side-by-side at 720 dp on desktop/web or 840 dp on mobile |
| Dialog | Fullscreen below 600 dp window width or 480 dp window height; otherwise toolbar dialog |

Navigation and master/detail use the measured local layout size. Dialog style
uses the host window size. The utility observes the host window by default; to
base a decision on a specific layout, pass `layoutSize: State<StyledAdaptiveSize>`.
Supply a `StyledAdaptivePolicy` to replace any of the default decisions. For
pure calculations, construct a `StyledAdaptiveContext` with separate local
layout and host-window sizes and call `StyledAdaptive.resolve(context, policy)`.

The `State` returned by `StyledAdaptive.navigation`, `masterDetail` and
`dialog` is read-only. Its value updates as the observed source changes. The
corresponding value types are `StyledNavigationMode`,
`StyledAdaptiveMasterDetailMode` and `StyledDialog.Style`.
`StyledAdaptive.showScrollbars(platform)` reports the platform preference; the
theme's `StyledTheme.behavior.showScrollbars` can independently override it.

## Master/detail

The caller supplies the related content. Adaptive never infers which entries
belong together from a back stack.

```kotlin
var selectedItem by remember { mutableStateOf<String?>(null) }

StyledAdaptiveMasterDetail(
    master = {
        StyledButton(onClick = { selectedItem = "item" }) {
            StyledText("Open item")
        }
    },
    detail = if (selectedItem != null) {
        { StyledText("Details for $selectedItem") }
    } else null,
    emptyDetail = {
        StyledText("Select an item")
    },
)
```

`detail = null` means no detail is present. `SinglePane` shows the detail when
one exists, otherwise the master. `SideBySide` always reserves the master pane;
it shows the detail when present and otherwise leaves an empty detail area or
composes `emptyDetail`. A non-null detail slot counts as present even when it
chooses to render no visible content.

`masterFraction` (default `0.4f`) sets the master's share of the available
width, and `paneSpacing` sets the gap. The layout mirrors naturally in RTL.
Switching modes only changes which supplied content is composed; selection,
back behavior and saveable state remain the caller's responsibility.

Pass `masterDetailMode` to use an already-resolved
`State<StyledAdaptiveMasterDetailMode>`, for example when a parent or app-owned
Navigation 3 scene strategy needs rendering and scene selection to use the same
decision. Otherwise the layout measures its own available constraints. The
explicit mode is authoritative; its other slot information still reflects the
layout's locally measured size. Inside Scaffold content, derive it from the
provided info state with `StyledAdaptive.masterDetail(adaptiveInfo)`.

## Scaffold

Both `StyledAdaptiveScaffold` overloads preserve the existing navigation-item
and custom-navigation forms. Their content receives both scaffold padding and
an observable `State<StyledAdaptiveInfo>` for the measured body:

```kotlin
StyledAdaptiveScaffold(
    platform = StyledTheme.platform,
    navigation = { mode ->
        // Render navigation appropriate for mode.
    },
) { paddingValues, adaptiveInfo ->
    val masterDetailMode = StyledAdaptive.masterDetail(adaptiveInfo)
    StyledText(
        "Width class: ${adaptiveInfo.value.context.layoutSize.widthClass}",
        Modifier.padding(paddingValues).consumeWindowInsets(paddingValues),
    )
    // Pass masterDetailMode to StyledAdaptiveMasterDetail when composing its panes.
}
```

The scaffold's `state` is optional and hoistable with
`remember { StyledAdaptiveScaffoldState() }`. Its `info` and `navigationMode`
are read-only observations, initially null until the scaffold's first committed
layout. The content's `adaptiveInfo` describes the measured body constraints;
derive adaptive result states from it with `StyledAdaptive.navigation`,
`StyledAdaptive.masterDetail` or `StyledAdaptive.dialog`;
the externally observed scaffold state describes the overall scaffold
constraints used for navigation selection. Apply and consume the supplied body
padding at the content root; the Adaptive layout does not apply it a second
time.

Both overloads hoist expandable side-navigation state through
`sideNavigationExpanded`. By default, they collapse navigation when the
committed mode changes into `Side(Expandable)` from another mode. With the
custom `navigation` overload, pass the same state to the navigation UI that you
pass to the scaffold. Set `collapseSideNavigationOnModeChange = false` to
preserve the expanded state; other mode changes do not reset it.

Navigation selection remains caller-owned. Use `rememberSaveable` and a saver
when the selected key must survive recreation; Adaptive does not persist
layout-derived decisions.

## Navigation 3 boundary

Navigation 3 can use `StyledAdaptive.masterDetail()` to choose a scene
presentation, then pass the same state as `masterDetailMode` to
`StyledAdaptiveMasterDetail`. The
application's scene strategy decides which related entries form the master and
detail and which entries a single-pane scene exposes. The application remains
responsible for `Scene.entries`, previous entries, scene keys, entry decorators,
back and predictive-back behavior, lifecycles and transitions. Do not render
back-stack entries outside their Navigation 3 entry/decorator context.

The core module has no Navigation 3 dependency and supplies no scene strategy
or adapter. This keeps adaptive layout reusable for applications using other
navigation approaches.

See [Scaffold](Scaffold.md), [Side navigation](SideNavigation.md) and
[Dialog](Dialog.md) for their component-specific behavior.
