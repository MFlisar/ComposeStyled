# Range slider

`StyledRangeSlider<T>` chooses two typed endpoints within `valueRange`.
It supports the same four number types and `stepSize` rules as
[`StyledSlider`](Slider.md), with `T : Number` and `T : Comparable<T>`.

```kotlin
val selectedRange = remember { mutableStateOf(20..30) }
StyledRangeSlider<Int>(
    value = selectedRange,
    valueRange = 0..100,
    stepSize = 1,
    minimumWindowSize = 5,
    maximumWindowSize = 10,
)
```

For saveable range state, use `numberRangeSaver<T>()`. It restores the standard range type even
when used from a generic function:

```kotlin
val selectedRange = rememberSaveable(stateSaver = numberRangeSaver<Int>()) {
    mutableStateOf<ClosedRange<Int>>(20..30)
}
```

`minimumWindowSize` and `maximumWindowSize` optionally constrain the **size**
of the selected window. For example, with `valueRange = 0..100`,
`minimumWindowSize = 5`, and `maximumWindowSize = 10`, the user can select a
window between 5 and 10 units wide.
When moving either endpoint would exceed a size limit, the other endpoint
moves along with it. Initial values are clamped to `valueRange` and adjusted
to satisfy the configured size limits. Both limits default to `null` (no
additional constraint).

Window sizes have type `T`, must be finite and non-negative, and must be
multiples of `stepSize` for discrete grids (including the implicit integral step
of one). The minimum cannot exceed the available span or the maximum; a maximum
larger than the span is capped to that span. Invalid limits are rejected.

The state overload accepts `MutableState<IntRange>`, `MutableState<LongRange>`,
standard Float/Double ranges, including states declared as
`MutableState<ClosedRange<T>>`. Construct ranges with Kotlin's `..` operator.
The state overload retains the concrete standard range type on writeback.
Custom range implementations are accepted by the value/callback overload,
not by the automatic state-writeback overload. Byte and Short are not supported.

The callback overload emits `ClosedRange<T>`:

```kotlin
var selected by remember {
    mutableStateOf<ClosedRange<Double>>(0.2..0.8)
}
StyledRangeSlider(
    value = selected,
    onValueChange = { selected = it },
    valueRange = 0.0..1.0,
    stepSize = 0.1,
)
```

Both overloads support `enabled`, an optional `interactionSource`, and
`onValueChangeFinished`. `activeColor` and `inactiveColor` customize the selected
and unselected track. Renderer coordinates are relative Float positions, with
the same precision and grid limits described in the Slider guide.
