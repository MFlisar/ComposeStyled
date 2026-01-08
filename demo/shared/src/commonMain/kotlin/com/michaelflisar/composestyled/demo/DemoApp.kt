package com.michaelflisar.composestyled.demo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.ThemeMode
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.components.StyledSurface
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.components.StyledTextField
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.core.tokens.darkStyledColors
import com.michaelflisar.composestyled.core.tokens.lightStyledColors
import com.michaelflisar.composestyled.theme.android.StyledComponentsAndroid

@Composable
fun DemoApp(
    platform: String,
) {
    val isDarkMode = isSystemInDarkTheme()
    var mode by remember { mutableStateOf<ThemeMode>(ThemeMode.System) }

    // generate theme variables
    val colors = remember(mode, isDarkMode) {
        when (mode) {
            ThemeMode.Light -> lightStyledColors()
            ThemeMode.Dark -> darkStyledColors()
            ThemeMode.System -> if (isDarkMode) darkStyledColors() else lightStyledColors()
        }
    }
    val shapes = remember { StyledShapes() }
    val typography = remember { StyledTypography() }
    val paddings = remember { StyledPaddings() }
    val spacings = remember { StyledSpacings() }

    // apply theme
    StyledTheme(
        styledComponents = StyledComponentsAndroid,
        colors = colors,
        shapes = shapes,
        typography = typography,
        paddings = paddings,
        spacings = spacings
    ) {
        Content(platform)
    }
    //StyledMaterial3Theme(
    //    colors = colors,
    //    shapes = shapes,
    //    typography = typography,
    //    paddings = paddings,
    //    spacings = spacings
    //) {
    //    Content(platform)
    //}
}

@Composable
private fun Content(platform: String) {

    var screen by remember { mutableStateOf(DemoScreen.Buttons) }
    StyledSurface {
        Column(
            modifier = Modifier.padding(all = StyledTheme.paddings.content),
            verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
        ) {
            StyledText(
                "ComposeStyled - Material3 Demo ($platform)",
                style = StyledTheme.typography.titleMedium
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
            ) {
                DemoScreen.entries.forEach {
                    StyledButton(
                        variant = StyledButton.Variant.FilledPrimary,
                        onClick = { screen = it }
                    ) {
                        StyledText(text = it.name)
                    }
                }
            }
            StyledText("Demo Screen: ${screen.name}", style = StyledTheme.typography.titleMedium)
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (screen) {
                    DemoScreen.Buttons -> ButtonsScreen()
                    DemoScreen.Cards -> CardsScreen()
                    DemoScreen.Inputs -> InputsScreen()
                }
            }
        }
    }
}

private enum class DemoScreen {
    Buttons,
    Cards,
    Inputs
}

@Composable
private fun ButtonsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
    ) {
        StyledText("Buttons")
        Row(horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)) {
            StyledButton(
                onClick = { },
                variant = StyledButton.Variant.FilledPrimary
            ) { StyledText("Filled") }
            StyledButton(
                onClick = { },
                variant = StyledButton.Variant.Outlined
            ) { StyledText("Outlined") }
            StyledButton(onClick = { }, variant = StyledButton.Variant.Text) { StyledText("Text") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)) {
            StyledButton(
                onClick = { },
                enabled = false,
                variant = StyledButton.Variant.FilledPrimary
            ) { StyledText("Disabled") }
            StyledButton(
                onClick = { },
                enabled = false,
                variant = StyledButton.Variant.Outlined
            ) { StyledText("Disabled") }
            StyledButton(
                onClick = { },
                enabled = false,
                variant = StyledButton.Variant.Text
            ) { StyledText("Disabled") }
        }
    }
}

@Composable
private fun CardsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
    ) {
        StyledText("Cards")
        StyledCard(
            modifier = Modifier.fillMaxWidth(),
            outlined = false
        ) {
            Column(
                modifier = Modifier
                    .padding(StyledTheme.paddings.medium),
                verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
            ) {
                StyledText("Filled Card")
                StyledText("This card uses StyledCard (Material3 adapter).")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
                ) {
                    StyledButton(
                        onClick = { },
                        variant = StyledButton.Variant.FilledPrimary
                    ) { StyledText("Action") }
                    StyledButton(
                        onClick = { },
                        variant = StyledButton.Variant.Text
                    ) { StyledText("Secondary") }
                }
            }
        }
        StyledCard(
            modifier = Modifier.fillMaxWidth(),
            outlined = true
        ) {
            Column(
                modifier = Modifier.padding(StyledTheme.paddings.medium),
                verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
            ) {
                StyledText("Outlined Card")
                StyledText("Outlined variant for low emphasis surfaces.")
            }
        }
    }
}

@Composable
private fun InputsScreen() {
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var roText by remember { mutableStateOf("Read-only value") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StyledText("Inputs")
        StyledTextField(
            value = text,
            onValueChange = { text = it },
            variant = StyledTextField.Variant.Filled,
            placeholder = { StyledText("Filled") }
        )
        StyledTextField(
            value = text2,
            onValueChange = { text2 = it },
            variant = StyledTextField.Variant.Outlined,
            placeholder = { StyledText("Outlined") }
        )
        StyledTextField(
            value = "Error text",
            onValueChange = { },
            isError = true,
            variant = StyledTextField.Variant.Outlined,
            placeholder = { StyledText("Error") }
        )
        StyledTextField(
            value = roText,
            onValueChange = { roText = it },
            readOnly = true,
            variant = StyledTextField.Variant.Filled,
            placeholder = { StyledText("Read-only") }
        )
        StyledTextField(
            value = "Disabled",
            onValueChange = { },
            enabled = false,
            variant = StyledTextField.Variant.Filled,
            placeholder = { StyledText("Disabled") }
        )
    }
}