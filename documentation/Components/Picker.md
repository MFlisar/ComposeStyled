# Picker

`StyledPicker` displays a non-editable value and starts a picker from its
trailing icon. It is composed from `StyledInput` and `StyledInputAction`, so it
uses the active theme without a separate renderer. The internal input is
read-only and does not receive interaction, but the picker keeps the enabled
appearance whenever `enabled` is `true`; setting `enabled = false` both disables
the picker action and shows its disabled appearance.

Provide an `IconComposable` (for example, with
`Icons.Default.ArrowDropDown.toIconComposable()`), and handle the action with
`onStartPicker`. The picker accepts a controlled string and supports `Auto`,
`Filled` and `Outlined` input variants. `Auto` follows the filled Material 3
default; `StyledOutlinedPicker` fixes the style to `Outlined`. Set
`clickOnIconOnly = false` to make the entire field clickable instead of only its
icon. The default comes from `StyledPickerDefaults.clickOnIconOnly` and is `true`.

```kotlin
StyledPicker(
    value = selectedOption,
    onStartPicker = { openOptions() },
    icon = Icons.Default.ArrowDropDown.toIconComposable(),
    label = "Selection",
)
```
