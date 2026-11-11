# ComposeDialogs

The `composedialogs` integration module adapts the ComposeDialogs `ui-color`,
`ui-date`, `ui-frequency`, and `ui-time` picker composables to Styled controls.
The Styled picker dialogs do not require a global provider installation:

```kotlin
DialogColor(
    state = colorDialog,
    color = selectedColor,
    title = { StyledText("Choose a color") },
    buttons = doneButton(),
)
```

The `DialogColor`, `DialogDate`, `DialogFrequency`, and `DialogTime` APIs take a
Styled `DialogState` and wrap the picker content
in a Styled dialog. Their picker UI is rendered through Styled controls and
uses the `com.michaelflisar.composedialogs.styled.provider` implementations.

`DialogDate` requires a `DatePickerRange` and `DatePickerSetup` from
`ui-date` so callers can define the allowed years and localized formatting.
`DialogFrequency` takes one `MutableState<Frequency>` from
`ui-frequency`; the immutable frequency model contains its type, interval, and
type-specific date and time values. For example:

```kotlin
val frequency = remember {
    mutableStateOf<Frequency>(
        Frequency.Daily(time = LocalTime(8, 30), interval = 1),
    )
}

DialogFrequency(
    state = frequencyDialog,
    frequency = frequency,
)
```

Use `supportedTypes` to limit the available `Frequency.Type` choices. The
dialog tracks interval validation from the picker and disables its positive
action while the interval is invalid.

The `DialogInfo`, `DialogInput`, `DialogList`, `DialogMenu`, `DialogNumber`, and
`DialogProgress` APIs are
also composed from Styled controls. `DialogMenu` renders nested entries
as a list; each submenu starts with a back row that returns to the parent list.
