package com.michaelflisar.demo.recipes

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.cupertino.flat.rememberCupertinoFlatThemeProvider
import com.michaelflisar.composestyled.fluent2.rememberFluent2ThemeProvider
import com.michaelflisar.composestyled.material3.rememberMaterial3ThemeProvider
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.PlatformDefaults
import com.michaelflisar.composestyled.platform.SystemBarStyleEffect
import com.michaelflisar.composestyled.platform.WindowMode
import com.michaelflisar.composestyled.platform.currentPlatform
import com.michaelflisar.composestyled.platform.rememberWindowEnvironment
import com.michaelflisar.composestyled.platform.systemBarIconStyleFor
import com.michaelflisar.composestyled.themes.StyledThemeAppearance
import com.michaelflisar.composestyled.themes.StyledThemeContrast
import com.michaelflisar.composestyled.themes.StyledThemeVariant
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBarItem
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHost
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHostState
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlinx.coroutines.launch

/**
 * base starter template - simply applies a theme and styles and places the content inside it
 */
@Composable
fun BaseStarterTemplate(
    content: @Composable () -> Unit,
) {
    // main setup - detect platform and select platform specific theme and color scheme
    // in real world you probably only add the fitting theme provider and select it directly...
    val hostPlatform = currentPlatform()
    val themeProvider = when (hostPlatform) {
        Platform.Android -> rememberMaterial3ThemeProvider()
        Platform.IOS -> rememberCupertinoFlatThemeProvider()
        Platform.Wasm,
        Platform.JvmWindows,
        Platform.JvmMacOS,
        Platform.JvmLinux,
        Platform.JvmUnknown,
        Platform.MacOS,
            -> rememberFluent2ThemeProvider()
    }

    // create a scaffold or a surface inside a StyledTheme() block and add your content inside it
    StyledTheme(
        platform = hostPlatform,
        themeProvider = themeProvider,
        themeColors = themeProvider.defaultThemeColors,
        themeVariant = StyledThemeVariant.Colorful,
        themeAppearance = StyledThemeAppearance.System,
        themeContrast = StyledThemeContrast.System,
        behavior = StyledTheme.Behavior(
            showScrollbars = PlatformDefaults.showScrollbars(hostPlatform),
        ),
        customization = {
            // apply customization like:
            // - replace implementations component by component (register(...))
            // - add additional styled components (register(...))
            // - update defaults of components (update(key, {}))
        },
        content = content
    )
}

/**
 * Provides short examples for each scaffold slot; apply the received padding to the content root.
 */
@Composable
fun ScaffoldStarterTemplate(
    onBack: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit = { paddingValues ->
        StyledText(
            text = "Your content goes here",
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        )
    },
) {
    val selectedDestination = remember { mutableStateOf(0) }
    val snackbarHostState = remember { StyledSnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    BaseStarterTemplate {

        val topAppBarColor = StyledTheme.colorScheme.surface
        val scaffoldColor = StyledTheme.colorScheme.surface
        val bottomNavColor = StyledTheme.colorScheme.surface

        // following takes care of system bar styling inkl. icon colors
        val windowEnvironment = rememberWindowEnvironment()
        if (windowEnvironment.mode == WindowMode.Normal) {
            SystemBarStyleEffect(
                statusBar = systemBarIconStyleFor(
                    color = topAppBarColor,
                    background = scaffoldColor,
                ),
                navigationBar = systemBarIconStyleFor(
                    color = bottomNavColor,
                    background = scaffoldColor,
                ),
            )
        }

        StyledScaffold(
            topBar = {
                StyledTopBar(
                    navigationIcon = {
                        if (onBack != null) {
                            StyledIconButton(onClick = onBack) {
                                StyledIcon(
                                    icon = Icons.AutoMirrored.Filled.ArrowBack.toIconComposable(),
                                    contentDescription = "Back to recipes",
                                )
                            }
                        }
                    },
                    title = { StyledText("Template Scaffold") },
                    containerColor = topAppBarColor
                )
            },
            bottomBar = {
                StyledBottomNavigationBar(
                    containerColor = bottomNavColor
                ) {
                    StyledBottomNavigationBarItem(
                        selected = selectedDestination.value == 0,
                        onClick = { selectedDestination.value = 0 },
                        icon = { StyledText("H") },
                        label = { StyledText("Home") },
                    )
                    StyledBottomNavigationBarItem(
                        selected = selectedDestination.value == 1,
                        onClick = { selectedDestination.value = 1 },
                        icon = { StyledText("S") },
                        label = { StyledText("Settings") },
                    )
                }
            },
            snackbarHost = {
                StyledSnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                StyledFloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Hello from the scaffold")
                        }
                    },
                ) {
                    StyledText("+")
                }
            },
            sideNavigation = {
                StyledSideNavigation(
                    title = { StyledText("Menu") },
                ) {
                    StyledSideNavigationItem(
                        selected = selectedDestination.value == 0,
                        onClick = { selectedDestination.value = 0 },
                        icon = { StyledText("H") },
                        label = { StyledText("Home") },
                    )
                    StyledSideNavigationItem(
                        selected = selectedDestination.value == 1,
                        onClick = { selectedDestination.value = 1 },
                        icon = { StyledText("S") },
                        label = { StyledText("Settings") },
                    )
                }
            },
            containerColor = scaffoldColor,
            content = content,
        )
    }
}