package com.michaelflisar.demo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.cupertino.rememberCupertinoTheme
import com.michaelflisar.composestyledcomponents.fluent2.rememberFluent2Theme
import com.michaelflisar.composestyledcomponents.material3.rememberMaterial3Theme
import com.michaelflisar.composestyledcomponents.platform.Platform
import com.michaelflisar.composestyledcomponents.tokens.autoStyledColors
import com.michaelflisar.composestyledcomponents.tokens.darkStyledColors
import com.michaelflisar.composestyledcomponents.tokens.lightStyledColors
import com.michaelflisar.composestyledcomponents.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyledcomponents.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationItem
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationRail
import com.michaelflisar.composestyledcomponents.ui.components.StyledScaffold
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarHost
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarHostState
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarResult
import com.michaelflisar.composestyledcomponents.ui.components.StyledSurface
import com.michaelflisar.composestyledcomponents.ui.components.StyledSwitch
import com.michaelflisar.composestyledcomponents.ui.components.StyledTopBar
import com.michaelflisar.composestyledcomponents.ui.layout.StyledRow
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedRow
import com.michaelflisar.composestyledcomponents.ui.layout.StyledVerticalScrollbarContainer
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledHorizontalSpacer
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledVerticalDivider
import com.michaelflisar.demo.demos.DemoButtons
import com.michaelflisar.demo.demos.DemoCard
import com.michaelflisar.demo.demos.DemoChips
import com.michaelflisar.demo.demos.DemoCheckboxes
import com.michaelflisar.demo.demos.DemoCustom
import com.michaelflisar.demo.demos.DemoDividers
import com.michaelflisar.demo.demos.DemoDropdowns
import com.michaelflisar.demo.demos.DemoExpandableSections
import com.michaelflisar.demo.demos.DemoGroupBox
import com.michaelflisar.demo.demos.DemoIcons
import com.michaelflisar.demo.demos.DemoInformationRow
import com.michaelflisar.demo.demos.DemoInputs
import com.michaelflisar.demo.demos.DemoInteractionBlocker
import com.michaelflisar.demo.demos.DemoLayouts
import com.michaelflisar.demo.demos.DemoNumberPicker
import com.michaelflisar.demo.demos.DemoPicker
import com.michaelflisar.demo.demos.DemoPopup
import com.michaelflisar.demo.demos.DemoPrimitives
import com.michaelflisar.demo.demos.DemoProgressIndicators
import com.michaelflisar.demo.demos.DemoRadioButtons
import com.michaelflisar.demo.demos.DemoSegmentedButtons
import com.michaelflisar.demo.demos.DemoSurfaces
import com.michaelflisar.demo.demos.DemoSwitches
import com.michaelflisar.demo.demos.DemoTabs
import com.michaelflisar.demo.demos.DemoTooltip
import com.michaelflisar.demo.demos.ScaffoldExamples
import com.michaelflisar.demo.helper.DropdownWithButtons
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.rememberExpandState
import kotlinx.coroutines.launch

@Stable
class DemoArea(
    val name: String,
    val imageVector: ImageVector,
    val expandState: ExpandState,
    val contents: List<@Composable (baseId: Int, expandState: ExpandState) -> Unit>,
)

@Composable
fun DemoContent() {

    val selectedPlatformIndex = remember { mutableStateOf(0) }
    val selectedThemeIndex = remember { mutableStateOf(0) }
    val selectedDestination = remember { mutableStateOf(0) }
    val useRail = remember { mutableStateOf(false) }
    val expandSingleTestOnly = remember { mutableStateOf(true) }

    val expandState1 = rememberExpandState(
        singleOnly = expandSingleTestOnly.value,
        initialExpandedIds = setOf(0)
    )
    val expandState2 = rememberExpandState(
        singleOnly = expandSingleTestOnly.value,
        initialExpandedIds = setOf(0)
    )
    val expandState3 = rememberExpandState(
        singleOnly = expandSingleTestOnly.value,
        initialExpandedIds = setOf(0)
    )
    val expandState4 = rememberExpandState(
        singleOnly = expandSingleTestOnly.value,
        initialExpandedIds = setOf(0)
    )
    val expandState5 = rememberExpandState(
        singleOnly = expandSingleTestOnly.value,
        initialExpandedIds = setOf(0)
    )
    val expandState6 = rememberExpandState(
        singleOnly = expandSingleTestOnly.value,
        initialExpandedIds = setOf(0)
    )

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

    LaunchedEffect(expandState1.expandedIds.toList()) {
        println("DemoContent: expandState1 = ${expandState1.expandedIds.toList()}")
    }
    // sort demos inside areas alphabetically to make it easier to find stuff

    val areas = remember {
        println("DemoContent: recomposing areas")
        listOf(
            DemoArea(
                name = "Components",
                imageVector = Icons.Default.List,
                expandState = expandState1,
                contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                    { baseId, expandState -> DemoButtons(baseId, expandState, showMessage) },
                    { baseId, expandState -> DemoCard(baseId, expandState) },
                    { baseId, expandState -> DemoCheckboxes(baseId, expandState) },
                    { baseId, expandState -> DemoChips(baseId, expandState) },
                    { baseId, expandState -> DemoDropdowns(baseId, expandState) },
                    { baseId, expandState -> DemoIcons(baseId, expandState) },
                    { baseId, expandState -> DemoInputs(baseId, expandState) },
                    { baseId, expandState ->
                        DemoInteractionBlocker(
                            baseId,
                            expandState,
                            showMessage
                        )
                    },
                    { baseId, expandState -> DemoNumberPicker(baseId, expandState) },
                    { baseId, expandState -> DemoPicker(baseId, expandState, showMessage) },
                    { baseId, expandState -> DemoPopup(baseId, expandState) },
                    { baseId, expandState -> DemoProgressIndicators(baseId, expandState) },
                    { baseId, expandState -> DemoRadioButtons(baseId, expandState) },
                    { baseId, expandState -> DemoSegmentedButtons(baseId, expandState) },
                    { baseId, expandState -> DemoSurfaces(baseId, expandState) },
                    { baseId, expandState -> DemoSwitches(baseId, expandState) },
                    { baseId, expandState -> DemoTabs(baseId, expandState) },
                    { baseId, expandState -> DemoTooltip(baseId, expandState) }
                )
            ),
            DemoArea(
                name = "Scaffold + Sub Elements",
                imageVector = Icons.Default.KeyboardDoubleArrowRight,
                expandState = expandState2,
                contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                    { baseId, expandState ->
                        ScaffoldExamples(
                            baseId,
                            expandState,
                            { showSnackbarWithUndo("Changes saved") })
                    }
                )
            ),
            DemoArea(
                name = "Layout",
                imageVector = Icons.Default.GridView,
                expandState = expandState3,
                contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                    { baseId, expandState -> DemoLayouts(baseId, expandState) }
                )
            ),
            DemoArea(
                name = "Primitives",
                imageVector = Icons.Default.HorizontalRule,
                expandState = expandState4,
                contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                    { baseId, expandState -> DemoDividers(baseId, expandState) },
                    { baseId, expandState -> DemoPrimitives(baseId, expandState, showMessage) }
                )
            ),
            DemoArea(
                name = "Specials",
                imageVector = Icons.Default.Style,
                expandState = expandState5,
                contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                    { baseId, expandState -> DemoExpandableSections(baseId, expandState) },
                    { baseId, expandState -> DemoGroupBox(baseId, expandState) },
                    { baseId, expandState -> DemoInformationRow(baseId, expandState) },
                )
            ),
            DemoArea(
                name = "Custom",
                imageVector = Icons.Default.Build,
                expandState = expandState6,
                contents = listOf<@Composable (baseId: Int, expandState: ExpandState) -> Unit>(
                    { baseId, expandState -> DemoCustom(baseId, expandState) },
                )
            ),
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

    StyledTheme(
        platform = Platform.ALL[selectedPlatformIndex.value],
        theme = when (Platform.ALL[selectedPlatformIndex.value]) {
            Platform.Android -> rememberMaterial3Theme(
                registerCustomComponents = {
                    register(StyledCustomComponent(StyledCustomComponent.Variant.Primary))
                },
            )

            is Platform.Desktop -> rememberFluent2Theme(
                registerCustomComponents = {
                    register(StyledCustomComponent(StyledCustomComponent.Variant.Secondary))
                },
            )

            Platform.IOS -> rememberCupertinoTheme(
                registerCustomComponents = {
                    register(StyledCustomComponent(StyledCustomComponent.Variant.PrimarySurface))
                },
            )

            Platform.Web -> rememberFluent2Theme(
                registerCustomComponents = {
                    register(StyledCustomComponent(StyledCustomComponent.Variant.SecondarySurface))
                },
            )
        },
        colors = when (selectedThemeIndex.value) {
            0 -> lightStyledColors()
            1 -> darkStyledColors()
            2 -> autoStyledColors()
            else -> throw RuntimeException()
        }
    ) {
        StyledSurface {
            StyledRow {

                Setup(
                    modifier = Modifier.width(200.dp).fillMaxHeight(),
                    selectedPlatformIndex = selectedPlatformIndex,
                    selectedThemeIndex = selectedThemeIndex,
                    useRail = useRail,
                    expandSingleTestOnly = expandSingleTestOnly
                )

                StyledVerticalDivider()

                StyledScaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar()
                    },
                    bottomBar = {
                        if (!useRail.value) {
                            BottomNavigation(navigationItems, selectedDestination)
                        }
                    },
                    navigationRail = {
                        if (useRail.value) {
                            Rail(navigationItems, selectedDestination)
                        }
                    },
                    snackbarHost = { StyledSnackbarHost(snackbarState) },
                    floatingActionButton = {
                        FAB(showSnackbarWithUndo)
                    },
                ) { paddingValues ->

                    Content(
                        paddingValues = paddingValues,
                        areaIndex = selectedDestination.value,
                        areas = areas
                    )
                }

            }
        }
    }
}

// -----------------
// main content areas
// -----------------

@Composable
private fun Setup(
    modifier: Modifier,
    selectedPlatformIndex: MutableState<Int>,
    selectedThemeIndex: MutableState<Int>,
    useRail: MutableState<Boolean>,
    expandSingleTestOnly: MutableState<Boolean>,
) {
    val themes = listOf("Light", "Dark", "Auto")
    val platforms = Platform.ALL.map { it.toString() }

    StyledVerticalScrollbarContainer(
        modifier = modifier
            .padding(StyledTheme.padding.content)
    ) {
        StyledSpacedColumn {
            SubTitle("Platform")
            DropdownWithButtons(
                items = platforms,
                selectedIndex = selectedPlatformIndex
            )

            SubTitle("Theme")
            DropdownWithButtons(
                items = themes,
                selectedIndex = selectedThemeIndex
            )

            StyledSpacedRow {
                StyledText(
                    modifier = Modifier.weight(1f),
                    text = "Navigation rail"
                )
                StyledSwitch(
                    checked = useRail
                )
            }

            StyledSpacedRow {
                StyledText(
                    modifier = Modifier.weight(1f),
                    text = "Expand single test only"
                )
                StyledSwitch(
                    checked = expandSingleTestOnly
                )
            }

            SubTitle("Theme Name")
            StyledText(
                text = "${platforms[selectedPlatformIndex.value]} => ${StyledTheme.themeName}"
            )
        }
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    areaIndex: Int,
    areas: List<DemoArea>,
) {
    StyledVerticalScrollbarContainer(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(
                start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
                end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
            )
    ) {
        StyledHorizontalSpacer(size = StyledTheme.padding.content.calculateTopPadding())

        StyledSpacedColumn {
            val area = remember(areas, areaIndex) { areas[areaIndex] }
            area.contents.forEachIndexed { index, content ->
                content(areaIndex * 10_000 + index * 100, area.expandState)
            }
        }

        StyledHorizontalSpacer(size = StyledTheme.padding.content.calculateBottomPadding())
    }
}


@Composable
private fun TopBar() {
    StyledTopBar(
        title = { StyledText("Styled Components") },
        containerColor = StyledTheme.colorScheme.primary,
        contentColor = StyledTheme.colorScheme.onPrimary
    )
}

@Composable
private fun BottomNavigation(
    navigationItems: List<StyledNavigationItem<Int>>,
    selectedDestination: MutableState<Int>,
) {
    StyledBottomNavigationBar(
        items = navigationItems,
        selectedKey = selectedDestination.value,
        onItemClick = { selectedDestination.value = it.key },
        containerColor = StyledTheme.colorScheme.primary,
        contentColor = StyledTheme.colorScheme.onPrimary
    )
}

@Composable
private fun Rail(
    navigationItems: List<StyledNavigationItem<Int>>,
    selectedDestination: MutableState<Int>,
) {
    StyledNavigationRail(
        items = navigationItems,
        selectedKey = selectedDestination.value,
        onItemClick = { selectedDestination.value = it.key },
        containerColor = StyledTheme.colorScheme.primary,
        contentColor = StyledTheme.colorScheme.onPrimary
    )
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