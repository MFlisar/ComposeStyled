# Swipe Actions

The swipe action components are shared compositions in
`com.michaelflisar.composestyled.ui.specials`. They work across themes without
their own renderer registration.
`StyledSwipeActionsDefaults` provides the shared default threshold, shape,
colors, padding, alignment, spacing, and overscroll behavior. Theme-dependent
values resolve from the active `StyledTheme`; each component parameter can be
overridden directly.

## Trigger an action when swiped

`StyledSwipeToActionBox` moves its content horizontally and accepts one
`StyledSwipeAction` for each logical side (`Start` and `End`). A side is enabled
when its action is enabled and `onSwipe` is provided. The action's default
content uses its optional
`label` and `icon`; provide `content` for a custom composable. Content and color
providers receive a `StyledSwipeActionScope` with `side`, `progress` from `0f`
to `1f`, and `willTrigger`, which becomes true after the swipe reaches the
threshold. The `revealBackgroundColor` and `revealContentColor` parameters are
fixed base colors; an action can override them with fixed colors or
progress-aware providers. On release at or beyond the threshold, `onSwipe`
receives the triggered side and the content animates back.
The foreground content and `modifier` determine the box size, subject to the
theme's minimum interactive size; the revealed slot is measured to that size
and cannot enlarge the box. The foreground moves as an opaque surface, revealing
the action slot only in the exposed area. The stationary container's `shape`
clips the foreground and reveal slot together; the moving foreground has a
square inner edge, so its outer corners stay fixed while it slides.
`backgroundColor` and `contentColor` style the foreground, while
`revealBackgroundColor` and `revealContentColor` are fixed `Color` values for
the revealed surface and its local content color. Their defaults come from
the foreground's `backgroundColor` and `contentColor`. Action-level color
providers take precedence over fixed action colors, which take precedence over
these base colors. Use the `scope` passed to action content when it needs to
change as the swipe advances.
Setting `revealBackgroundColor` to `Color.Transparent` exposes whatever is
behind the swipe box; `backgroundColor` is painted only on the moving
foreground.
`contentPadding` defaults to the theme's medium padding and applies to both the
foreground and swipe slot.
`contentAlignment` aligns the foreground and determines the revealed slot's
vertical alignment; the revealed content is placed against the exposed logical
edge (`Start` or `End`), following the current layout direction.

```kotlin
StyledSwipeToActionBox(
    shape = StyledTheme.shapes.medium,
    backgroundColor = StyledTheme.colorScheme.surfaceVariant,
    contentColor = StyledTheme.colorScheme.onSurfaceVariant,
    contentPadding = PaddingValues(all = StyledTheme.padding.medium),
    contentAlignment = Alignment.CenterStart,
    overscrollBehavior = StyledSwipeOverscrollBehavior.None,
    actionFromStart = StyledSwipeAction(
        label = "Archive",
        backgroundColorProvider = { scope ->
            lerp(Color.Transparent, StyledTheme.colorScheme.success, scope.progress)
        },
        contentColorProvider = { scope ->
            lerp(
                StyledTheme.colorScheme.onSurfaceVariant,
                StyledTheme.colorScheme.onSuccess,
                scope.progress,
            )
        },
    ),
    actionFromEnd = StyledSwipeAction(
        label = "Delete",
        backgroundColorProvider = { scope ->
            lerp(Color.Transparent, StyledTheme.colorScheme.error, scope.progress)
        },
        contentColorProvider = { scope ->
            lerp(
                StyledTheme.colorScheme.onSurfaceVariant,
                StyledTheme.colorScheme.onError,
                scope.progress,
            )
        },
    ),
    onSwipe = { side ->
        when (side) {
            StyledSwipeActionSide.Start -> archiveItem()
            StyledSwipeActionSide.End -> deleteItem()
        }
    },
) {
    StyledText("Swipe this item")
}
```

The default threshold is `56.dp`. `actionFromStart` is revealed by swiping from
the logical start toward the end; `actionFromEnd` is revealed by swiping from
the end toward the start. These directions follow the current layout direction
(left-to-right in LTR and right-to-left in RTL for the start action). The
content slot is display-only, not a button.
`overscrollBehavior` defaults to `StyledSwipeOverscrollBehavior.None`, limiting
the drag to the trigger threshold. `Free` allows movement up to the box width;
`Sticky` starts without a sudden speed change at the threshold, then continuously
reduces movement as overscroll grows, approaching the box width asymptotically.
In both modes the trigger threshold remains unchanged and `progress` stays
capped at `1f`.

## Reveal actions to choose from

`StyledSwipeRevealActionBox` reveals one or more full-height text buttons on
either side. It stays open after the drag. Selecting an action runs its
callback and closes the reveal; tapping the visible content or swiping back
also closes it. The box enforces the theme's minimum interactive size. Buttons
use the active `StyledButton` renderer and theme.
The stationary container uses `shape` to clip its background, revealed actions
and moving foreground together. The moving foreground and reveal buttons have
square inner edges, while the container's outer corners remain fixed during a
swipe. The foreground uses `backgroundColor` and `contentColor`; the box's
`revealBackgroundColor` and `revealContentColor` are fixed colors used as the
action buttons' defaults, which follow the foreground colors unless explicitly
overridden. Each action can set fixed `backgroundColor` and `contentColor`
values; `Color.Unspecified` falls through to the box's reveal color. These
colors are painted only behind the active side's action buttons, including
during overscroll; the other side and the uncovered overscroll area show
`backgroundColor`. Actions on the inactive side stay invisible and disabled,
even if overscrolling exposes that side.
The action's resolved background and content colors are also passed to its
`StyledButton` customization, so the active theme's button feedback (including
Material 3's ripple) uses the action's content color.
Each side's actions are measured independently, so a long action set on one
side does not reduce the space available to the other side on narrow boxes.
When actions are present, the box's minimum height includes the vertical
`contentPadding` so the full-height buttons have room for their content. Each
action color defaults to the box's fixed reveal color.
Actions do not expose a button role: their appearance is controlled by the box
and optional per-action colors. The box keeps the buttons full height and uses
the active `StyledButton` renderer for interaction and semantics.

`contentPadding` defaults to the theme's medium padding and insets each action
button's label and icon without reducing the button's full height.
`contentAlignment` defaults to `Alignment.CenterStart` and aligns the
foreground content. `actionSpacing` defaults to `0.dp`, so the action buttons
and foreground meet without gaps; set it to a positive value to add spacing.
`overscrollBehavior` defaults to
`StyledSwipeOverscrollBehavior.None`, limiting the drag to the width needed to
reveal all actions. `Free` allows movement up to the box width, while `Sticky`
starts without a sudden speed change beyond the action width, then continuously
reduces movement as overscroll grows and approaches the box width asymptotically.
Both overscroll modes settle back to the fully revealed action width on release.

```kotlin
StyledSwipeRevealActionBox(
    shape = StyledTheme.shapes.medium,
    backgroundColor = StyledTheme.colorScheme.surfaceVariant,
    contentColor = StyledTheme.colorScheme.onSurfaceVariant,
    contentPadding = PaddingValues(all = StyledTheme.padding.medium),
    contentAlignment = Alignment.CenterStart,
    overscrollBehavior = StyledSwipeOverscrollBehavior.None,
    actionsFromStart = listOf(
        StyledSwipeRevealAction(
            label = "Archive",
            backgroundColor = StyledTheme.colorScheme.success,
            contentColor = StyledTheme.colorScheme.onSuccess,
            onClick = { archiveItem() },
        ),
        StyledSwipeRevealAction(
            label = "Delete",
            backgroundColor = StyledTheme.colorScheme.error,
            contentColor = StyledTheme.colorScheme.onError,
            onClick = { deleteItem() },
        ),
    ),
) {
    StyledText("Swipe to reveal actions")
}
```

Each action requires an `onClick` and uses one of two constructors: provide a
required `label` and optional `IconComposable`, or provide a custom `content`
slot instead. The content constructor receives a
`StyledSwipeRevealActionScope` with the action's `side`, `progress`, and
`isOpen`. Both constructors accept fixed `backgroundColor` and `contentColor`
overrides. The `onClick` callback receives a
`StyledSwipeRevealActionContext` containing the revealed side and open state.
`actionsFromStart` is revealed by swiping from the logical start toward the
end; `actionsFromEnd` is revealed by swiping from the end toward the start.
The reveal opens when the drag passes half the available action width.
