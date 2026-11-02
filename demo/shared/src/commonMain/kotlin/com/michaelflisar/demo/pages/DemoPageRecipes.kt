package com.michaelflisar.demo.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.demo.helper.DemoTopBar
import com.michaelflisar.demo.recipes.ScaffoldStarterTemplate

@Composable
internal fun DemoPageRecipes(
    onBack: () -> Unit,
) {
    val showScaffoldRecipe = rememberSaveable { mutableStateOf(false) }
    if (showScaffoldRecipe.value) {
        ScaffoldStarterTemplate(
            onBack = { showScaffoldRecipe.value = false },
        )
    } else {
        val scaffoldColor = StyledTheme.colorScheme.surface
        val topBarColor = StyledTheme.colorScheme.primary

        StyledScaffold(
            containerColor = scaffoldColor,
            topBar = {
                DemoTopBar(
                    containerColor = topBarColor,
                    title = "Recipes",
                    onBack = onBack,
                )
            },
        ) { paddingValues ->
            StyledColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(StyledTheme.padding.content),
                itemSpacing = StyledTheme.spacing.medium,
            ) {
                StyledText("Available recipes", style = StyledTheme.typography.titleLarge)
                StyledListItem(
                    headline = { StyledText("ScaffoldStarterTemplate") },
                    supporting = { StyledText("A themed scaffold with examples for each slot.") },
                    onClick = { showScaffoldRecipe.value = true },
                )
            }
        }
    }
}
