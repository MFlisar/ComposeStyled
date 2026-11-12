package com.michaelflisar.demo.demos.recipes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.demo.demos.recipes.examples.DemoPageTemplate
import com.michaelflisar.demo.demos.recipes.examples.DemoRecipeSelection
import com.michaelflisar.demo.helper.DemoTopBar
import com.michaelflisar.demo.recipes.ScaffoldStarterTemplate

private data class Recipe(
    val name: String,
    val description: String,
    val content: @Composable (onBack: () -> Unit) -> Unit,
)

private object DemoPageRecipes {

    val recipes = listOf(
        Recipe(
            name = "Selection",
            description = "A themed scaffold with examples for each slot.",
            content = ::DemoRecipeSelection
        ),
        Recipe(
            name = "Template App",
            description = "A full example app template",
            content = ::DemoPageTemplate
        ),
        Recipe(
            name = "Template Scaffold",
            description = "A themed scaffold with examples for each slot.",
            content = ::ScaffoldStarterTemplate
        )
    )
}

@Composable
internal fun DemoPageRecipes(
    onBack: () -> Unit,
) {
    val selectedRecipe = rememberSaveable { mutableIntStateOf(-1) }
    val onBack = {
        if (selectedRecipe.value == -1) {
            onBack()
        } else selectedRecipe.value = -1
    }

    when (selectedRecipe.value) {
        -1 -> {
            val scaffoldColor = StyledTheme.colorScheme.surface
            val topBarColor = StyledTheme.colorScheme.primary

            SystemBarStyleEffectIfNeeded(
                statusBarColor = topBarColor,
                navigationBarColor = scaffoldColor,
                backgroundColor = scaffoldColor,
            )

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
                    DemoPageRecipes.recipes.forEachIndexed { index, recipe ->

                        StyledListItem(
                            headline = { StyledText(recipe.name) },
                            supporting = { StyledText(recipe.description) },
                            onClick = { selectedRecipe.value = index },
                            //leading = { StyledIcon(StyledIcons.Submenu) },
                            shape = RoundedCornerShape(size = 8.dp),
                            containerColor = StyledTheme.colorScheme.primarySurface,
                            contentColor = StyledTheme.colorScheme.onPrimarySurface,
                        )
                    }
                }
            }
        }

        else -> {
            val content = DemoPageRecipes.recipes[selectedRecipe.value]
            content.content.invoke(onBack)
        }
    }
}
