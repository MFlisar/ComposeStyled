# Expand icon

`StyledExpandIcon` is a non-interactive state indicator composed from the active
`StyledIcon` renderer. The caller owns expansion and interaction; the icon does
not expand content or position a popup.

## Automatic rotation

Pass a Boolean to animate between the collapsed and expanded orientations:

```kotlin
StyledExpandIcon(expanded = expanded)
```

`expandDirection` describes the direction of the expanded content. A rotating
icon points in that direction when collapsed and in the opposite direction
when expanded. A custom icon must point down in its original orientation.

| `ExpandDirection` | Collapsed | Expanded |
|---|---|---|
| `Down` (default) | Down | Up |
| `Up` | Up | Down |
| `Start` | Start | End |
| `End` | End | Start |

`Start` and `End` follow `LocalLayoutDirection`: Start is left in LTR and right
in RTL. Up and Down do not change in RTL.

`rotationDirection` selects the half-turn's path, independently of placement
and expansion direction. Its default is `CounterClockwise`; `Clockwise`
reaches the same expanded orientation via the opposite path.

```kotlin
StyledExpandIcon(
    expanded = expanded,
    expandDirection = StyledExpandIcon.ExpandDirection.End,
    rotationDirection = StyledExpandIcon.RotationDirection.Clockwise,
    iconMode = StyledExpandIcon.IconMode.Rotating(
        icon = Icons.Default.ArrowDropDown.toIconComposable(),
    ),
)
```

The default rotating icon is the library's downward dropdown arrow.
`color` inherits the current Styled content color, `size` defaults to the
theme's icon size, and `modifier` is applied to the indicator. `size = null`
leaves sizing to the icon and modifier.

## Replace icons, optionally with crossfade

Supply separate `IconComposable` values for the two states:

```kotlin
StyledExpandIcon(
    expanded = expanded,
    iconMode = StyledExpandIcon.IconMode.Replacing(
        collapsedIcon = Icons.Default.Add.toIconComposable(),
        expandedIcon = Icons.Default.Remove.toIconComposable(),
        crossfade = true,
    ),
)
```

`crossfade` defaults to `false` for an immediate switch. When enabled, Compose
`Crossfade` animates the change, including changes while a transition is running.
Both icons use the active Styled icon renderer and the same color and size.
No rotation is applied: `expandDirection` and `rotationDirection` are ignored
in this mode. There are no slide or scale transitions.

Examples using `Icons.Default` require the corresponding Compose Material icons
dependency and `com.michaelflisar.composestyled.classes.toIconComposable`.

## Direct rotation progress

Use the Float overload to drive rotation from a slider, gesture or external
animation without a second internal animation:

```kotlin
StyledExpandIcon(
    expansionProgress = progress,
    expandDirection = StyledExpandIcon.ExpandDirection.Down,
)
```

`0f` is fully collapsed, `1f` fully expanded, and intermediate values interpolate
the half-turn in the selected `rotationDirection`. This overload accepts only
`IconMode.Rotating`, not `Replacing`. Progress must be finite and within
`0f..1f`; values outside this range, NaN and infinity throw
`IllegalArgumentException`. External animations must stay within that range.

## Migration

The old `sideIsLeft` and `icon` parameters have been removed:

| Previous argument | Replacement |
|---|---|
| `sideIsLeft = true` | `rotationDirection = StyledExpandIcon.RotationDirection.CounterClockwise` (default) |
| `sideIsLeft = false` | `rotationDirection = StyledExpandIcon.RotationDirection.Clockwise` |
| `icon = customIcon` | `iconMode = StyledExpandIcon.IconMode.Rotating(icon = customIcon)` |

The default direction remains Down, with the same collapsed and expanded
orientations. Automatic rotation uses a non-overshooting tween so its progress
always satisfies the Float overload's contract.

The interactive primitives demo shows all directions, RTL resolution, both
replacement styles and direct progress control.
