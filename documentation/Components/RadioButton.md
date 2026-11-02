`StyledRadioButton` has controlled `selected` state, a nullable `onClick`, `enabled`,
`tint` and an optional interaction source. Its optional composable `label` slot is
placed to the right of the radio button and centered vertically, with theme spacing
between them. The supplied `modifier` applies to the combined radio button and
label when the slot is present. `clickBehavior` defaults to
`StyledToggleBehavior.ControlOnly`; set it to `StyledToggleBehavior.FullItem` to
make the radio button and label row selectable together. The row is clipped to
`shape`, which defaults to `StyledRadioButtonDefaults.shape`. A null callback leaves
interaction and semantics to a parent, such as a selectable row.

```kotlin
var selectedOption by remember { mutableStateOf("first") }

StyledRadioButton(
    selected = selectedOption == "first",
    onClick = { selectedOption = "first" },
    clickBehavior = StyledToggleBehavior.FullItem,
    label = { StyledText("First option") },
)
```
