# ComposeStyled Material3 Adapter

Package namespace: `com.michaelflisar.composestyled.theme.material3`

## What this module does

- Provides `StyledMaterial3Theme(...)` which installs:
  - the core `StyledTheme`
  - a mapped `MaterialTheme` (Material3)
- Provides Material3-based Styled components in:
  - `com.michaelflisar.composestyled.theme.material3.components.*`

## Usage

```kotlin
StyledMaterial3Theme(mode = StyledThemeMode.System) {
    com.michaelflisar.composestyled.theme.material3.components.StyledButton(
        onClick = {}
    ) {
        com.michaelflisar.composestyled.theme.material3.components.StyledText("OK")
    }
}
```
