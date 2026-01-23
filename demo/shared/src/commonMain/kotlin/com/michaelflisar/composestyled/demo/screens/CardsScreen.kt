package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.components.layout.ContentRow
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.demo.DemoThemes
import com.michaelflisar.composestyled.demo.composables.PreviewFamilyRegionColumn
import com.michaelflisar.composestyled.demo.composables.PreviewScheme

@Composable
fun CardsScreen() {
    PreviewScheme(
        title = "Cards",
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
}

@Composable
private fun ColumnScope.RenderVariants() {
    val variants = StyledCard.Variant.entries
    variants.forEach { variant ->
        StyledText(
            text = variant.name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = StyledTheme.typography.titleSmall
        )
        Emphasis.entries.forEach { emphasis ->
            ContentRow {
                StyledCard(
                    variant = variant,
                    emphasis = emphasis,
                    modifier = Modifier.weight(1f)
                ) {
                    StyledText(
                        text = emphasis.name,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                StyledCard(
                    variant = variant,
                    emphasis = emphasis,
                    modifier = Modifier.weight(1f),
                    onClick = { }
                ) {
                    StyledText(
                        text = emphasis.name + " (clickable)",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}