package com.michaelflisar.demo.demos.showcase

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveScaffold
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveScaffoldState
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHost
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHostState
import com.michaelflisar.composestyled.ui.components.StyledSnackbarResult
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollContainer
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalSpacer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.composestyled.utils.rememberExpandState
import com.michaelflisar.composestyled.utils.rememberScrollToTopRequester
import com.michaelflisar.composestyled.utils.supportScrollToTopRequester
import com.michaelflisar.demo.helper.DemoBottomNavigation
import com.michaelflisar.demo.helper.DemoSideNavigation
import com.michaelflisar.demo.helper.DemoTopBar
import com.michaelflisar.demo.helper.LocalShowcaseScrollToTopRequester
import kotlinx.coroutines.launch


private enum class DemoNavigationMode(val label: String) {
    BottomNavigation("Bottom navigation"),
    SideNavigation("Side navigation"),
    Adaptive("Adaptive"),
}

@Stable
class DemoArea(
    val name: String,
    val imageVector: ImageVector,
    val expandState: ExpandState,
    val contents: List<@Composable (baseId: Int, expandState: ExpandState) -> Unit>,
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
                        com.michaelflisar.demo.demos.showcase.areas.DemoBadges(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoButtons(
                            baseId,
                            expandState,
                            showMessage
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoCard(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoCheckboxes(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoChips(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoComponentParts(
                            baseId,
                            expandState,
                            { showSnackbarWithUndo("Changes saved") },
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoDateTimePickers(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoDropdowns(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoIcons(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoInputs(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoInteractionBlocker(
                            baseId,
                            expandState,
                            showMessage
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoListItems(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoNumberPicker(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoPicker(
                            baseId,
                            expandState,
                            showMessage
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoPopup(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoProgressIndicators(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoRadioButtons(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoSearchBar(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoSegmentedButtons(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoSliders(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoSurfaces(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoSwitches(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoTabs(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoTooltip(
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
                        com.michaelflisar.demo.demos.showcase.areas.DemoDialogs(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoLayouts(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.ScaffoldExamples(
                            baseId,
                            expandState,
                            { showSnackbarWithUndo("Changes saved") })
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoSheet(
                            baseId,
                            expandState
                        )
                    }
                )
            ),
            DemoArea(
                name = "Primitives",
                imageVector = Icons.Default.HorizontalRule,
                expandState = expandedStates[2],
                contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoDividers(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoPrimitives(
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
                        com.michaelflisar.demo.demos.showcase.areas.DemoSidePanes(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoExpandableSections(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoGroupBox(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoInformationRow(
                            baseId,
                            expandState
                        )
                    },
                    { baseId, expandState ->
                        com.michaelflisar.demo.demos.showcase.areas.DemoSwipeActions(
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
                        com.michaelflisar.demo.demos.showcase.areas.DemoCustom(
                            baseId,
                            expandState
                        )
                    },
                )
            ),
        )
    return areas
}

@Composable
internal fun DemoPageShowCase(
    onBack: () -> Unit,
) {
    val selectedDestination = rememberSaveable { mutableStateOf(0) }
    val selectedNavigationMode = rememberSaveable { mutableStateOf(0) }
    val expandSingleTestOnly = rememberSaveable { mutableStateOf(true) }

    val contentStateHolder = rememberSaveableStateHolder()

    val expandedStates = List(5) {
        rememberExpandState(
            singleOnly = expandSingleTestOnly.value,
            initialExpandedIds = setOf(0)
        )
    }

    val scope = rememberCoroutineScope()
    val snackbarState = remember { StyledSnackbarHostState() }
    val showMessage: (String) -> Unit = { message ->
        scope.launch {
            snackbarState.currentSnackbarData?.dismiss()
            snackbarState.showSnackbar(message)
        }
    }
    val showSnackbarWithUndo: (String) -> Unit = { message ->
        scope.launch {
            snackbarState.currentSnackbarData?.dismiss()
            val result = snackbarState.showSnackbar(
                message = message,
                actionLabel = "Undo",
                withDismissAction = true,
            )
            if (result == StyledSnackbarResult.ActionPerformed) {
                snackbarState.showSnackbar("Undo clicked")
            }
        }
    }

    val areas = remember {
        createDemoAreas(
            expandedStates = expandedStates,
            showMessage = showMessage,
            showSnackbarWithUndo = showSnackbarWithUndo
        )
    }

    val navigationItems = remember {
        areas.mapIndexed { index, area ->
            StyledNavigationItem(
                key = index,
                label = { StyledText(area.name) },
                icon = {
                    StyledIcon(
                        icon = area.imageVector.toIconComposable(),
                        contentDescription = null
                    )
                },
            )
        }
    }
    val navigationMode = DemoNavigationMode.entries[selectedNavigationMode.value]

    val scaffoldColor = StyledTheme.colorScheme.surface
    val topBarColor = StyledTheme.colorScheme.primary
    val bottomNavigationColor = StyledTheme.colorScheme.surfaceVariant

    when (navigationMode) {
        DemoNavigationMode.Adaptive -> {

            val adaptiveScaffoldState = remember { StyledAdaptiveScaffoldState() }
            val adaptiveNavigationMode = adaptiveScaffoldState.navigationMode
            if (adaptiveNavigationMode != null) {
                SystemBarStyleEffectIfNeeded(
                    statusBarColor = topBarColor,
                    navigationBarColor = if (adaptiveNavigationMode.isBottom) {
                        bottomNavigationColor
                    } else {
                        scaffoldColor
                    },
                    backgroundColor = scaffoldColor,
                )
            }
            StyledAdaptiveScaffold(
                platform = StyledTheme.platform,
                navigationItems = navigationItems,
                selectedKey = selectedDestination,
                state = adaptiveScaffoldState,
                modifier = Modifier.fillMaxSize(),
                containerColor = scaffoldColor,
                navigationContainerColor = bottomNavigationColor,
                navigationContentColor = StyledTheme.contentColorFor(
                    bottomNavigationColor
                ),
                topBar = { DemoTopBar(topBarColor, "Component showcase", onBack) },
                snackbarHost = { StyledSnackbarHost(snackbarState) },
                floatingActionButton = { FAB(showSnackbarWithUndo) },
                sideNavigationExpanded = remember { mutableStateOf(false) },
                collapseSideNavigationOnModeChange = true,
            ) { paddingValues, _ ->
                Content(
                    paddingValues = paddingValues,
                    areaIndex = selectedDestination.value,
                    areas = areas,
                    stateHolder = contentStateHolder,
                )
            }
        }

        DemoNavigationMode.BottomNavigation,
        DemoNavigationMode.SideNavigation,
            -> {
            SystemBarStyleEffectIfNeeded(
                statusBarColor = topBarColor,
                navigationBarColor = if (navigationMode == DemoNavigationMode.BottomNavigation) {
                    bottomNavigationColor
                } else {
                    scaffoldColor
                },
                backgroundColor = scaffoldColor,
            )

            StyledScaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = scaffoldColor,
                topBar = { DemoTopBar(topBarColor, "Component showcase", onBack) },
                bottomBar = {
                    if (navigationMode == DemoNavigationMode.BottomNavigation) {
                        DemoBottomNavigation(
                            navigationItems,
                            selectedDestination,
                            bottomNavigationColor,
                        )
                    }
                },
                sideNavigation = {
                    if (navigationMode == DemoNavigationMode.SideNavigation) {
                        DemoSideNavigation(
                            navigationItems,
                            selectedDestination,
                            StyledSideNavigation.Mode.Expandable,
                        )
                    }
                },
                snackbarHost = { StyledSnackbarHost(snackbarState) },
                floatingActionButton = { FAB(showSnackbarWithUndo) },
            ) { paddingValues ->
                Content(
                    paddingValues = paddingValues,
                    areaIndex = selectedDestination.value,
                    areas = areas,
                    stateHolder = contentStateHolder,
                )
            }
        }
    }
}

// -----------------
// main content areas
// -----------------

@Composable
private fun Content(
    paddingValues: PaddingValues,
    areaIndex: Int,
    areas: List<DemoArea>,
    stateHolder: SaveableStateHolder,
) {
    stateHolder.SaveableStateProvider(areas[areaIndex].name) {
        val scrollState = rememberScrollState()
        val scrollToTopRequester = rememberScrollToTopRequester(
            scrollState = scrollState,
            topPadding = StyledTheme.padding.content.calculateTopPadding(),
        )
        StyledVerticalScrollContainer(
            scrollState = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
                    end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
                )
                .supportScrollToTopRequester(scrollToTopRequester)
        ) {
            StyledHorizontalSpacer(size = StyledTheme.padding.content.calculateTopPadding())

            CompositionLocalProvider(LocalShowcaseScrollToTopRequester provides scrollToTopRequester) {
                StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
                    val area = remember(areas, areaIndex) { areas[areaIndex] }
                    area.contents.forEachIndexed { index, content ->
                        content(areaIndex * 10_000 + index * 100, area.expandState)
                    }
                }
            }

            StyledHorizontalSpacer(size = StyledTheme.padding.content.calculateBottomPadding())
        }
    }
}


@Composable
private fun FAB(showSnackbarWithUndo: (String) -> Unit) {
    StyledFloatingActionButton(
        onClick = { showSnackbarWithUndo("Show snackbar") }
    ) {
        StyledIcon(
            icon = Icons.Default.Add.toIconComposable(),
            contentDescription = "Show snackbar"
        )
    }
}
