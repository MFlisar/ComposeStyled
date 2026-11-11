# Dialogs and bottom sheets

`StyledDialog` is the single API for modal content. Its concrete `style` selects
the presentation and carries the configuration relevant to that presentation.
`StyledDialog.Style.FloatingWindow` is the non-modal exception: it is rendered
inside the active `StyledTheme` host, and the surrounding content remains
interactive.
`StyledBottomSheet` is a separate, non-modal overlay. Both live in
`com.michaelflisar.composestyled.ui.layout` and share the same state, buttons,
dismissal policy and `ColumnScope` body slot.

## Basic usage

```kotlin
val state = rememberDialogState()

StyledButton(onClick = state::show) {
    StyledText("Open")
}
StyledDialog(
    state = state,
    title = { StyledText("Save changes?") },
    buttons = StyledDialog.Buttons(
        positive = StyledDialog.Button(onClick = { save() }) {
            StyledText("Save")
        },
        negative = StyledDialog.Button(onClick = {}) {
            StyledText("Cancel")
        },
        neutral = StyledDialog.Button(
            onClick = { applyChanges() },
            dismissOnClick = false,
        ) {
            StyledText("Apply")
        },
    ),
) { contentPadding ->
    StyledText(
        "Your changes have not been saved.",
        modifier = Modifier.padding(contentPadding),
    )
}
```

Omitting `style` uses `StyledDialogDefaults.style`, initially
`StyledDialog.Style.Dialog()`. There are no separate dialog, fullscreen or modal
sheet composables and no style-resolver overload.

## Choosing a style

| Style | Intended use | Layout |
|-------|--------------|--------|
| `StyledDialog.Style.Dialog()` | Confirmations, messages and short forms | Content-height alert with title and actions below the body |
| `StyledDialog.Style.ToolbarDialog()` | Larger forms, pickers and previews | Bounded workspace with toolbar and remaining-height body |
| `StyledDialog.Style.Fullscreen()` | Extensive workflows or compact screens | Full-window toolbar workspace |
| `StyledDialog.Style.BottomSheet()` | Contextual choices and actions | Draggable sheet with optional viewport-fixed action footer |
| `StyledDialog.Style.FloatingWindow()` | Persistent tools or movable workspaces | Non-modal, draggable window with a bottom action footer |

`ToolbarDialog` is not a separate movable native desktop window. Unlike an
alert, its body fills the remaining bounded workspace. `Fullscreen` uses the
same workspace structure without outer margins. An explicitly chosen style
does not automatically switch to another presentation.

```kotlin
StyledDialog(
    state = state,
    style = StyledDialog.Style.ToolbarDialog(
        maxWidth = 840.dp,
        maxHeightFraction = 0.85f,
        edgeMargin = 24.dp,
        expandToFullscreen = true,
    ),
    title = { StyledText("Edit document") },
    buttons = buttons,
) { contentPadding ->
    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
    ) {
        // Editor content; toolbar and footer remain outside scrolling.
    }
}
```

The body slot receives style-specific `PaddingValues`. Apply them exactly once
inside the body; renderers do not also apply those body insets. For a scrolling
`Column`, put `.padding(contentPadding)` after `verticalScroll` so the padding
scrolls with the content. For a `LazyColumn`, pass the values to its
`contentPadding`. Title, toolbar and action-footer spacing remain renderer-owned.

Fullscreen dialogs use a fade transition by default. Choose `Fade` or
`Slide(direction = ...)` with `StyledDialog.Style.Fullscreen(animation = ...)`.
The slide direction identifies the entering edge (`FromLeft`, `FromRight`,
`FromTop` or `FromBottom`); dismissal slides back toward that same edge. The
default is `FromRight`.
All modal styles, including fullscreen, use the platform's Compose `Dialog`.
Fullscreen requests a transparent scrim while remaining modal; other modal
styles use the platform's default scrim. Fullscreen's fade or slide animation
applies to its content. System-bar icon styling follows the transition: for a
fade or horizontal slide, it changes after the dialog is fully visible and
restores when dismissal starts. For vertical slides, the bar at the entering
edge changes as the dialog arrives, while the opposite bar changes when the
transition completes; dismissal reverses that timing. Nested modal styles use
the same dialog path.

Fullscreen dialogs can opt into a scroll-driven expandable header:

```kotlin
StyledDialog.Style.Fullscreen(
    expandableHeader = StyledDialog.Style.Fullscreen.ExpandableHeader(
        expandedHeight = 240.dp,
        containerColor = StyledTheme.colorScheme.surfaceVariant,
    ),
)
```

`expandableHeader` is `null` by default, preserving the regular toolbar.
`expandedHeight` is optional and sets the total expanded height, including the
regular top bar and window insets; when omitted, it uses the expandable top bar's
natural title-and-padding size. The dialog connects the header to nested scroll
from its body. The body must contain scrollable content to collapse or expand
the header. `containerColor` and `contentColor` configure the collapsed header;
by default, its background matches the dialog container and the content color is
derived from it. Fullscreen dialog content uses the theme's `surface` role by
default; the Material 3 renderer retains `surfaceContainerHigh` for other
dialog styles. `expandedContainerColor` and `expandedContentColor` optionally
override those colors while expanded; otherwise, the collapsed colors are used.
When an expanded background is provided without an expanded content color, the
content color is derived from that background. If `title` is `null`, the regular
toolbar is retained.

Bottom sheets slide up when shown. Button, back and outside dismissals slide
down; a swipe-to-dismiss uses the drag motion itself and does not replay a
second exit animation.

Toolbar dialogs can opt into `expandToFullscreen`. The toolbar then offers an
Expand action; double-clicking its title toggles between the bounded and
fullscreen workspaces. In fullscreen, no extra Restore button is shown; the
title double-click and its accessibility custom action restore the bounded
layout. Expansion animates the workspace bounds, switches to edge-to-edge
layout, and updates system-bar icon contrast. The option defaults to `false`.
If no title is provided, a Restore action is shown because there is no title
area to use for the double-click gesture.

### Floating windows

`FloatingWindow` passes outside clicks through to the host content by default.
Set `backgroundMode` to `StyledDialog.FloatingWindowBackgroundMode.Block`,
`StyledDialog.FloatingWindowBackgroundMode.Minimize` or
`StyledDialog.FloatingWindowBackgroundMode.Dismiss` to intercept clicks outside
the expanded window. These modes each accept `showScrim` to optionally dim the
host. `StyledDialog.FloatingWindowBackgroundMode.Interactive` has no scrim
option and always passes outside clicks through. A scrim is shown only while
the window is expanded and is removed when minimized. `Minimize` parks the
window as a bubble without dismissing the dialog. The window is rendered in the
automatically provided `StyledTheme` host, or in the nearest explicit
`StyledFloatingHost` when you need to limit floating windows to a specific
bounded area.

Dragging a minimized bubble snaps it to the nearest horizontal edge on release
by default. Set `snapToEdge = false` to keep its dropped position.

```kotlin
StyledDialog(
    state = state,
    style = StyledDialog.Style.FloatingWindow(
        initialSize = DpSize(640.dp, 420.dp),
        minSize = DpSize(320.dp, 240.dp),
        shape = RoundedCornerShape(16.dp),
        initialPosition = StyledDialog.FloatingPosition(
            alignment = Alignment.TopStart,
            offset = DpOffset(24.dp, 24.dp),
        ),
        initialBubblePosition = StyledDialog.FloatingPosition(
            alignment = Alignment.BottomStart,
            offset = DpOffset(24.dp, (-24).dp),
        ),
        initiallyMinimized = true,
        backgroundMode = StyledDialog.FloatingWindowBackgroundMode.Minimize(
            showScrim = true,
        ),
        draggable = true,
        resizable = true,
        showMinimizeButton = true,
        showMaximizeButton = true,
        showCloseButton = true,
        icon = Icons.Default.Edit.toIconComposable(),
        bubble = StyledDialog.BubbleConfig(
            containerColor = StyledTheme.colorScheme.primary,
            contentColor = StyledTheme.colorScheme.onPrimary,
            borderColor = StyledTheme.colorScheme.outline,
            shape = RoundedCornerShape(50),
            shadowElevation = 20.dp,
        ),
    ),
    title = { StyledText("Inspector") },
    buttons = buttons,
) { contentPadding ->
    // The action footer remains at the bottom of the window.
    Column(Modifier.fillMaxSize().padding(contentPadding)) {
        // Floating-window body
    }
}
```

`initialSize` and `minSize` default to 720×480 dp and 320×240 dp, respectively;
both must be finite and positive, and the initial size cannot be smaller than
the minimum. When the host is smaller, the window is constrained to its
available area. The shape defaults to the active theme's dialog shape, and
`edgeMargin` defaults to its dialog margin. Resizing is available at each edge
and corner. In the expanded window, the title and body share the same horizontal
content inset. Dragging is limited to the title area; double-clicking that area
maximizes/restores when `showMaximizeButton` is enabled. Minimizing animates the
window into a draggable 48×48 dp bubble. Its bounds always remain square,
independent of its shape. The bubble shows the optional `icon`, or the title
within those same bounds when no icon is provided. On JVM desktop, Android and web, the
window edges/corners show matching resize cursors; the title area keeps the
platform's default cursor. The resize cursor stays active for the whole drag,
including outside the window's current bounds. iOS and native macOS currently
use the platform's default cursor.

`initialPosition` places the window relative to the available host area and
defaults to `Alignment.Center`. `initialBubblePosition` uses the same
`FloatingPosition(alignment, offset)` configuration; `Alignment.BottomStart`
with a negative y offset, for example, places the bubble above the bottom edge.
Offsets are applied after alignment, then both positions are constrained to the
host area. `Start` and `End` alignments follow the current layout direction. If
`initialBubblePosition` is omitted, the bubble is centered on the window the
first time it is minimized (also when `initiallyMinimized` is true). Set
`initiallyMinimized = true` to start with the bubble shown instead of the full
window.

Window and bubble positions are remembered independently: clicking the bubble
restores the window at its last window position, and minimizing again returns
to the last bubble position, even after moving the expanded window. Dragging
the bubble sets its new parking position. Each position is retained as a
proportion of its movable range, so changing host bounds (for example, on
rotation) adjusts the visible placement without replacing the saved relative
position. The window size continues to be constrained independently to the
current host bounds.
On first minimize, the bubble is centered on the window. Both positions remain
constrained to the host's available area. Header actions are enabled by default for minimize
and close, and disabled by default for maximize. Closing calls
`onDismissRequest`; the window is removed when its state is no longer visible.

`bubble` optionally overrides the minimized appearance. By default it keeps the
window shape and content color, uses `surfaceVariant` as its background and has
no border. Setting `containerColor` also derives its matching content color via
`StyledTheme.contentColorFor`, unless `contentColor` is explicitly supplied.
`borderColor` enables a 1 dp outline only on the bubble. These overrides do not
change the expanded window's appearance or its icon/title selection.
`shadowElevation` optionally overrides the bubble's shadow: null retains the
window's 12 dp elevation, `0.dp` removes the shadow, and other values must be
finite and non-negative. The expanded window keeps its 12 dp elevation.
Background, content and border colors, and shadow elevation animate over 220 ms when minimizing or
restoring, alongside size and position. When both shapes are
`RoundedCornerShape` (including percentage corners), their corners animate too;
other custom shapes switch directly. The shared floating renderer applies this
behavior in Material 3, Cupertino and Fluent 2.

### Adaptive selection

```kotlin
StyledDialog(
    state = state,
    style = StyledAdaptive.dialog(StyledTheme.platform).value,
    title = { StyledText("Edit document") },
    buttons = buttons,
) { contentPadding ->
    // Body
    Column(Modifier.padding(contentPadding)) {
        // Workspace content
    }
}
```

`StyledAdaptive.dialog(platform)` returns an observable
`State<StyledDialog.Style>` and observes the host window size itself, converting
it to dp. Read `.value` for the current style. It does not use the local
constraints of the composable that opens the modal. It selects `Fullscreen`
below 600 dp width **or** 480 dp height, otherwise `ToolbarDialog()`, on every
platform. This is intentionally a workspace policy, not an automatic conversion
of mobile dialogs into sheets: alerts and sheets serve different purposes and
are commonly used alongside each other on mobile.

Resizing between these two styles preserves remembered workspace body state.
The host-window selection is independent of keyboard insets; the rendered modal
still applies safe-area and IME padding to keep content above the keyboard.
Remembered body continuity is not guaranteed when switching to an unrelated
alert or sheet layout.

### Registry defaults

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = rememberMaterial3ThemeProvider(),
    customization = {
        update(StyledDialog.DefaultsKey) {
            copy(
                style = StyledDialog.Style.ToolbarDialog(),
                shape = RoundedCornerShape(12.dp),
                toolbarMaxWidth = 1000.dp,
            )
        }
    },
) {
    StyledDialog(state = state) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            // Uses the configured default style.
        }
    }
}
```

Use `UpdateStyledRegistry` for a scoped default. An explicit `style` overrides
the registry's default style. Nullable fields on the typed style inherit the
active registry's dimensions/shape; non-null fields override them.

| Default | Material 3 | Cupertino | Fluent 2 |
|---------|------------|-----------|----------|
| Alert `maxWidth` | 560 dp | 320 dp | 560 dp |
| `sheetMaxWidth` | 640 dp | 640 dp | 640 dp |
| `toolbarMaxWidth` | 960 dp | 960 dp | 960 dp |
| `maxHeightFraction` | 0.9 | 0.9 | 0.9 |
| Floating initial size | 720×480 dp | 720×480 dp | 720×480 dp |
| Floating minimum size | 320×240 dp | 320×240 dp | 320×240 dp |
| Initial `style` | `Dialog()` | `Dialog()` | `Dialog()` |

These are library defaults, not universal design-system mandates. Default
`edgeMargin` is 16 dp on Android/iOS and 24 dp on desktop/web. Alerts and toolbar
dialogs are centered within the actual modal window, capped by both available
space after margins and the height fraction. `Fullscreen` always uses a
rectangular surface and the entire inset-adjusted window.

Widths must be positive (`Dp.Infinity` removes the cap), height fractions in
`(0, 1]`, and margins finite and non-negative. Avoid `requiredWidth` or
`requiredHeight` modifiers that deliberately bypass measured limits.
Scrolling is caller-owned: use a scrollable Column or LazyColumn **inside**
the body, rather than scrolling the entire modal.

### Content and action spacing

Alerts wrap their content. Their renderer owns the outer inset, title-to-body
spacing and body-to-action spacing separately; omitting all actions removes the
action gap, not the normal outer inset.

| Alert metric | Material 3 | Cupertino | Fluent 2 |
|--------------|------------|-----------|----------|
| Outer inset | 24 dp | `spacing.large` | 24 dp |
| Title to body | 16 dp | `spacing.medium` | 8 dp |
| Body to action group | 24 dp | `spacing.medium` | 8 dp |
| Between actions / wrapped rows | 8 / 12 dp | `spacing.small` / `spacing.small` | 8 / 8 dp |

Material values follow the bundled native Material 3 alert layout. Fluent's
24/8 rhythm adapts the Fluent Web dialog reference to Compose dp. Cupertino
retains this library's simplified vertical alert layout; it does not claim
pixel-identical UIKit rendering. With the standard platform tokens, `large`
is 12 dp on desktop/web and 16 dp on mobile, `medium` is 6/8 dp, and `small`
is 2/4 dp. These are layout gaps, not distances between visible text glyphs:
buttons retain their own content padding and minimum interactive footprint.

Toolbar dialogs, fullscreen dialogs, floating windows and sheets configured with
`expandToFullscreen` keep their intended workspace/viewport height. Regular
bottom sheets wrap their measured title, body and actions up to the configured
height limit and remeasure when body content changes. Scrollable content uses
the available height up to that limit. The renderer passes body-only
`PaddingValues`; the caller applies them once. Fixed sheet footers include
their measured height in the body's bottom padding, so scrollable content
clears the footer without an extra gap above it. Without a footer, the body
receives its ordinary bottom inset.

Short content in a height-filling workspace can therefore leave substantial
free space above the footer. This is intentional workspace space, not an
additional content-to-button padding. Regular bottom sheets instead fit short
content; use `Style.Dialog()` for a content-height confirmation when an alert
presentation is intended.

## State and ViewModels

State factories remain in `com.michaelflisar.composestyled.ui.state`.
`rememberDialogState()` creates a `VisibilityDialogState` with `visible`,
`show()` and `dismiss()`. Pass `initialVisible = true` to start visible. Its
visibility is saved across supported host recreation, so a visible dialog or
sheet remains visible after a configuration change.

The data-backed overload creates `DataDialogState<T>`. Non-null data means
visible; `dismiss()` clears it. `show(data)` replaces the data and opens it.
`requireData()` returns non-null data or throws if hidden. Read it inside a
modal slot, composed only while visible:

```kotlin
val state = rememberDialogState<String>(null)

StyledButton(onClick = { state.show("Document") }) {
    StyledText("Edit")
}
StyledDialog(
    state = state,
    style = StyledDialog.Style.Fullscreen(),
    title = { StyledText(state.requireData()) },
) { contentPadding ->
    StyledText(
        "Editing ${state.requireData()}",
        modifier = Modifier.padding(contentPadding),
    )
}
```

Arbitrary `T` values are not automatically saveable. To restore a data-backed
dialog, use the overload that accepts a `dataSaver`:

```kotlin
val state = rememberDialogState(
    initialData = null as String?,
    dataSaver = Saver(save = { it }, restore = { it }),
)
```

The saver stores the currently shown data; dismissal-animation bookkeeping and
callbacks are recreated rather than persisted.

The Foundation bottom sheet also restores its last settled peek/expanded choice.
Floating-window size, position, minimized/maximized mode and bubble position
are restored and then clamped to the current host bounds. Live drag gestures
and in-progress animations are not restored.

Initial values are used only when state is first created. Outside composition,
including ViewModels, use `dialogStateOf()` or `dialogStateOf<T>(initialData)`.
These observable states hold no Activity/window/composition reference.
During an animated sheet or fullscreen exit, `data` becomes null immediately
while `requireData()` retains the dismissed value until the transition ends.
The non-composable factories do not save state; use an appropriate
`SavedStateHandle` when state is owned by a ViewModel.

## Buttons and dismissal

`positive`, `negative` and `neutral` use `StyledButton` or `StyledIconButton`.
Their variant defaults to `StyledButton.Variant.Auto`, allowing each theme to
choose its contextual action style. Set an explicit variant to keep a visual
treatment fixed across action slots. Presentation (`Auto`, `Text` or `Icon`) is
independent from the button variant. All actions are optional and support
disabled state.

```kotlin
StyledDialog.Button(
    onClick = { save() },
    label = "Save",
    icon = Icons.Default.Check.toIconComposable(),
    presentation = StyledDialog.ButtonPresentation.Auto,
)
```

The structured constructor requires a nonblank `label`. Its `icon` is optional;
`presentation` defaults to `Auto`:

| Presentation | Behavior |
|--------------|----------|
| `Auto` | Uses the theme's policy for the actual action position; missing icons always fall back to text |
| `Text` | Always displays the label, ignoring the icon |
| `Icon` | Always displays an icon button, including in alerts and sheet footers; throws `IllegalArgumentException` at construction if no icon is supplied |

Automatic icons are limited to toolbar positions in `ToolbarDialog` and
`Fullscreen`. Material 3 prefers an icon for the negative navigation action and
text for the positive action; Cupertino keeps both labeled. Fluent 2 places
its actions in the footer and keeps `Auto` as text. Neutral footer actions,
regular dialogs and both kinds of bottom sheets also keep `Auto` as text.
No icon assets are inferred; the caller must supply an icon for icon presentation.

Icon-only actions use the label as their accessibility description and tooltip.
Both text and icon controls receive the modal action context, so registered
button/icon-button replacements affect modal actions too.

For theme-wide toolbar presentation preferences, replace the modal renderer
through `StyledTheme(customization)`:

```kotlin
register(Material3Dialog.create(presentationMapping = { slot ->
    if (slot == StyledDialog.ActionSlot.Positive) StyledDialog.ButtonPresentation.Icon
    else Material3Dialog.DefaultPresentationMapping(slot)
}))
```

`CupertinoFlatDialog.create` exposes the same mapping. It only resolves `Auto`
at actual toolbar positions; explicit per-action `Text`/`Icon` always wins,
and a missing optional icon still falls back to text.

The existing trailing-lambda constructor accepts arbitrary `RowScope` content.
It remains a text-style action and is never automatically interpreted as an
icon action. Both constructors retain the same callback and dismissal behavior.

Every style and the embedded sheet accept `StyledDialog.Properties`:

```kotlin
properties = StyledDialog.Properties(
    dismissOnButtonClick = true,
    dismissOnBackPress = true,
    dismissOnClickOutside = true,
)
```

Callbacks run before automatic dismissal, while data is available. The button's
nullable `dismissOnClick` overrides the global setting. Disabled actions neither
run nor dismiss. Synchronous callback exceptions propagate without automatic
dismissal. For async work, disable automatic dismissal and dismiss after success;
launching a coroutine does not delay the synchronous callback.

`onDismissRequest` defaults to `state::dismiss`. Override it to intercept button,
Back/Escape, outside-click or swipe requests. Keeping the state visible vetoes
dismissal. Direct `state.dismiss()` works regardless of interaction policy; if a
callback already dismissed it, no additional automatic request is emitted.

Outside-click dismissal applies only where a modal has an outside area.
Fullscreen has none; embedded sheets do not intercept background clicks.
Back/Escape uses host-platform Compose support.

## Bottom-sheet configuration

```kotlin
val sheetStyle = StyledDialog.Style.BottomSheet(
    maxWidth = 640.dp,
    maxHeightFraction = 0.9f,
    options = StyledDialog.SheetOptions(
        fixedButtons = true,
        expandToFullscreen = true,
        gesturesEnabled = true,
        dismissOnSwipe = true,
        initiallyExpanded = false,
        peekHeightFraction = 0.5f,
    ),
)

StyledDialog(
    state = state,
    style = sheetStyle,
    buttons = buttons,
    title = { StyledText("Options") },
) { contentPadding ->
    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
    ) {
        // Scrollable content
    }
}
```

Height fractions refer to the viewport after remaining top/side safe-area and
keyboard insets. Bottom safe-area insets are reserved inside the sheet.
Embedded sheets recalculate insets from their actual window bounds: a sheet at the
window's bottom edge reserves navigation space, while one above that area does not.
`peekHeightFraction` must be in `(0, 1]` and is clamped to the maximum sheet
height. `maxHeightFraction` lives only on the style/defaults, not also on options.
Regular sheets wrap to their measured content up to `maxHeightFraction` and the
top edge margin. If the measured sheet is no taller than the peek area, it is
fully visible and treated as expanded; there is no separate collapsed position.
Larger sheets snap between peek and expanded positions. Downward swipes may
dismiss independently of Back/outside-click policy. Nested scrolling expands
the sheet before upward body scrolling; downward scrolling reaches the body's
start before moving the sheet.
Clicking the drag handle toggles between peek and expanded positions when gestures
are enabled and these positions differ; it does not dismiss the sheet.
`expandToFullscreen` makes the expanded position fill the sheet viewport (the
dialog window for modal sheets). The expanded height takes precedence over
`maxHeightFraction`. The sheet animates its handle/title into a safe-area-aware
top bar; themes that place fullscreen actions in the toolbar move
positive/negative actions there while neutral actions remain in the footer.
The option defaults to `false`.

With `fixedButtons = true`, the opaque, shadowless footer is a separate overlay anchored to
the **viewport's bottom edge**, with buttons above any overlapping system navigation area.
It stays stationary until the top of the padded body content reaches its top edge,
then moves down with the sheet, preserving the content padding below the title.
Without a title, the
sheet keeps at least its top corner radius visible above the footer (for example,
16 dp for 16 dp rounded corners) before the footer starts moving. Body space is
reserved and keyboard insets are respected. The bottom safe-area background stays
opaque throughout dragging so body content cannot reappear behind system navigation.
Modal sheets use an edge-to-edge window on Android to draw this background.
Their navigation icon style follows the sheet background and is restored when
the sheet closes. With `expandToFullscreen`, the status-bar icons also adapt as
the top bar reaches the status area. Standalone fullscreen dialogs draw
edge-to-edge and adapt both system-bar icon styles to their backgrounds.
With `false`, the footer moves with the sheet and may be offscreen at peek;
expand to reach it. Its buttons also reserve any remaining bottom safe-area inset.

Sheets attach to the bottom. On viewports at least 600 dp wide, margins also
limit the side edges; compact sheets use the available width up to `maxWidth`.
Sheet and fixed footer have exactly the same capped width and horizontal center.
The top edge respects the configured margin.

### Embedded, non-modal sheets

```kotlin
Box(Modifier.fillMaxSize()) {
    // Underlying screen
    StyledBottomSheet(
        state = sheetState,
        style = sheetStyle,
        buttons = buttons,
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            // Body
        }
    }
}
```

Place the sheet last in a bounded-height parent; its viewport is that parent.
It opens no native modal window and does not block background clicks.
Insets already handled by the parent are respected. Sheets embedded in the middle
of a layout add no unrelated system-navigation padding; sheets reaching the window
edge still protect their content. Embedded sheets do not change system-bar icon styles.
Unbounded scrolling parents without an explicit height are unsupported.

Embedded sheets use their own `StyledBottomSheet.Key`/`DefaultsKey` and
`StyledBottomSheetDefaults`, not the modal default style. Update
`StyledBottomSheet.DefaultsKey` to customize `shape`, `maxWidth`,
`maxHeightFraction` or `edgeMargin`. Explicit style fields still win.

## Theme rendering and replacement

| Theme | Alert actions | Toolbar/fullscreen actions | Sheet actions | Floating-window actions |
|-------|---------------|----------------------------|---------------|--------------------------|
| Material 3 | Native `BasicAlertDialog` host and Material surface; trailing text buttons | Negative leading, positive trailing in toolbar; neutral in footer | Trailing text buttons | Trailing footer actions; default-role actions use text buttons |
| Cupertino | Full-width vertical positive, neutral, negative actions | Centered title, negative leading, positive trailing; neutral in footer | Full-width vertical plain actions | Bottom footer; default-role actions use plain buttons |
| Fluent 2 | Trailing positive, neutral, negative actions | Window-like title bar with all actions in bottom footer | Trailing positive, neutral, negative actions | Positive, neutral, negative footer actions with Fluent action-slot hierarchy |

When `expandToFullscreen` is enabled, Material 3 and Cupertino promote positive
and negative sheet actions into the top bar, matching their fullscreen-dialog
action layout; neutral actions remain at the bottom. Fluent 2 keeps its actions
in the footer.

Material footer actions place the positive action at the trailing edge when
they fit on one row and first when they wrap, preserving the content's layout
direction in both LTR and RTL. Cupertino alerts and sheets retain vertically
stacked positive, neutral, negative actions; its floating-window footer uses
trailing horizontal actions. Explicit icon presentations and renderer
replacements participate in the same action layout.

Toolbar workspaces use public Styled title bars. Sheets share Foundation drag
and fixed-footer geometry with native Material surfaces where applicable;
they are not wrappers around native `ModalBottomSheet`.

Action contexts are scoped only around buttons:
`StyledDialog.Context.DialogAction`, `FullscreenAction` (both workspace styles)
and `BottomSheetAction`. Title/body controls retain their surrounding context.
Replacing `StyledButton` affects standalone and modal/sheet actions alike.
See [Button](Button.md) for role mappings.

All modal styles dispatch through `StyledDialog.Implementation`. Embedded sheets
dispatch through `StyledBottomSheet.Implementation`; replacing one does not
replace the other. Each theme registers both renderers and their defaults.
Register a replacement before updating its defaults.

`StyledModal` and `StyledAdaptive.modal(platform)` were renamed to
`StyledDialog` and `StyledAdaptive.dialog(platform)`. Adaptive APIs now live in
`com.michaelflisar.composestyled.adaptive`; `dialog` returns
`State<StyledDialog.Style>`, so read `.value` when passing its current result.
This is a breaking API change; update all nested type references such as
`StyledModal.Style` and `StyledModal.Buttons` to `StyledDialog.Style` and
`StyledDialog.Buttons`.

The unified **Layout** demo area exercises all styles, adaptive resizing, scoped
default-style overrides, zero to three actions, optional titles, long action
labels, short versus scrollable content, data-backed state and sheet policies.
