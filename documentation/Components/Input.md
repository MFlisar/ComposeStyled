# Input

`StyledInput` exposes `Auto`, `Filled` and `Outlined` variants on all value
overloads. `Auto` is the default and maps to the filled Material 3 text field;
platform renderers adapt that style to their conventions. In a dropdown filter
context (`StyledInput.Context.DropdownFilter`), built-in renderers map `Auto` to
outlined instead. `Outlined` is always explicit and can be selected with
`variant = StyledInput.Variant.Outlined` or the fixed-style
`StyledOutlinedInput` convenience overloads. Renderers can map the variants
differently, and a theme can replace a component mapping through
`StyledTheme(customization = ...)`.
Nullable numeric inputs use `StyledOptionalInput(variant = ...)` or the fixed
`StyledOutlinedOptionalInput` overloads.

The input supports enabled/read-only modes, single-line or multiline input
(`singleLine = false`, `minLines`, `maxLines`), label, placeholder,
leading/trailing icons and supporting text. Shape defaults follow the
implementation; use `StyledInputDefaults.customize(CustomColors, border)` to
customize colors consistently across filled and outlined styles. Filled input
uses `filledBackground`/`filledForeground`; outlined input uses
`textForeground` and the optional border color.

Keyboard options/actions, visual transformation and interaction source are
forwarded to the underlying text field. Leading and trailing slots provide an
input-owned context so Styled buttons and icon buttons in those slots use the
button pointer cursor rather than inheriting the text input cursor.

`StyledInput` also accepts `TextFieldValue` (or `MutableState<TextFieldValue>`)
when the caller needs to control cursor position, selection or IME composition.
Custom input renderers receive and forward `TextFieldValue` in
`StyledInput.Implementation.Render`.

For passwords combine `PasswordVisualTransformation()` with
`KeyboardOptions(keyboardType = KeyboardType.Password)`. For errors set `isError`,
provide localized `errorMessage` for accessibility and render the message in
`supportingText` when desired. Error text is not inserted automatically.

```kotlin
var text by remember { mutableStateOf("") }

StyledInput(
    value = text,
    onValueChange = { text = it },
    label = { StyledText("Name") },
    placeholder = { StyledText("Enter your name") },
    supportingText = { StyledText("Shown on your profile") },
)
```

Input decoration slots are forwarded directly; their remembered state, focus
and implementation animations are not shared between renderers. The
internally managed cursor/selection value and numeric text draft are saved
across supported host recreation; controlled input text remains caller-owned.
Material 3 uses native controls; Cupertino and Fluent 2 share Foundation controls
with theme-specific defaults.

Numeric `StyledInput` and `StyledOptionalInput` overloads, including their
outlined convenience functions, support `Int`, `Long`, `Float` and `Double`.
Byte, Short and custom Number
subclasses are rejected with `IllegalArgumentException`, even when keyboard options
are supplied explicitly. Use a supported type for the value and, for optional
inputs, the sample.
