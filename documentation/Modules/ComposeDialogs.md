# ComposeDialogs

The `composedialogs` integration module adapts the ComposeDialogs `ui-color`,
`ui-date`, `ui-frequency`, and `ui-time` picker composables to Styled controls.
The Styled picker dialogs do not require a global provider installation:

```kotlin
StyledDialogColor(
    state = colorDialog,
    color = selectedColor,
    title = { StyledText("Choose a color") },
    buttons = doneButton(),
)
```

The `StyledDialogColor`, `StyledDialogDate`, `StyledDialogFrequency`, and
`StyledDialogTime` APIs take a Styled `DialogState` and wrap the picker content
in a Styled dialog. Their picker UI is rendered through Styled controls and
uses the `com.michaelflisar.composedialogs.styled.provider` implementations.

`StyledDialogDate` requires a `DatePickerRange` and `DatePickerSetup` from
`ui-date` so callers can define the allowed years and localized formatting.
`StyledDialogFrequency` takes the picker selection states directly, matching
the UI module's stateless domain API. For example:

```kotlin
StyledDialogFrequency(
    state = frequencyDialog,
    selectedType = selectedType,
    selectedTime = selectedTime,
    selectedDayOfWeek = selectedDayOfWeek,
    selectedDayOfMonth = selectedDayOfMonth,
    selectedMonthOfYear = selectedMonthOfYear,
    selectedInterval = selectedInterval,
)
```

The `StyledDialogInfo`, `StyledDialogInput`, `StyledDialogList`,
`StyledDialogMenu`, `StyledDialogNumber`, and `StyledDialogProgress` APIs are
also composed from Styled controls. `StyledDialogMenu` renders nested entries
as a list; each submenu starts with a back row that returns to the parent list.
