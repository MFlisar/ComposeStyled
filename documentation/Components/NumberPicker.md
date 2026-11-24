# Number picker

`StyledNumberPicker` is a compound control built from `StyledInput` and
`StyledIconButton` with an explicit Tertiary role, so it follows the active
theme without a separate renderer registration. Supply a `NumberParser` for
parsing, formatting and step changes, and a `NumberValidator` for input
validation. It supports controlled values, a `MutableState` overload, optional
increment/decrement buttons and wrapping between the values in `overrun`.
Number pickers accept the input `Auto`, `Filled` and `Outlined` variants;
`Auto` follows the filled Material 3 default, and
`StyledOutlinedNumberPicker` fixes the input style to `Outlined`.

Use `StyledOptionalNumberPicker` when an unset value is meaningful. It accepts
`T?`; `null` shows the supplied placeholder (or an empty field) and is not
passed to the validator, so it does not produce a validation error. Clearing
the picker with its clear action or by deleting the input emits `null`. The
optional picker has the same outlined convenience as
`StyledOutlinedOptionalNumberPicker`.

The optional picker uses `parser.zero` as its default `initialValue`: from an
unset state, `+` sets that value and `−` sets one parser step below it. Override
`initialValue` when zero is not the desired starting point. Parsers created by
`rememberNumberParser` provide a typed zero automatically. When constructing a
`NumberParser` directly, provide its `zero` value or pass `initialValue`
explicitly.

Set `allowManualInput` to `false` to make the value field read-only while keeping
the increment and decrement buttons in their normal enabled state. `enabled`
controls the whole picker, including its visual enabled state and buttons.

Supported numeric types are `Int`, `Long`, `Float` and `Double`. Byte, Short and
custom Number subclasses are rejected with `IllegalArgumentException`, including
when a custom parser is supplied. `rememberNumberParser` requires `sample` and
`stepSize` to use the same supported type. `NumberParser`, `NumberValidator` and
their remember factories remain public; the `NumberUtil` implementation helper
is internal. The current text draft, including an incomplete or invalid numeric
entry, is saved across supported host recreation. The controlled numeric value
remains caller-owned.

```kotlin
val value = remember { mutableStateOf(0) }
val parser = rememberNumberParser(sample = 0, stepSize = 1)
val validator = rememberNumberValidator(value.value) { candidate ->
    if (candidate == null || candidate !in 0..100) {
        ValidationResult.Invalid("Enter a value between 0 and 100")
    } else {
        ValidationResult.Valid
    }
}

StyledNumberPicker(value, validator, parser)
```

```kotlin
val optionalValue = remember { mutableStateOf<Int?>(null) }
val parser = rememberNumberParser(sample = 0, stepSize = 1)
val validator = rememberValidator<Int>(optionalValue.value ?: 0) { candidate ->
    if (candidate != null && candidate !in 0..100) {
        ValidationResult.Invalid("Enter a value between 0 and 100")
    } else {
        ValidationResult.Valid
    }
}

StyledOptionalNumberPicker(
    value = optionalValue,
    validator = validator,
    parser = parser,
    placeholder = { StyledText("Not defined") },
)
```
