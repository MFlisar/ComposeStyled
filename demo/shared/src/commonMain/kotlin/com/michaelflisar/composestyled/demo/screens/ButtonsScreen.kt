package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.components.layout.ContentRow
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Density
import com.michaelflisar.composestyled.core.classes.Size
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.demo.DemoThemes
import com.michaelflisar.composestyled.demo.composables.PreviewFamilyRegionColorStates
import com.michaelflisar.composestyled.demo.composables.PreviewFamilyRegionColumn
import com.michaelflisar.composestyled.demo.composables.PreviewScheme
import com.michaelflisar.composestyled.theme.cupertino.CupertinoUtil
import com.michaelflisar.composestyled.theme.fluent2.Fluent2Util
import com.michaelflisar.composestyled.theme.material3.Material3Util

@Composable
fun ButtonsScreen() {
    PreviewScheme(
        title = "Buttons",
        modifier = Modifier.fillMaxSize()
    ) {
        Content()
    }
}

@Composable
private fun Content() {

    PreviewFamilyRegionColumn(DemoThemes.themeM3) { RenderVariants() }
    PreviewFamilyRegionColumn(DemoThemes.themeCupertino) { RenderVariants() }
    PreviewFamilyRegionColumn(DemoThemes.themeFluent2) { RenderVariants() }

    PreviewFamilyRegionColorStates<StyledButton.Variant>(
        theme = DemoThemes.themeM3,
        resolve = { v -> Material3Util.resolveButtonColors(v) }
    )
    PreviewFamilyRegionColorStates<StyledButton.Variant>(
        theme = DemoThemes.themeCupertino,
        resolve = { v -> CupertinoUtil.resolveButtonColors(v) }
    )
    PreviewFamilyRegionColorStates<StyledButton.Variant>(
        theme = DemoThemes.themeFluent2,
        resolve = { v -> Fluent2Util.resolveButtonColors(v) }
    )
}

@Composable
private fun ColumnScope.RenderVariants() {
    val variants = StyledButton.Variant.entries
    StyledText(
        text = "Default",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = StyledTheme.typography.titleSmall
    )
    ContentRow {
        variants.forEach { variant ->
            StyledButton(
                onClick = {},
                variant = variant,
                modifier = Modifier.weight(1f)
            ) {
                StyledText(variant.name.first().toString())
            }
        }
    }
    StyledText(
        text = "Sizes",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = StyledTheme.typography.titleSmall
    )
    ContentRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Size.entries.forEach { size ->
            StyledButton(
                onClick = {},
                size = size,
                modifier = Modifier.weight(1f)
            ) {
                StyledText(size.name)
            }
        }
    }
    StyledText(
        text = "Densities",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = StyledTheme.typography.titleSmall
    )
    ContentRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Density.entries.forEach { density ->
            StyledButton(
                onClick = {},
                density = density,
                modifier = Modifier
            ) {
                StyledText(density.name)
            }
        }
    }
    StyledText(
        text = "Custom",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = StyledTheme.typography.titleSmall
    )
    StyledText(
        text = "(tint = Magenta, onTint = White)",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = StyledTheme.typography.titleSmall
    )
    ContentRow {
        variants.forEach { variant ->
            val customization = StyledButton.customize(
                tint = Color.Magenta,
                onTint = Color.White
            )
            StyledButton(
                onClick = {},
                variant = variant,
                modifier = Modifier.weight(1f),
                customization = customization
            ) {
                StyledText(variant.name.first().toString())
            }
        }
    }
}