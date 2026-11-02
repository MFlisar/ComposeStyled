# Slider

`StyledSlider<T>` supports `Int`, `Long`, `Float` and `Double`,
using `T : Number` with `T : Comparable<T>`. Values, range endpoints and
`stepSize` use the same type. The type can normally be inferred from `value`.

```kotlin
val volume = remember { mutableStateOf(50) }
StyledSlider<Int>(
    value = volume,
    valueRange = 0..100,
    stepSize = 5,
)
```

The `MutableState<T>` overload updates state directly; the value/callback overload
accepts `value: T` and `onValueChange: (T) -> Unit`. Both overloads
support `enabled`, an optional `interactionSource`, and
`onValueChangeFinished`. `activeColor` and `inactiveColor` customize the filled
and remaining track.

## Step size

`stepSize` replaces the old `steps` parameter. It is the distance between values,
not the number of intermediate tick marks. It must be positive, finite, and
divide the range span evenly (allowing floating-point representation tolerance).
The grid is anchored at `valueRange.start`. Values are clamped and snapped to it.
Invalid configurations throw `IllegalArgumentException`.

When `stepSize` is `null`, integral types use a step of one; Float and Double
are continuous. The default `valueRange` is a typed zero-to-one range.
Specify an appropriate range for integer controls.

```kotlin
val opacity = remember { mutableStateOf(0.5) }
StyledSlider(
    value = opacity,
    valueRange = 0.0..1.0,
    stepSize = 0.1,
)
```

Use Kotlin's `..` operator for ranges. Byte, Short and custom Number subclasses
are not supported and are rejected with `IllegalArgumentException`.

## Numeric precision

Renderers retain their Float-based contract, but receive relative grid positions
rather than absolute numeric values. Integral arithmetic preserves Long endpoints
and grid values even near `Long.MIN_VALUE` or `Long.MAX_VALUE`. Floating-point
interaction still has Float position resolution; a Double slider is not a
full-precision numeric editor.

A discrete grid supports at most 16,777,216 intervals, the consecutive integer
precision limit of Float. Larger grids are rejected explicitly; increase
`stepSize` instead of silently losing selectable values. Continuous ranges must
have finite endpoints and a finite positive span.
