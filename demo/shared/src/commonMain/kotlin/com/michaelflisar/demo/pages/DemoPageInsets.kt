package com.michaelflisar.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.platform.SystemBarStyleEffect
import com.michaelflisar.composestyled.platform.WindowMode
import com.michaelflisar.composestyled.platform.rememberWindowEnvironment
import com.michaelflisar.composestyled.platform.systemBarIconStyleFor
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSwitch
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.layout.StyledScaffoldDefaults
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollbarContainer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlin.math.roundToInt

private val TopBarColor = Color(0xFF1565C0)
private val BottomBarColor = Color(0xFF6A1B9A)
private val SideNavigationColor = Color(0xFFEF6C00)
private val FabColor = Color(0xFF00838F)
private val ScaffoldPaddingColor = Color(0xFFC62828)
private val ContentColor = Color(0xFF2E7D32)
private val NestedInsetsColor = Color(0xFFF9A825)
private val NestedContentColor = Color(0xFFFFF8E1)

/**
 * Visualizes how the Scaffold distributes window insets:
 * colored slots show where each part draws, red marks the padding passed to the content,
 * green is the padded content and yellow is a nested `safeDrawing` padding.
 */
@Composable
internal fun DemoPageInsets(
    onBack: () -> Unit,
) {
    val showTopBar = rememberSaveable { mutableStateOf(true) }
    val showBottomBar = rememberSaveable { mutableStateOf(true) }
    val showSideNavigation = rememberSaveable { mutableStateOf(true) }
    val showFab = rememberSaveable { mutableStateOf(true) }
    val barInsets = rememberSaveable { mutableStateOf(true) }
    val scaffoldInsets = rememberSaveable { mutableStateOf(true) }
    val consumeInsets = rememberSaveable { mutableStateOf(true) }
    val nestedInsets = rememberSaveable { mutableStateOf(true) }

    val selectedDestination = rememberSaveable { mutableStateOf(0) }
    val sideNavigationExpanded = rememberSaveable { mutableStateOf(false) }
    val navigationItems = createInsetNavigationItems()

    val zeroInsets = WindowInsets(0, 0, 0, 0)
    val defaultContentInsets = StyledScaffoldDefaults.contentWindowInsets

    if (rememberWindowEnvironment().mode == WindowMode.Normal) {
        SystemBarStyleEffect(
            statusBar = systemBarIconStyleFor(
                if (showTopBar.value) TopBarColor else ScaffoldPaddingColor,
                ScaffoldPaddingColor,
            ),
            navigationBar = systemBarIconStyleFor(
                if (showBottomBar.value) BottomBarColor else ScaffoldPaddingColor,
                ScaffoldPaddingColor,
            ),
        )
    }

    StyledScaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ScaffoldPaddingColor,
        contentColor = Color.White,
        contentWindowInsets = if (scaffoldInsets.value) defaultContentInsets else zeroInsets,
        topBar = {
            if (showTopBar.value) {
                StyledTopBar(
                    containerColor = TopBarColor,
                    contentColor = Color.White,
                    windowInsets = if (barInsets.value) {
                        StyledTopBarInsets
                    } else {
                        zeroInsets
                    },
                    navigationIcon = {
                        StyledIconButton(onClick = onBack) {
                            StyledIcon(
                                icon = Icons.AutoMirrored.Filled.ArrowBack.toIconComposable(),
                                contentDescription = "Back to demo selector",
                            )
                        }
                    },
                    title = { StyledText("TopBar (blue)") },
                )
            }
        },
        bottomBar = {
            if (showBottomBar.value) {
                StyledBottomNavigationBar(
                    items = navigationItems,
                    selectedKey = selectedDestination.value,
                    onItemClick = { selectedDestination.value = it.key },
                    containerColor = BottomBarColor,
                    contentColor = Color.White,
                    windowInsets = if (barInsets.value) {
                        StyledBottomBarInsets
                    } else {
                        zeroInsets
                    },
                )
            }
        },
        sideNavigation = {
            if (showSideNavigation.value) {
                StyledSideNavigation(
                    items = navigationItems,
                    selectedKey = selectedDestination.value,
                    onItemClick = { selectedDestination.value = it.key },
                    mode = StyledSideNavigation.Mode.Expandable,
                    expanded = sideNavigationExpanded,
                    containerColor = SideNavigationColor,
                    contentColor = Color.Black,
                    title = { StyledText("Side (orange)") },
                    windowInsets = if (barInsets.value) {
                        StyledSideNavigationInsets
                    } else {
                        zeroInsets
                    },
                )
            }
        },
        floatingActionButton = {
            if (showFab.value) {
                StyledFloatingActionButton(
                    onClick = {},
                    containerColor = FabColor,
                    contentColor = Color.White,
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "FAB",
                    )
                }
            }
        },
    ) { paddingValues ->
        InsetsContent(
            paddingValues = paddingValues,
            consumeInsets = consumeInsets.value,
            nestedInsets = nestedInsets.value,
            options = listOf(
                "Show top bar" to showTopBar,
                "Show bottom bar" to showBottomBar,
                "Show side navigation" to showSideNavigation,
                "Show FAB" to showFab,
                "Bar windowInsets (top / bottom / side)" to barInsets,
                "Scaffold contentWindowInsets" to scaffoldInsets,
                "Content consumes paddingValues" to consumeInsets,
                "Nested safeDrawing padding (yellow)" to nestedInsets,
            ),
        )
    }
}

// -------------------------
// content
// -------------------------

@Composable
private fun InsetsContent(
    paddingValues: PaddingValues,
    consumeInsets: Boolean,
    nestedInsets: Boolean,
    options: List<Pair<String, MutableState<Boolean>>>,
) {
    // red: scaffold container, visible wherever paddingValues keeps the content away
    Box(modifier = Modifier.fillMaxSize()) {
        // green: content after applying paddingValues
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .then(if (consumeInsets) Modifier.consumeWindowInsets(paddingValues) else Modifier)
                .background(ContentColor)
                .padding(8.dp),
        ) {
            // yellow: a nested layout that applies safeDrawing again
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NestedInsetsColor)
                    .then(if (nestedInsets) Modifier.windowInsetsPadding(WindowInsets.safeDrawing) else Modifier)
                    .background(NestedContentColor),
            ) {
                StyledVerticalScrollbarContainer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(StyledTheme.padding.content),
                ) {
                    StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
                        InsetsLegend(paddingValues)
                        options.forEach { (label, state) ->
                            StyledRow(itemSpacing = StyledTheme.spacing.medium) {
                                StyledText(
                                    modifier = Modifier.weight(1f),
                                    text = label,
                                    color = Color.Black,
                                )
                                StyledSwitch(checked = state)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InsetsLegend(paddingValues: PaddingValues) {
    val safeDrawing = WindowInsets.safeDrawing.asPaddingValues()
    StyledText(
        "Insets demo",
        style = StyledTheme.typography.titleLarge,
        color = Color.Black,
    )
    StyledText(
        text = listOf(
            "Slots:",
            "• Top bar = blue",
            "• Bottom bar = purple",
            "• Side navigation = orange",
            "• FAB = teal",
            "",
            "Content:",
            "• Scaffold background visible through the content paddingValues = red",
            "• Content after padding(paddingValues) = green",
            "",
            "Nested:",
            "• Nested windowInsetsPadding(safeDrawing) = yellow",
            "  hidden when the content consumes paddingValues",
            "  doubles the system bar space when it does not",
        ).joinToString("\n"),
        color = Color.Black,
    )
    StyledText(
        text = "paddingValues: ${paddingValues.format()}",
        color = Color.Black,
    )
    StyledText(
        text = "safeDrawing (device): ${safeDrawing.format()}",
        color = Color.Black,
    )
}

@Composable
private fun PaddingValues.format(): String {
    val direction = LocalLayoutDirection.current
    return "start ${calculateStartPadding(direction).value.roundToInt()}, " +
        "top ${calculateTopPadding().value.roundToInt()}, " +
        "end ${calculateEndPadding(direction).value.roundToInt()}, " +
        "bottom ${calculateBottomPadding().value.roundToInt()} dp"
}

// -------------------------
// helpers
// -------------------------

private val StyledTopBarInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)

private val StyledBottomBarInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)

private val StyledSideNavigationInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start)

@Composable
private fun createInsetNavigationItems(): List<StyledNavigationItem<Int>> = remember {
    listOf(
        StyledNavigationItem(
            key = 0,
            label = { StyledText("Home") },
            icon = { StyledIcon(Icons.Default.Home.toIconComposable(), contentDescription = null) },
        ),
        StyledNavigationItem(
            key = 1,
            label = { StyledText("Settings") },
            icon = { StyledIcon(Icons.Default.Settings.toIconComposable(), contentDescription = null) },
        ),
    )
}
