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
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonVariant
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.components.StyledInput
import com.michaelflisar.composestyled.core.components.StyledInputVariant
import com.michaelflisar.composestyled.core.components.StyledSurface
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.theme.StyledTheme
import com.michaelflisar.composestyled.core.theme.StyledThemeMode
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.core.tokens.darkStyledColors
import com.michaelflisar.composestyled.core.tokens.lightStyledColors
import com.michaelflisar.composestyled.theme.material3.StyledMaterial3Theme

@Composable
fun DemoApp(
    platform: String,
) {
    var mode by remember { mutableStateOf<StyledThemeMode>(StyledThemeMode.System) }

    // generate theme variables
    val isDarkMode = isSystemInDarkTheme()
    val colors = remember(mode, isDarkMode) {
        when (mode) {
            StyledThemeMode.Light -> lightStyledColors()
            StyledThemeMode.Dark -> darkStyledColors()
            StyledThemeMode.System -> if (isDarkMode) darkStyledColors() else lightStyledColors()
        }
    }
    val shapes = remember { StyledShapes() }
    val typography = remember { StyledTypography() }
    val paddings = remember { StyledPaddings() }
    val spacings = remember { StyledSpacings() }

    // apply theme
    StyledMaterial3Theme(
        colors = colors,
        shapes = shapes,
        typography = typography,
        paddings = paddings,
        spacings = spacings
    ) {
        Content(platform)
    }
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
                    StyledButton(onClick = { screen = it }) {
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
                variant = StyledButtonVariant.Filled
            ) { StyledText("Filled") }
            StyledButton(
                onClick = { },
                variant = StyledButtonVariant.Outlined
            ) { StyledText("Outlined") }
            StyledButton(onClick = { }, variant = StyledButtonVariant.Text) { StyledText("Text") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)) {
            StyledButton(
                onClick = { },
                enabled = false,
                variant = StyledButtonVariant.Filled
            ) { StyledText("Disabled") }
            StyledButton(
                onClick = { },
                enabled = false,
                variant = StyledButtonVariant.Outlined
            ) { StyledText("Disabled") }
            StyledButton(
                onClick = { },
                enabled = false,
                variant = StyledButtonVariant.Text
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
                        variant = StyledButtonVariant.Filled
                    ) { StyledText("Action") }
                    StyledButton(
                        onClick = { },
                        variant = StyledButtonVariant.Text
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
        StyledInput(
            value = text,
            onValueChange = { text = it },
            variant = StyledInputVariant.Filled,
            placeholder = "Filled"
        )
        StyledInput(
            value = text2,
            onValueChange = { text2 = it },
            variant = StyledInputVariant.Outlined,
            placeholder = "Outlined"
        )
        StyledInput(
            value = "Error text",
            onValueChange = { },
            isError = true,
            variant = StyledInputVariant.Outlined,
            placeholder = "Error"
        )
        StyledInput(
            value = roText,
            onValueChange = { roText = it },
            readOnly = true,
            variant = StyledInputVariant.Filled,
            placeholder = "Read-only"
        )
        StyledInput(
            value = "Disabled",
            onValueChange = { },
            enabled = false,
            variant = StyledInputVariant.Filled,
            placeholder = "Disabled"
        )
    }
}