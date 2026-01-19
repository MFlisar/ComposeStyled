package com.michaelflisar.composestyled.demo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.components.menu.MenuItem
import com.michaelflisar.composestyled.components.menu.MenuSubMenu
import com.michaelflisar.composestyled.components.menu.PopupMenu
import com.michaelflisar.composestyled.components.menu.rememberMenuState
import com.michaelflisar.composestyled.components.scaffold.FabPosition
import com.michaelflisar.composestyled.components.scaffold.StyledBottomBar
import com.michaelflisar.composestyled.components.scaffold.StyledFloatingActionButton
import com.michaelflisar.composestyled.components.scaffold.StyledNavigationItem
import com.michaelflisar.composestyled.components.scaffold.StyledRail
import com.michaelflisar.composestyled.components.scaffold.StyledScaffold
import com.michaelflisar.composestyled.components.scaffold.StyledSnackbarHost
import com.michaelflisar.composestyled.components.scaffold.StyledSnackbarHostState
import com.michaelflisar.composestyled.components.scaffold.StyledTopAppBarExpandable
import com.michaelflisar.composestyled.components.scaffold.styledExitUntilCollapsedScrollBehavior
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.ThemeMode
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.demo.screens.ButtonsScreen
import com.michaelflisar.composestyled.demo.screens.CardsScreen
import com.michaelflisar.composestyled.demo.screens.GeneralScreen
import com.michaelflisar.composestyled.demo.screens.InputsScreen
import com.michaelflisar.composestyled.demo.screens.ScreenBanners
import com.michaelflisar.composestyled.demo.screens.ScreenMenu
import kotlinx.coroutines.launch

private enum class DemoScreen {
    General,
    Buttons,
    Cards,
    Inputs,
    Snackbars,
    Scaffold,
    Settings,
    Banners,
    Menu
}

@Composable
fun DemoApp(
    platform: String,
    appName: String,
) {
    val themes = DemoThemes.themes

    val isDarkMode = isSystemInDarkTheme()
    val mode = rememberSaveable { mutableStateOf(ThemeMode.Light) }
    val themeIndex = rememberSaveable { mutableIntStateOf(0) }
    val theme = remember(themeIndex.value) {
        derivedStateOf { themes[themeIndex.value] }
    }

    // generate theme variables
    val colors = remember(mode.value, isDarkMode) {
        DemoThemes.selectTheme(mode.value, isDarkMode)
    }

    val screen = remember { mutableStateOf(DemoScreen.entries.first()) }

    val navigationItems = { icons: Boolean ->
        DemoScreen.entries.map {
            StyledNavigationItem(
                selected = screen.value == it,
                onClick = { screen.value = it },
                icon = if (icons) {
                    { StyledIcon(Icons.Default.Info, null) }
                } else null,
                label = { StyledText(it.name, maxLines = 1) },
            )
        }
    }

    BoxWithConstraints {

        val showRail = remember(maxWidth, maxHeight) { maxWidth > maxHeight }

        // apply theme
        StyledTheme(
            // theme definition
            theme = theme.value.theme,
            // colors
            colors = colors,
            // extensions (optional)
            renderers = theme.value.extensions
        ) {
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { StyledSnackbarHostState() }

            val scrollBehavior = styledExitUntilCollapsedScrollBehavior()

            StyledScaffold(
                modifier = Modifier.fillMaxSize(),
                scrollBehavior = scrollBehavior,
                topBar = { collapsedFraction ->
                    StyledTopAppBarExpandable(
                        containerColor = StyledTheme.colors.primarySurface,
                        contentColor = StyledTheme.colors.onPrimarySurface,
                        collapsedFraction = collapsedFraction,
                        title = appName,
                        navigationIcon = {
                            StyledButton(
                                variant = StyledButton.Variant.Text,
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Navigation clicked!",
                                            clearQueue = true
                                        )
                                    }
                                }
                            ) {
                                StyledIcon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Info"
                                )
                            }
                        },
                        actions = {
                            val menu = rememberMenuState()
                            StyledButton(
                                variant = StyledButton.Variant.Text,
                                onClick = { menu.show() }
                            ) {
                                StyledIcon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More"
                                )
                                PopupMenu(
                                    state = menu
                                ) {
                                    MenuSubMenu(
                                        text = { StyledText("Theme (${mode.value.name})") },
                                    ) {
                                        ThemeMode.entries.forEach { theme ->
                                            MenuItem(
                                                text = { StyledText(theme.name) },
                                                onClick = { mode.value = theme }
                                            )
                                        }
                                    }
                                    MenuSubMenu(
                                        text = { StyledText("Theme Style (${themes[themeIndex.value].name})") },
                                    ) {
                                        themes.forEachIndexed { index, s ->
                                            MenuItem(
                                                text = { StyledText(s.name) },
                                                onClick = { themeIndex.value = index }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                },
                rail = {
                    if (showRail) {
                        StyledRail(
                            items = navigationItems(false),
                            footer = { FAB(snackbarHostState, true) },
                            header = {
                                StyledText(
                                    platform,
                                    style = StyledTheme.typography.titleMedium
                                )
                            }
                        )
                    }
                },
                bottomBar = {
                    if (!showRail) {
                        StyledBottomBar(
                            items = navigationItems(true),
                            containerColor = StyledTheme.colors.primarySurface,
                            contentColor = StyledTheme.colors.onPrimarySurface,
                        )
                    }
                },
                fabPosition = FabPosition(),
                floatingActionButton = {
                    if (!showRail) {
                        FAB(snackbarHostState, false)
                    }
                },
                snackbarHost = { StyledSnackbarHost(snackbarHostState) }
            ) { paddingValues ->
                DemoPagePane(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues),
                    screen = screen
                )
            }
        }
    }
}

@Composable
private fun FAB(
    snackbarHostState: StyledSnackbarHostState,
    inRail: Boolean,
) {
    val scope = rememberCoroutineScope()
    val counter = remember { mutableIntStateOf(0) }
    StyledFloatingActionButton(
        imageVector = Icons.Default.Add,
        text = "Test".takeIf { inRail },
        fillWidth = inRail,
        onClick = {
            scope.launch {
                val x = counter.intValue++
                val result = snackbarHostState.showSnackbar(
                    message = "Hello from the Snackbar ($x)!",
                    actionLabel = "Dismiss",
                    clearQueue = true
                )
                println("Snackbar result: $result")
            }
        }
    )
}

@Composable
private fun DemoPagePane(
    modifier: Modifier,
    screen: MutableState<DemoScreen>,
) {
    Box(
        modifier = modifier
            .padding(all = StyledTheme.paddings.content),
    ) {
        when (screen.value) {
            DemoScreen.General -> GeneralScreen()
            DemoScreen.Buttons -> ButtonsScreen()
            DemoScreen.Cards -> CardsScreen()
            DemoScreen.Inputs -> InputsScreen()
            DemoScreen.Scaffold -> {
                StyledText(
                    "Scaffold content goes here",
                    style = StyledTheme.typography.bodyLarge
                )
            }

            DemoScreen.Snackbars -> {
                StyledText(
                    "Snackbars content goes here",
                    style = StyledTheme.typography.bodyLarge
                )
            }

            DemoScreen.Settings -> {
                StyledText(
                    "Settings content goes here",
                    style = StyledTheme.typography.bodyLarge
                )
            }

            DemoScreen.Banners -> ScreenBanners()
            DemoScreen.Menu -> ScreenMenu()
        }
    }
}
