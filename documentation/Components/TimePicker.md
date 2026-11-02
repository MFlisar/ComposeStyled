# Time Picker

`StyledTimePicker` is an inline 24-hour time selector for
`kotlinx.datetime.LocalTime`. Hour buttons wrap between 00 and 23; minute
buttons wrap across the hour boundary and preserve seconds and nanoseconds.

```kotlin
val selectedTime = remember { mutableStateOf(LocalTime(14, 30)) }

StyledTimePicker(
    value = selectedTime,
    minuteStep = 5,
)
```

Use the controlled overload with `value` and `onValueChange` when state is held
elsewhere. `minuteStep` must be between 1 and 59 and defaults to 1. Set
`enabled = false` to disable all adjustments.
