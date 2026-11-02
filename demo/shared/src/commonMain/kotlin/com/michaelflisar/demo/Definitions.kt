package com.michaelflisar.demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composestyled.cupertino.flat.rememberCupertinoFlatThemeProvider
import com.michaelflisar.composestyled.cupertino.liquid.rememberCupertinoLiquidThemeProvider
import com.michaelflisar.composestyled.fluent2.rememberFluent2ThemeProvider
import com.michaelflisar.composestyled.material3.rememberMaterial3ThemeProvider
import com.michaelflisar.composestyled.themes.StyledThemeColors
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.cupertino.CupertinoThemes
import com.michaelflisar.composestyled.themes.fluent2.Fluent2Themes
import com.michaelflisar.composestyled.themes.material3.Material3Themes
import com.michaelflisar.composestyled.themes.nature.NatureThemes
import com.michaelflisar.composestyled.themes.pastel.PastelThemes
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.pages.DemoPageInsets
import com.michaelflisar.demo.pages.DemoPageRecipes
import com.michaelflisar.demo.pages.DemoPageSelection
import com.michaelflisar.demo.pages.DemoPageTemplate
import com.michaelflisar.demo.pages.DemoPageUIShowCase

object Definitions {

    val ImplementationCollections = listOf(
        "Fluent 2" to @Composable { rememberFluent2ThemeProvider() },
        "Material 3" to @Composable { rememberMaterial3ThemeProvider() },
        "Cupertino Flat" to @Composable { rememberCupertinoFlatThemeProvider() },
        "Cupertino Liquid" to @Composable { rememberCupertinoLiquidThemeProvider() },
    )

    val ThemeCollections = listOf(
        Fluent2Themes.collection,
        Material3Themes.collection,
        CupertinoThemes.collection,
        PastelThemes.collection,
        NatureThemes.collection,
    )

    fun createMainPages(onBackPress: () -> Unit) = listOf(
        "Starter template" to @Composable { DemoPageTemplate(onBackPress) },
        "Recipes" to @Composable { DemoPageRecipes(onBackPress) },
        "Component showcase" to @Composable { DemoPageUIShowCase(onBackPress) },
        "Selection demo" to @Composable { DemoPageSelection(onBackPress) },
        "Insets demo" to @Composable { DemoPageInsets(onBackPress) },
    )

    // sort demos inside areas alphabetically to make it easier to find stuff
    fun createDemoAreas(
        expandedStates: List<ExpandState>,
        showMessage: (String) -> Unit,
        showSnackbarWithUndo: (String) -> Unit,
    ): List<DemoArea> {
        val areas =
            listOf(
                DemoArea(
                    name = "Components",
                    imageVector = Icons.Default.List,
                    expandState = expandedStates[0],
                    contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoBadges(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoButtons(
                                baseId,
                                expandState,
                                showMessage
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoCard(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoCheckboxes(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoChips(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoComponentParts(
                                baseId,
                                expandState,
                                { showSnackbarWithUndo("Changes saved") },
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoDateTimePickers(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoDropdowns(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoIcons(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoInputs(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoInteractionBlocker(
                                baseId,
                                expandState,
                                showMessage
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoListItems(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoNumberPicker(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoPicker(
                                baseId,
                                expandState,
                                showMessage
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoPopup(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoProgressIndicators(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoRadioButtons(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoSearchBar(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoSegmentedButtons(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoSliders(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoSurfaces(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoSwitches(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoTabs(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoTooltip(
                                baseId,
                                expandState
                            )
                        }
                    )
                ),
                DemoArea(
                    name = "Layout",
                    imageVector = Icons.Default.GridView,
                    expandState = expandedStates[1],
                    contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoDialogs(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoLayouts(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.ScaffoldExamples(
                                baseId,
                                expandState,
                                { showSnackbarWithUndo("Changes saved") })
                        }
                    )
                ),
                DemoArea(
                    name = "Primitives",
                    imageVector = Icons.Default.HorizontalRule,
                    expandState = expandedStates[2],
                    contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoDividers(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoPrimitives(
                                baseId,
                                expandState,
                                showMessage
                            )
                        }
                    )
                ),
                DemoArea(
                    name = "Specials",
                    imageVector = Icons.Default.Style,
                    expandState = expandedStates[3],
                    contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoSidePanes(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoExpandableSections(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoGroupBox(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoInformationRow(
                                baseId,
                                expandState
                            )
                        },
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoSwipeActions(
                                baseId,
                                expandState
                            )
                        },
                    )
                ),
                DemoArea(
                    name = "Custom",
                    imageVector = Icons.Default.Build,
                    expandState = expandedStates[4],
                    contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                        { baseId, expandState ->
                            com.michaelflisar.demo.demos.regions.DemoCustom(
                                baseId,
                                expandState
                            )
                        },
                    )
                ),
            )
        return areas
    }
}

@Stable
class DemoArea(
    val name: String,
    val imageVector: ImageVector,
    val expandState: ExpandState,
    val contents: List<@Composable (baseId: Int, expandState: ExpandState) -> Unit>,
)

internal fun demoThemeColorsSaver(
    defaultColors: StyledThemeColors,
    collections: List<StyledThemeColorsCollection>,
): Saver<MutableState<StyledThemeColors>, List<String>> {
    val colorsByKey = collections
        .flatMap { collection ->
            collection.themes.map { colors ->
                (collection.id to colors.id) to colors
            }
        }
        .toMap()
    return Saver(
        save = { state ->
            val key = demoThemeColorsKey(state.value, collections)
            listOf(key.first, key.second)
        },
        restore = { key ->
            val colors = key
                .takeIf { it.size == 2 }
                ?.let { colorsByKey[it[0] to it[1]] }
                ?: defaultColors
            mutableStateOf(colors)
        },
    )
}

internal fun demoThemeColorsKey(
    colors: StyledThemeColors,
    collections: List<StyledThemeColorsCollection>,
): Pair<String, String> {
    val collectionId = collections.firstOrNull { colors in it.themes }?.id.orEmpty()
    return collectionId to colors.id
}