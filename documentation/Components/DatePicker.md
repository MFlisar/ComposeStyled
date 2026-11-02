# Date Picker

`StyledDatePicker` is an inline, controlled calendar that selects a
`kotlinx.datetime.LocalDate`. It uses the active theme's Styled buttons and
text, starts weeks on Monday, and can disable dates before or after optional
bounds.

```kotlin
val selectedDate = remember { mutableStateOf(LocalDate(2026, 6, 15)) }

StyledDatePicker(
    value = selectedDate,
    minimumDate = LocalDate(2026, 1, 1),
    maximumDate = LocalDate(2026, 12, 31),
)
```

Use the controlled overload with `value` and `onValueChange` when state is held
elsewhere. Set `enabled = false` to disable month navigation and date changes.
The displayed month is formatted as `YYYY-MM`; weekday labels are currently
two-letter English abbreviations. The internally managed visible month is
saved across supported host recreation; the selected date remains caller-owned.
