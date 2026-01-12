package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonContent
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.layout.ContentColumn
import com.michaelflisar.composestyled.core.layout.ContentFlowRow

@Composable
fun ButtonsScreen() {
    ContentColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Demo 1 - Buttons
        StyledText("Buttons", style = StyledTheme.typography.titleMedium)
        CoreButtons()

        // Demo 2 - Buttons + StyledButtonContent
        StyledText("Buttons + StyledButtonContent", style = StyledTheme.typography.titleMedium)
        ComponentButtons()

        // Demo 3 - Custom Buttons
        StyledText("Custom Buttons", style = StyledTheme.typography.titleMedium)
        CustomButtons()
    }
}

@Composable
private fun CoreButtons() {
    ContentFlowRow {
        StyledButton.Variant.entries.forEach { variant ->
            StyledButton(
                onClick = { },
                variant = variant
            ) { StyledText(variant.name) }
        }
    }
}

@Composable
private fun ComponentButtons() {
    ContentFlowRow {
        StyledButton.Variant.entries.forEach { variant ->
            StyledButton(
                variant = variant,
                onClick = { }
            ) {
                StyledButtonContent(
                    icon = Icons.Default.Home,
                    text = variant.name
                )
            }
        }
    }
}

@Composable
private fun CustomButtons() {
    ContentFlowRow {
        StyledButton(
            onClick = { },
            variant = StyledButton.Variant.Primary,
            customization = StyledButton.customize(
                background = StyledTheme.colors.error,
                content = StyledTheme.colors.onError
            )
        ) { StyledText("Primary (customization = error)") }
        StyledButton(
            onClick = { },
            variant = StyledButton.Variant.Primary,
            customization = StyledButton.customize(
                background = StyledTheme.colors.success,
                content = StyledTheme.colors.onSuccess
            )
        ) { StyledText("Primary (customization = success)") }
        StyledButton(
            onClick = { },
            variant = StyledButton.Variant.Primary,
            shape = RectangleShape
        ) { StyledText("Primary (shape = RectangleShape)") }
    }
}