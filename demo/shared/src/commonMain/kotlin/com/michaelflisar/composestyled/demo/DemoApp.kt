package com.michaelflisar.composestyled.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.StyledBanner
import com.michaelflisar.composestyled.components.layout.ContentColumn
import com.michaelflisar.composestyled.components.scaffold.FabPosition
import com.michaelflisar.composestyled.components.scaffold.StyledBottomBar
import com.michaelflisar.composestyled.components.scaffold.StyledFloatingActionButton
import com.michaelflisar.composestyled.components.scaffold.StyledNavigationItem
import com.michaelflisar.composestyled.components.scaffold.StyledRail
import com.michaelflisar.composestyled.components.scaffold.StyledScaffold
import com.michaelflisar.composestyled.components.scaffold.StyledSnackbarHost
import com.michaelflisar.composestyled.components.scaffold.StyledSnackbarHostState
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.ThemeMode
import com.michaelflisar.composestyled.core.components.StyledHorizontalSeparator
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.components.StyledVerticalSeparator
import com.michaelflisar.composestyled.core.renderer.ExtensionRenderer
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.tokens.darkStyledColors
import com.michaelflisar.composestyled.core.tokens.lightStyledColors
import com.michaelflisar.composestyled.demo.composables.CheckedButton
import com.michaelflisar.composestyled.demo.screens.ButtonsScreen
import com.michaelflisar.composestyled.demo.screens.CardsScreen
import com.michaelflisar.composestyled.demo.screens.InputsScreen
import com.michaelflisar.composestyled.demo.screens.ScreenBanners
import com.michaelflisar.composestyled.demo.screens.ScreenMenu
import com.michaelflisar.composestyled.theme.cupertino.StyleCupertino
import com.michaelflisar.composestyled.theme.fluent2.StyleFluent2
import com.michaelflisar.composestyled.theme.material3.StyleMaterial3
import com.michaelflisar.composestyled.theme.wrapper.material3.StyleMaterial3Wrapper
import kotlinx.coroutines.launch

private enum class DemoScreen {
    Buttons,
    Cards,
    Inputs,
    Snackbars,
    Scaffold,
    Settings,
    Banners,
    Menu
}

@Stable
class StyleWithExtensions(
    val name: String,
    val style: StyledComponents,
    val setup: StyledTheme.Setup,
    val extensions: List<ExtensionRenderer<*>>,
)

// custom style combining M3 with Cupertino buttons - just for demonstration
val CustomStyledComponents1 = StyledTokenComponents(
    surface = StyleMaterial3.components.surface,
    button = StyleCupertino.components.button,
    card = StyleMaterial3.components.card,
    checkbox = StyleMaterial3.components.checkbox,
    icon = StyleMaterial3.components.icon,
    separator = StyleMaterial3.components.separator,
    text = StyleMaterial3.components.text,
    textField = StyleMaterial3.components.textField,
)

// custom style combining M3 with Fluent2 buttons - just for demonstration
val CustomStyledComponents2 = StyledTokenComponents(
    surface = StyleMaterial3.components.surface,
    button = StyleFluent2.components.button,
    card = StyleMaterial3.components.card,
    checkbox = StyleMaterial3.components.checkbox,
    icon = StyleMaterial3.components.icon,
    separator = StyleMaterial3.components.separator,
    text = StyleMaterial3.components.text,
    textField = StyleMaterial3.components.textField,
)

// available styles with their specific extensions
val styles = listOf(
    StyleWithExtensions(
        name = "Material3",
        style = StyleMaterial3.components,
        setup = StyleMaterial3.setup,
        extensions = listOf(StyledBanner.Material3, StyledRail.Material3)
    ),
    StyleWithExtensions(
        name = "Cupertino",
        style = StyleCupertino.components,
        setup = StyleCupertino.setup,
        extensions = listOf(StyledBanner.Cupertino, StyledRail.Cupertino)
    ),
    StyleWithExtensions(
        name = "Fluent2",
        style = StyleFluent2.components,
        setup = StyleFluent2.setup,
        extensions = listOf(StyledBanner.Fluent2, StyledRail.Fluent2)
    ),
    StyleWithExtensions(
        name = "Material3 (Wrapper)",
        style = StyleMaterial3Wrapper.components,
        setup = StyleMaterial3Wrapper.setup,
        extensions = listOf(StyledBanner.Material3, StyledRail.Material3)
    ),
    StyleWithExtensions(
        name = "Custom (M3 + Cupertino Buttons)",
        style = CustomStyledComponents1,
        setup = StyleMaterial3.setup,
        extensions = listOf(StyledBanner.Cupertino, StyledRail.Cupertino)
    ),
    StyleWithExtensions(
        name = "Custom (M3 + Fluent2 Buttons)",
        style = CustomStyledComponents2,
        setup = StyleMaterial3.setup,
        extensions = listOf(StyledBanner.Fluent2, StyledRail.Fluent2)
    )
)

@Composable
fun DemoApp(
    platform: String,
) {
    val isDarkMode = isSystemInDarkTheme()
    val mode = remember { mutableStateOf(ThemeMode.System) }
    val style = remember { mutableStateOf(styles.first()) }

    // generate theme variables
    val colors = remember(mode.value, isDarkMode) {
        when (mode.value) {
            ThemeMode.Light -> lightStyledColors()
            ThemeMode.Dark -> darkStyledColors()
            ThemeMode.System -> if (isDarkMode) darkStyledColors() else lightStyledColors()
        }
    }

    val screen = remember { mutableStateOf(DemoScreen.Buttons) }

    val navigationItems = { icons: Boolean ->
        DemoScreen.entries.map {
            StyledNavigationItem(
                selected = screen.value == it,
                onClick = { screen.value = it },
                icon = if (icons) {
                    { StyledIcon(Icons.Default.Info, null) }
                } else null,
                label = { StyledText(it.name) },
            )
        }
    }

    BoxWithConstraints {

        val showRail = remember(maxWidth, maxHeight) { maxWidth > maxHeight }

        // apply theme
        StyledTheme(
            // styles components
            components = style.value.style,
            // styled locals
            colors = colors,
            setup = style.value.setup,
            // styled extensions (optional)
            extensionRenderers = style.value.extensions
        ) {
            val snackbarHostState = remember { StyledSnackbarHostState() }
            val scope = rememberCoroutineScope()
            val counter = remember { mutableStateOf(0) }
            StyledScaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(StyledTheme.colors.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        StyledText(
                            "Top Bar",
                            color = StyledTheme.colors.onPrimary,
                            style = StyledTheme.typography.titleMedium
                        )
                    }
                },
                rail = {
                    if (showRail) {
                        StyledRail(
                            items = navigationItems(false),
                            footer = { FAB(snackbarHostState, true) }
                        )
                    }
                },
                bottomBar = {
                    if (!showRail) {
                        StyledBottomBar(
                            items = navigationItems(true)
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
            ) {
                DemoPagePane(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHostState = snackbarHostState,
                    platform = platform,
                    mode = mode,
                    screen = screen,
                    style = style,
                    styles = styles
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
    snackbarHostState: StyledSnackbarHostState,
    platform: String,
    mode: MutableState<ThemeMode>,
    screen: MutableState<DemoScreen>,
    style: MutableState<StyleWithExtensions>,
    styles: List<StyleWithExtensions>,
) {
    val topRowHeight = 64.dp
    Row(modifier) {

        Column(
            modifier = Modifier.width(200.dp).fillMaxHeight()
        ) {
            // 1) Platform Info
            ContentColumn(
                modifier = Modifier.fillMaxWidth().height(topRowHeight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StyledText(
                    text = platform,
                    style = StyledTheme.typography.titleMedium,
                    color = StyledTheme.colors.primary
                )
            }

            // 2) Theme Mode
            StyledHorizontalSeparator()
            ContentColumn {
                StyledText(
                    text = "Theme Mode",
                    style = StyledTheme.typography.titleMedium
                )
                ThemeMode.entries.forEach { theme ->
                    CheckedButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = theme.name,
                        checked = mode.value == theme,
                        onClick = { mode.value = theme }
                    )
                }
            }

            // 3) Styles
            StyledHorizontalSeparator()
            ContentColumn {
                StyledText(
                    text = "Styles",
                    style = StyledTheme.typography.titleMedium
                )
                styles.forEach { s ->
                    CheckedButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = s.name,
                        checked = style.value == s,
                        onClick = { style.value = s }
                    )
                }
            }
        }
        StyledVerticalSeparator()
        Content(screen.value, Modifier.weight(1f).fillMaxHeight())
    }
}

@Composable
private fun Content(
    screen: DemoScreen,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .padding(all = StyledTheme.paddings.content),
    ) {
        when (screen) {
            DemoScreen.Buttons -> ButtonsScreen()
            DemoScreen.Cards -> CardsScreen()
            DemoScreen.Inputs -> InputsScreen()
            DemoScreen.Scaffold -> {
                StyledText("Scaffold content goes here", style = StyledTheme.typography.bodyLarge)
            }

            DemoScreen.Snackbars -> {
                StyledText("Snackbars content goes here", style = StyledTheme.typography.bodyLarge)
            }

            DemoScreen.Settings -> {
                StyledText("Settings content goes here", style = StyledTheme.typography.bodyLarge)
            }

            DemoScreen.Banners -> ScreenBanners()
            DemoScreen.Menu -> ScreenMenu()
        }
    }
}
