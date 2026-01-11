package com.michaelflisar.composestyled.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.ThemeMode
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledCheckbox
import com.michaelflisar.composestyled.core.components.StyledHorizontalSeparator
import com.michaelflisar.composestyled.core.components.StyledSurface
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.components.StyledVerticalSeparator
import com.michaelflisar.composestyled.core.layout.ContentColumn
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.core.tokens.darkStyledColors
import com.michaelflisar.composestyled.core.tokens.lightStyledColors
import com.michaelflisar.composestyled.demo.composables.CheckedButton
import com.michaelflisar.composestyled.demo.screens.ButtonsScreen
import com.michaelflisar.composestyled.demo.screens.CardsScreen
import com.michaelflisar.composestyled.demo.screens.InputsScreen
import com.michaelflisar.composestyled.theme.cupertino.StyledComponentsCupertino
import com.michaelflisar.composestyled.theme.fluent2.StyledComponentsFluent2
import com.michaelflisar.composestyled.theme.material3.StyledComponentsMaterial3
import com.michaelflisar.composestyled.theme.wrapper.material3.StyledComponentsMaterial3Wrapper

private enum class DemoScreen {
    Buttons,
    Cards,
    Inputs
}

@Composable
fun DemoApp(
    platform: String,
) {
    val isDarkMode = isSystemInDarkTheme()
    val mode = remember { mutableStateOf<ThemeMode>(ThemeMode.System) }
    val styles = mapOf<String, StyledComponents>(
        "Material3" to StyledComponentsMaterial3,
        //"Cupertino" to StyledComponentsCupertino,
        //"Fluent2" to StyledComponentsFluent2,
        "Material3 (Wrapper)" to StyledComponentsMaterial3Wrapper
    )
    val style = remember { mutableStateOf(styles.entries.first().value) }

    // generate theme variables
    val colors = remember(mode.value, isDarkMode) {
        when (mode.value) {
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
        styledComponents = style.value,
        colors = colors,
        shapes = shapes,
        typography = typography,
        paddings = paddings,
        spacings = spacings
    ) {
        StyledSurface {
            DemoPagePane(
                platform = platform,
                mode = mode,
                style = style,
                styles = styles
            )
        }
    }
}

@Composable
fun DemoPagePane(
    platform: String,
    mode: MutableState<ThemeMode>,
    style: MutableState<StyledComponents>,
    styles: Map<String, StyledComponents>,
) {
    Row {

        Column(
            modifier = Modifier.width(200.dp).fillMaxHeight()
        ) {
            // 1) Platform Info
            ContentColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StyledText(
                    text = platform,
                    style = StyledTheme.typography.titleMedium,
                    color = StyledTheme.colors.primary
                )
            }

            // 2) Theme Mode
            StyledHorizontalSeparator()
            ContentColumn {
                StyledText(
                    text = "Theme Mode",
                    style = StyledTheme.typography.titleMedium
                )
                ThemeMode.entries.forEach { theme ->
                    CheckedButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = theme.name,
                        checked = mode.value == theme,
                        onClick = { mode.value = theme }
                    )
                }
            }

            // 3) Styles
            StyledHorizontalSeparator()
            ContentColumn {
                StyledText(
                    text = "Styles",
                    style = StyledTheme.typography.titleMedium
                )
                styles.forEach { s ->
                    CheckedButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = s.key,
                        checked = style.value == s.value,
                        onClick = { style.value = s.value }
                    )
                }
            }
        }
        StyledVerticalSeparator()
        ContentColumn {
            Content()
        }
    }
}

@Composable
private fun Content() {

    var screen by remember { mutableStateOf(DemoScreen.Buttons) }
    StyledSurface {
        Column(
            modifier = Modifier.padding(all = StyledTheme.paddings.content),
            verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
            ) {
                DemoScreen.entries.forEach {
                    StyledButton(
                        variant = StyledButton.Variants.FilledPrimary,
                        onClick = { screen = it }
                    ) {
                        StyledText(text = it.name)
                    }
                }
            }
            StyledText(
                text = "Demo Screen: ${screen.name}",
                style = StyledTheme.typography.titleMedium
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (screen) {
                    DemoScreen.Buttons -> ButtonsScreen()
                    DemoScreen.Cards -> CardsScreen()
                    DemoScreen.Inputs -> InputsScreen()
                }
            }
        }
    }
}

