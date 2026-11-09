# Scaffold

`StyledScaffold` arranges themed bars, navigation, snackbars and floating actions
around the app content. Its body receives padding that should be applied to the
content. Import `StyledScaffold` and `StyledScaffoldDefaults` from
`com.michaelflisar.composestyled.ui.layout`. This structural classification is
independent of its registered theme renderer; bars and controls remain in
`ui.components`.

```kotlin
val scope = rememberCoroutineScope()
val snackbarState = remember { StyledSnackbarHostState() }
var selectedDestination by remember { mutableStateOf(0) }

val scaffoldColor = StyledTheme.colorScheme.surface
val topBarColor = StyledTheme.colorScheme.primarySurface
val bottomBarColor = StyledTheme.colorScheme.surfaceVariant

SystemBarStyleEffectIfNeeded(
    statusBarColor = topBarColor,
    navigationBarColor = bottomBarColor,
    backgroundColor = scaffoldColor,
)

StyledScaffold(
    containerColor = scaffoldColor,
    topBar = {
        StyledTopBar(
            containerColor = topBarColor,
            title = { StyledText("My app") },
        )
    },
    bottomBar = {
        StyledBottomNavigationBar(containerColor = bottomBarColor) {
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
    StyledSurface(Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)) {
        StyledText("Content", Modifier.padding(StyledTheme.padding.content))
    }
}
```

Explicit color parameters follow `StyledScaffold`; shape defaults follow the
implementation's `Defaults` pattern. For content that stays clear of the bars,
apply the supplied body
`PaddingValues` and consume them on the same content root with
`Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)`, so nested
inset-aware layouts do not add them again. For a lazy list, use
`contentPadding = innerPadding` and `Modifier.consumeWindowInsets(innerPadding)`.
The Foundation Scaffold also incorporates the styled horizontal
content padding. No additional generic emphasis variants or duplicate
customization objects are required for these containers.

Use `sideNavigation` for logical-start navigation between the top and bottom
bars. This replaces the former rail slot. Its measured width is included in
body padding on every animation frame, so expanded side navigation reserves
the correct content space. FABs and snackbars retain Scaffold-wide placement.
See [Side navigation](SideNavigation.md) for modes, fixed slots and scrolling.

## Scrolling behind semi-transparent bars

A semi-transparent `containerColor` alone does not move content behind a bar.
For each overlay bar, move its supplied top or bottom Scaffold padding from
`Modifier.padding(...)` into the scroll container's `contentPadding`. Keep
horizontal padding (including side navigation) and padding for opaque bars on
the viewport. Continue consuming the complete Scaffold padding on the content
root so nested layouts do not apply the same insets again.

`StyledVerticalScrollContainer` applies `contentPadding` inside its scrolling
content. This lets items scroll behind the overlay while keeping the first and
last items fully reachable outside the bars at the scroll limits. Lazy lists
can use the same split with their own `contentPadding`.

The demo's **Starter template > Styling > Options** provides independent
semi-transparent toolbar and bottom-navigation switches, using the active bar
color at 65% opacity. Numbered, alternating-color content on the settings and
styling pages makes the overlap visible when scrolling. Both default and
adaptive Scaffolds support the switches; adaptive side navigation remains
opaque and keeps its reserved width. Bar content colors stay based on the
opaque theme color. System-bar icon styling uses the displayed bar color
composited over the Scaffold background as its reference; it does not sample
the scrolling content, so contrast over changing content should be checked on
the target device.

## Safe areas and inset ownership

`StyledTopBar` and `StyledCenterAlignedTopBar` handle top and horizontal
`WindowInsets.safeDrawing` by default. `StyledBottomNavigationBar` handles bottom
and horizontal safe-area insets. Their backgrounds extend behind those insets,
while their controls are padded and the insets are consumed for their descendants.
Custom bars must provide their own inset handling.

Scaffold uses the measured bars, including their inset padding, to reserve space.
Without bars, `contentWindowInsets` supplies safe-area padding instead.
Cupertino and Fluent2 default to `WindowInsets.safeDrawing`; Material3 retains
its native `ScaffoldDefaults.contentWindowInsets`. Insets already consumed by a
parent are excluded. This applies to Android system bars/cutouts and iOS safe
areas as provided by the host; it does not enable edge-to-edge or configure the
native host.

The side-navigation slot is positioned inside the reserved area and consumes the
offsets already covered by that placement, only for its own descendants.
`StyledSideNavigation` therefore does not add the same safe-area padding twice.
No padding argument is needed for the bar or side-navigation slots; content
continues receiving `PaddingValues` to apply explicitly.

Insets can be overridden independently: `contentWindowInsets = WindowInsets(0, 0, 0, 0)`
disables Scaffold's own safe-area contribution, not the bars' own `windowInsets`.
For an embedded preview with no window-inset handling, also pass zero
`windowInsets` to its bars and side navigation.

There is no Kotlin annotation that requires using a lambda's padding parameter.
Material3's unused-padding diagnostic is a dedicated Android-Lint rule for its
own Scaffold, not a check automatically inherited by Styled components.

## Hoisting adaptive navigation information

Both `StyledAdaptiveScaffold` overloads accept a hoisted state. Create it with
`remember { StyledAdaptiveScaffoldState() }`. Import the scaffold and state from
`com.michaelflisar.composestyled.adaptive`.

```kotlin
val adaptiveState = remember { StyledAdaptiveScaffoldState() }
val navigationMode = adaptiveState.navigationMode

if (navigationMode != null) {
    StyledText("Current navigation: $navigationMode")
}

StyledAdaptiveScaffold(
    platform = StyledTheme.platform,
    state = adaptiveState,
    navigation = { mode ->
        StyledText("Navigation: $mode")
    },
) { paddingValues, adaptiveInfo ->
    StyledText(
        "Width class: ${adaptiveInfo.value.context.layoutSize.widthClass}",
        Modifier.padding(paddingValues).consumeWindowInsets(paddingValues),
    )
}
```

`navigationMode` is observable and read-only to callers. It is `null` until
the first successful layout composition, then reports the last committed mode.
It updates after composition when the scaffold's available constraints,
platform or `navigationMode` policy produce a different mode. It uses the
scaffold's local constraints, not the host window size. The body also receives
`State<StyledAdaptiveInfo>`, calculated from the measured body constraints
after Scaffold reservations. Read `.value` to use its current measurements and
decisions. The navigation slot continues receiving the current mode directly,
without waiting for the state update.

Use one state per scaffold. It retains its last reported value while remembered,
even if that scaffold leaves composition; layout-derived values are not saved
across recreation. Omit `state` when external observation is not needed.
Adaptive APIs are in `com.michaelflisar.composestyled.adaptive`; navigation
selection remains caller-owned and can use normal `rememberSaveable` with a
custom saver where needed.
The state observes the result; use the existing `navigationMode` policy parameter
to control mode selection. For example, `DemoTemplate` reads this state to style
system bars outside the navigation slot.

## Caller-directed system-bar styling

`StyledScaffold`, `StyledTopBar`, and `StyledBottomNavigationBar` do not
automatically change system bars or report colors. Call
`SystemBarStyleEffectIfNeeded` where the caller owns the window and pass the
colors rendered behind each bar. It applies contrast-aware icon styles only in
normal window mode. Its `backgroundColor` defaults to the theme surface; pass
the Scaffold's actual container color when it differs. For a transparent or
semi-transparent bar, `systemBarIconStyleFor(color, background)` composites the
bar color over that underlay. If no toolbar is present, use the Scaffold color
for `statusBarColor`. When the bar uses an explicit content color, pass it as
`statusBarContentColor` or `navigationBarContentColor` so system-bar icons follow
the same foreground; for a translucent theme role, derive it from the opaque
role color with `StyledTheme.contentColorFor(roleColor)`.

Use the lower-level `SystemBarStyleEffect` when you need explicit
`SystemBarIconStyle` values or different window-mode behavior.

On Android 10+, a styled bar also disables Android's contrast scrim
(`isStatusBarContrastEnforced` / `isNavigationBarContrastEnforced`), so the bar
color drawn behind three-button navigation stays visible. The previous values
are restored when the effect leaves composition.

`SystemBarStyleEffect` does not inspect window mode; use
`rememberWindowEnvironment()` to decide whether to call it. Android reports
split-screen and floating/freeform as `MultiWindow`; iOS reports reduced
scene/window bounds where it can identify them. The effect restores the prior
icon appearance when it leaves composition. Avoid overlapping effects that
control the same bar in one native window.

On Android, the effect changes only icon appearance. It does not change bar
backgrounds, insets, edge-to-edge behavior, or the OS contrast scrim.

On iOS, only status-bar appearance is supported; the navigation-bar style is
ignored and the home-indicator style remains UIKit-controlled. Wrap the app's
Compose controller with `withStyledSystemBarStyleHost()` and install the
returned controller in the app's view-controller hierarchy:

```kotlin
ComposeUIViewController { App() }
    .withStyledSystemBarStyleHost()
```
