# Side navigation

`StyledSideNavigation` replaces the former rail API. It supports compact
icon-oriented navigation and expanded sidebar rows across all themes.
For a temporary, edge-revealed overlay rather than persistent navigation, see
[Side sheet](SideSheet.md).

```kotlin
val expanded = rememberSaveable { mutableStateOf(false) }

StyledScaffold(
    sideNavigation = {
        StyledSideNavigation(
            items = destinations,
            selectedKey = selectedKey,
            onItemClick = { selectedKey = it.key },
            mode = StyledSideNavigation.Mode.Expandable,
            expanded = expanded,
            title = { StyledText("My app") },
            header = { StyledText("Workspace") },
            footer = { StyledText("Account information") },
        )
    },
) { innerPadding ->
    StyledText("Content", Modifier.padding(innerPadding).consumeWindowInsets(innerPadding))
}
```

## Modes and state

| Mode | Presentation | Toggle |
|---|---|---|
| `Compact` (default) | Fixed compact width | None |
| `Expandable` | Caller-controlled compact or expanded | Built in |
| `Expanded` | Fixed expanded width | None |

The `MutableState<Boolean>` overload remembers saveable expansion state by
default, initially `false`. A controlled overload takes `expanded: Boolean` and
`onExpandedChange`. Fixed modes ignore that value without changing it or
calling the callback. Hoist expansion state above conditional navigation slots
when it must survive switching to bottom navigation.

`compactLabelVisibility` supports `Hidden` (default), `Selected`, and `Always`.
It only affects compact items; expanded items show their supplied label beside
the icon. Labels retain their accessibility semantics when visually hidden.
Provide an accessible icon description if there is no label.

Both the list API and the free-content API use the same registered renderer:

```kotlin
StyledSideNavigation(mode = StyledSideNavigation.Mode.Expanded) {
    StyledSideNavigationItem(
        selected = selectedKey == "home",
        onClick = { selectedKey = "home" },
        label = { StyledText("Home") },
    ) {
        StyledIcon(
            icon = Icons.Default.Home.toIconComposable(),
            contentDescription = null,
        )
    }
}
```

## Title, header and footer

The optional title belongs to the toggle row, not to the header. In expandable
mode the toggle remains visible in both states; the title only appears expanded.
Fixed expanded navigation can show a title without a toggle. Compact navigation
does not reserve an empty title row. The title is start-aligned, beside the
toggle when one is present.
All three themes align the title with the expanded destination labels,
including in fixed expanded mode. Fluent 2 uses an inset selection stroke and
a compact icon column to avoid a rail-sized gap before the text.
The toggle icon is independently aligned with the destination icon centers;
aligning the title does not shift the toggle toward the outer edge.

Destination icons keep their horizontal position while the navigation changes
width. Expanded labels are measured at a stable width and revealed beside the
icons, from left to right in LTR. Text is not scaled or repeatedly rewrapped,
and items do not recenter as more space becomes available. Placement and reveal
are mirrored in RTL. Optional compact labels appear below the icons when fully
compact, yielding to side labels when expansion starts. Each label is composed
once and keeps its remembered state when moving between these positions.

### Expansion icon

The default toggle is a non-rotating menu icon. Supply `toggleIcon` to replace
only its visual content; the navigation still supplies the themed
`StyledIconButton`, click action and accessible expand/collapse description.
The toggle inherits the navigation's normal `contentColor`, not the selected
destination's accent color.
Do not add another clickable control inside this slot.

The slot receives `StyledSideNavigation.ToggleState`:

| Property | Meaning |
|---|---|
| `targetExpanded` | Requested expanded state, including during transitions |
| `expansionProgress` | Actual width-animation progress: `0f` compact, `1f` expanded |

Use the progress overload of [StyledExpandIcon](expand-icon.md) for a synchronized
rotation, rather than starting another animation:

```kotlin
StyledSideNavigation(
    mode = StyledSideNavigation.Mode.Expandable,
    toggleIcon = { state ->
        StyledExpandIcon(
            expansionProgress = state.expansionProgress,
            expandDirection = StyledExpandIcon.ExpandDirection.End,
        )
    },
) {
    // StyledSideNavigationItem(...)
}
```

The arrow points toward expansion when compact and toward collapse when
expanded. `ExpandDirection.End` handles LTR/RTL placement. Do not use the Boolean
overload for this rotation: it would start an independent animation.

For two different icons, use the Boolean overload with `IconMode.Replacing`:

```kotlin
toggleIcon = { state ->
    StyledExpandIcon(
        expanded = state.targetExpanded,
        iconMode = StyledExpandIcon.IconMode.Replacing(
            collapsedIcon = Icons.Default.Menu.toIconComposable(),
            expandedIcon = Icons.Default.Close.toIconComposable(),
        ),
    )
}
```

Replacing icons are used unchanged, without rotation. Set `crossfade = true`
only if you intentionally want a separate icon crossfade; the default swaps
immediately at the target-state change.

Progress preserves its current value on animation reversal. It follows width,
not the entire header/footer transition: it remains `0f` while expansion first
creates their vertical space and reaches `0f` before collapse removes that space.
Fixed modes have no toggle and do not compose the icon slot.

All list and free-content overloads accept this slot. The list-based
`StyledAdaptiveScaffold` forwards its `sideNavigationToggleIcon` slot to side
navigation; it has no effect on bottom navigation.

### Header and footer

Header and footer appear **only expanded** and remain outside the destination
scroll area. The footer is pinned to the bottom. Keep fixed slots reasonably
small so destinations retain usable space in short windows.

The transition has two phases:

- Collapse narrows the sidebar and fades the fixed-slot content, then shrinks
  the now-empty header/footer areas vertically.
- Expand first creates their vertical space while the content is invisible,
  then widens the sidebar and reveals it.

Header/footer content is measured at stable expanded width, avoiding text
reflow while the sidebar narrows. A new toggle request reverses from the current
animation values. Invisible fixed slots cannot receive focus and are excluded
from accessibility. Slots leave composition once fully collapsed; hoist any
state that must survive this.

## Scrolling and layout

`scrollable = true` is the default. Only destinations scroll vertically.
Use Scaffold or an explicit height to provide a bounded navigation viewport.
Use `scrollable = false` for a fixed destination region; overflow is clipped,
not silently converted to scrolling. Do not use `ColumnScope.weight` inside
scrollable destination content, which is measured with unbounded content height.

Pass a `ScrollState` to control or preserve position. `showScrollbar` defaults
to the desktop/web platform preference and is independent of scrolling:
`showScrollbar = false` keeps touch and wheel scrolling available.

Scaffold places `sideNavigation` at logical start (left in LTR, right in RTL),
between the top and bottom bars. Body padding follows its measured width,
including intermediate animation widths. Apply that padding to body content.
No extra system-bar padding is needed for navigation inside Scaffold; FABs and
snackbars remain Scaffold-wide overlays.

### Safe-area insets

Like the top and bottom bars, side navigation has a `windowInsets` parameter.
It defaults to `WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start)`.
Standalone navigation therefore protects its title, toggle, fixed slots and
destinations from top/bottom and outer start-side system insets, including iOS
safe areas supplied by the host. Start follows the layout direction; the inner
end edge is not padded by default.

The background covers the inset area. The configured compact/expanded widths
describe the navigation area before additional horizontal inset padding; a
remaining start inset increases the total measured width rather than reducing
the space available for labels. Already consumed insets are not applied again.

Inside Scaffold, placement already reserves space between bars and within the
Scaffold's inset area. Scaffold consumes those covered offsets for the navigation
slot, leaving side navigation to handle only any remaining requested insets.
The body padding still includes its entire measured width, including any
remaining inset padding and intermediate animation widths.

To disable the navigation's own inset handling, for example in an embedded
component preview, pass `windowInsets = WindowInsets(0, 0, 0, 0)`. This is independent
of Scaffold's `contentWindowInsets` and the other bars' `windowInsets`.
Apply and consume Scaffold's body padding at the content root; see
[Scaffold](Scaffold.md#safe-areas-and-inset-ownership).

The sealed `StyledNavigationMode` in the `classes` package distinguishes
`Bottom` from `Side(mode)`, where `mode` is a `StyledSideNavigation.Mode`.
`StyledAdaptive.navigation(...)` returns an observable
`State<StyledNavigationMode>` based on platform and layout size; read `.value`
for the current mode. All adaptive APIs are in
`com.michaelflisar.composestyled.adaptive`.

`StyledAdaptiveScaffold` passes the side mode through to
`StyledSideNavigation.Mode`. Both overloads can hoist expandable state through
`sideNavigationExpanded`; custom navigation should pass that same state to its
navigation UI.

## Themes and customization

Use `StyledSideNavigationDefaults.customize` for shared selected/unselected
content colors and the selected indicator. Unspecified values keep renderer
defaults. Unselected item text and icons inherit the navigation's `contentColor`,
which defaults to `StyledTheme.contentColorFor(containerColor)`. An explicit
`unselectedItemContentColor` customization overrides that inherited color;
selected items retain their renderer's selected colors. Change widths through the registry:

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = themeProvider,
    customization = {
        update(StyledSideNavigation.DefaultsKey) {
            copy(compactWidth = 96.dp, expandedWidth = 300.dp)
        }
    },
) { /* ... */ }
```

Widths must be finite and positive, and expanded width must be at least compact
width.

| Theme | Selection treatment |
|---|---|
| Material 3 | Native expressive rail item with a pill indicator |
| Fluent 2 | Rounded accent stroke on the start side of the icon, animated in/out without moving content |
| Cupertino | Rounded selected-row background in an adaptive sidebar |

`selectedItemIndicatorColor` customizes the corresponding pill, stroke or row
background. Fluent 2 retains separate hover, press and keyboard-focus feedback.
Its compact icon-only mode is a library adaptation; the Fluent Web Nav guidance
does not define an icon-only layout.

Cupertino and Fluent 2 use theme-owned adapters over a shared Foundation
renderer. Cupertino is an adaptive sidebar, not a native UIKit rail; adaptive
navigation choices on the iPhone are unchanged.
Material 3 uses native expressive `WideNavigationRailItem` controls inside a
shared non-modal frame. Its icon position stays fixed while expanded label
content is revealed. It uses the native icon-only indicator geometry with
symmetric padding: with standard Android 24 dp icons, the compact icon-only
indicator is 56 dp wide and high, with the icon centered. The side label and its
spacing reveal inside that indicator as the rail expands, without moving the
icon or leaving an empty label gap when compact. Compact labels, when
requested, are placed below the icon and can increase the row height. The shared
inter-item spacing remains separate from the native selected indicator.

This is a breaking migration: use `StyledSideNavigation`,
`StyledSideNavigationItem`, `StyledSideNavigationDefaults`, the corresponding
theme renderer names, and Scaffold's `sideNavigation` slot. The old rail API has
no deprecated forwarding wrappers.

The default toggle has changed from an arrow to a menu icon. Use the slot above
to restore a progress-driven arrow or another custom presentation.

Custom side-navigation renderers must forward `windowInsets` and the new
`toggleIcon` argument from `StyledSideNavigation.Implementation.Render`.
When using `FoundationSideNavigationLayout`, pass `onExpandedChange` and
`toggleIcon`; the frame supplies the button and constructs the slot state from
its actual width animation.
