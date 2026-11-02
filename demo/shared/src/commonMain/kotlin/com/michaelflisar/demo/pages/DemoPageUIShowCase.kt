package com.michaelflisar.demo.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveScaffold
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveScaffoldState
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.platform.SystemBarStyleEffect
import com.michaelflisar.composestyled.platform.WindowMode
import com.michaelflisar.composestyled.platform.rememberWindowEnvironment
import com.michaelflisar.composestyled.platform.systemBarIconStyleFor
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHost
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHostState
import com.michaelflisar.composestyled.ui.components.StyledSnackbarResult
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollbarContainer
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalSpacer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.rememberExpandState
import com.michaelflisar.composestyled.utils.rememberScrollToTopRequester
import com.michaelflisar.composestyled.utils.supportScrollToTopRequester
import com.michaelflisar.demo.Definitions
import com.michaelflisar.demo.DemoArea
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

@Composable
internal fun DemoPageUIShowCase(
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
        Definitions.createDemoAreas(
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
    val windowMode = rememberWindowEnvironment().mode
    val statusBarStyle = systemBarIconStyleFor(topBarColor, scaffoldColor)
    val bottomNavigationBarStyle = systemBarIconStyleFor(bottomNavigationColor, scaffoldColor)
    val scaffoldSystemBarStyle = systemBarIconStyleFor(scaffoldColor, scaffoldColor)

    when (navigationMode) {
        DemoNavigationMode.Adaptive -> {

            val adaptiveScaffoldState = remember { StyledAdaptiveScaffoldState() }
            val adaptiveNavigationMode = adaptiveScaffoldState.navigationMode
            if (windowMode == WindowMode.Normal && adaptiveNavigationMode != null) {
                SystemBarStyleEffect(
                    statusBar = statusBarStyle,
                    navigationBar = if (adaptiveNavigationMode.isBottom) {
                        bottomNavigationBarStyle
                    } else {
                        scaffoldSystemBarStyle
                    },
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
    stateHolder: androidx.compose.runtime.saveable.SaveableStateHolder,
) {
    stateHolder.SaveableStateProvider(areas[areaIndex].name) {
        val scrollState = rememberScrollState()
        val scrollToTopRequester = rememberScrollToTopRequester(
            scrollState = scrollState,
            topPadding = StyledTheme.padding.content.calculateTopPadding(),
        )
        StyledVerticalScrollbarContainer(
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
