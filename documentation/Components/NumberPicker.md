# Number picker

`StyledNumberPicker` is a compound control built from `StyledInput` and
`StyledIconButton` with an explicit Tertiary role, so it follows the active theme without a separate
renderer registration. Supply a `NumberParser` for parsing, formatting and step
changes, and a `NumberValidator` for input validation. It supports controlled
values, a `MutableState` overload, optional increment/decrement buttons and
wrapping between the values in `overrun`. Inputs use `StyledInput` for the default style and `StyledAlternativeInput` for
the alternative style. Number pickers provide matching
`StyledNumberPicker` and `StyledAlternativeNumberPicker` functions, each with
controlled and `MutableState` value overloads.

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
