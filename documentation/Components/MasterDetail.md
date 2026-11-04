# Master/detail layout

`StyledMasterDetail` is a reusable layout for showing related master and detail
content in one pane or side by side. It does not choose the mode, own selection
state or infer navigation relationships. Resolve the mode with
`StyledAdaptive.masterDetail` or supply one directly.

```kotlin
var selectedItem by remember { mutableStateOf<String?>(null) }

StyledAdaptiveContainer(Modifier.fillMaxSize()) { adaptiveInfo ->
    val mode = StyledAdaptive.masterDetail(adaptiveInfo)
    StyledMasterDetail(
        mode = mode.value,
        detailData = selectedItem,
        master = {
            StyledButton(onClick = { selectedItem = "item" }) {
                StyledText("Open item")
            }
        },
        detail = { item ->
            if (item == null) {
                StyledText("Select an item")
            } else {
                StyledText("Details for $item")
            }
        },
    )
}
```

`detailData` is the selected item, or `null` when there is none. In
`SinglePane`, `master` is shown for `null`; otherwise `detail` is composed with
the selected item. In `SideBySide`, both slots are always composed and `detail`
receives the nullable item so the caller can render an empty state. Selection,
back behavior and saveable state remain the caller's responsibility.

`masterFraction` (default `0.4f`) sets the master's share of the available
width after `paneSpacing` is reserved. The layout mirrors naturally in RTL.
