`StyledSwitch` is a controlled Boolean component with `checked`, nullable
`onCheckedChange`, `enabled`, `tint`, `onTint` and an optional interaction source.
Its optional composable `label` slot appears to the right of the switch and is
vertically centered. `clickBehavior` defaults to
`StyledToggleBehavior.ControlOnly`; set it to `StyledToggleBehavior.FullItem` to
make the switch and label row toggle together. The row is clipped to `shape`, which
defaults to `StyledSwitchDefaults.shape`; the supplied `modifier` applies to the
combined switch and label. A null callback leaves interaction and semantics to
the parent.

The Cupertino switch uses a 51 x 31 dp track with a shadowed thumb; Fluent 2 uses
a 40 x 20 dp track with an outlined off state and hover/pressed colors. Both
animate state changes, support RTL and keyboard focus, and retain `tint` for the
checked track and `onTint` for the checked thumb.

```kotlin
var checked by remember { mutableStateOf(false) }
StyledSwitch(
    checked = checked,
    onCheckedChange = { checked = it },
    clickBehavior = StyledToggleBehavior.FullItem,
    label = { StyledText("Enable notifications") },
)
```
