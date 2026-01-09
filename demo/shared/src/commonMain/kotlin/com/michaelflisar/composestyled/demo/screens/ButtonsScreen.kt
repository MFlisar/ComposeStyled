package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.core.layout.ContentColumn
import com.michaelflisar.composestyled.core.layout.ContentRow
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonContent
import com.michaelflisar.composestyled.core.components.StyledText

private val ButtonVariants = mapOf(
    StyledButton.Variant.FilledPrimary to "Filled Primary",
    StyledButton.Variant.Outlined to "Outlined",
    StyledButton.Variant.Text to "Text"
)

@Composable
fun ButtonsScreen() {
    ContentColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Demo 1 - Buttons (core)
        StyledText("Buttons (Core)", style = StyledTheme.typography.titleMedium)
        CoreButtons()

        // Demo 2 - Buttons (components)
        StyledText("Buttons (Components)", style = StyledTheme.typography.titleMedium)
        ComponentButtons()
    }
}

@Composable
private fun CoreButtons() {
    ContentRow {
        ButtonVariants.forEach { (variant, name) ->
            StyledButton(
                onClick = { },
                variant = variant
            ) { StyledText(name) }
        }
    }
}

@Composable
private fun ComponentButtons() {
    ContentRow {
        ButtonVariants.forEach { (variant, name) ->
            StyledButton(
                variant = variant,
                onClick = { }
            ) {
                StyledButtonContent(
                    icon = Icons.Default.Home,
                    text = name
                )
            }
        }
    }
}