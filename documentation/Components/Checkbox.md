`StyledCheckbox` is a controlled Boolean component. It accepts `checked`, nullable
`onCheckedChange`, `enabled`, `tint`, `onTint` and an optional interaction source.
The optional composable `label` slot is placed to the right of the checkbox and
centered vertically, with theme spacing between them. The supplied `modifier`
applies to the combined checkbox and label when the slot is present.
With a label, `clickBehavior` defaults to `StyledToggleBehavior.FullItem`, so the
label and checkbox share one touch target with at least the theme's minimum
interactive height and theme-medium horizontal padding for clear click feedback.
Set it to `StyledToggleBehavior.ControlOnly` when only the checkbox should
toggle. The row is clipped to `shape`, which defaults to
`StyledCheckboxDefaults.shape`.
The Boolean-state overload follows these same defaults. With a null callback,
a parent can continue to own the interaction and semantics.

```kotlin
var checked by remember { mutableStateOf(false) }
StyledCheckbox(
    checked = checked,
    onCheckedChange = { checked = it },
    label = { StyledText("Enable notifications") },
)
```
