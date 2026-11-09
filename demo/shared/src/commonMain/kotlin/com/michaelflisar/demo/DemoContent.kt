package com.michaelflisar.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.PlatformDefaults
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.platform.currentPlatform
import com.michaelflisar.composestyled.themes.StyledThemeAppearance
import com.michaelflisar.composestyled.themes.StyledThemeContrast
import com.michaelflisar.composestyled.themes.StyledThemeVariant
import com.michaelflisar.composestyled.themes.rememberThemeColors
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledGrid
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollContainer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.composetable.styled.StyledTableUIProvider
import com.michaelflisar.demo.custom.StyledCustomComponent
import com.michaelflisar.demo.helper.DemoTopBar
import com.michaelflisar.demo.helper.SubTitle
import kotlin.math.ceil

fun init() {

    // in styled we need to init libraries
    StyledTableUIProvider.install()
}


@Composable
fun DemoContent(
    menuBar: @Composable (() -> Unit)? = null,
) {
    LaunchedEffect(Unit) {
        init()
    }

    // 1) Host platform
    val hostPlatform = currentPlatform()
    val selectedPlatformIndex =
        rememberSaveable { mutableStateOf(Platform.entries.indexOf(hostPlatform).coerceAtLeast(0)) }
    val selectedPlatform = Platform.entries[selectedPlatformIndex.value]

    // 2) initial implementation index based on host platform
    val initialImplementationIndex = when (hostPlatform.os) {
        Platform.OS.Android -> 1 // Material 3
        Platform.OS.IOS -> 2 // Cupertino Flat
        else -> 0 // Fluent 2
    }
    val selectedImplementationIndex =
        rememberSaveable { mutableStateOf(initialImplementationIndex) }
    val selectedThemeProvider =
        Definitions.ImplementationCollections[selectedImplementationIndex.value].second()

    // 3) theme settings
    val themeColors = rememberThemeColors(selectedThemeProvider, Definitions.ThemeCollections)
    val themeVariant = rememberSaveable { mutableStateOf(StyledThemeVariant.Colorful) }
    val themeAppearance = rememberSaveable { mutableStateOf(StyledThemeAppearance.Light) }
    val themeContrast = rememberSaveable { mutableStateOf(StyledThemeContrast.System) }

    val dialogSetup = rememberDialogState(initialVisible = true)
    val demoStateHolder = rememberSaveableStateHolder()
    val selectedDemo = rememberSaveable { mutableIntStateOf(-1) }
    val showSetupPanel = selectedDemo.value != 0

    StyledTheme(
        platform = selectedPlatform,
        themeProvider = selectedThemeProvider,
        themeColors = themeColors.value,
        themeVariant = themeVariant.value,
        themeAppearance = themeAppearance.value,
        themeContrast = themeContrast.value,
        behavior = StyledTheme.Behavior(
            showScrollbars = PlatformDefaults.showScrollbars(hostPlatform),
        ),
        customization = {

            // first register components, because the will also register their defaults

            // 1) we add a custom component
            val variant = when (selectedPlatform) {
                Platform.Android -> StyledCustomComponent.Variant.Primary
                Platform.IOS -> StyledCustomComponent.Variant.PrimarySurface
                Platform.Wasm -> StyledCustomComponent.Variant.SecondarySurface
                Platform.JvmWindows,
                Platform.JvmMacOS,
                Platform.JvmLinux,
                Platform.JvmUnknown,
                Platform.MacOS,
                    -> StyledCustomComponent.Variant.Secondary
            }
            register(StyledCustomComponent(variant))


            // 2) we replace a default implementation of a component (selectively for a platform)
            /*
                         if (platform == Platform.Android) {
                             // e.g. on android, this makes EVERY button everywhere an outlined button!
                             register(
                                 Material3Button.create(
                                     variantMapping = { variant, context -> Material3Button.Variant.Outlined }
                                 )
                             )
                         }

                         // 3) we customize the defaults of a component (selectively for a platform)
                         if (platform == Platform.JvmWindows) {
                             update(StyledButton.DefaultsKey) { copy(shape = RectangleShape) }
                         }
            */
        }
    ) {
        StyledSurface {
            StyledColumn {

                // 1) menu bar
                menuBar?.invoke()

                // 2) content
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    val pages = remember {
                        Definitions.createMainPages { selectedDemo.value = -1 }
                    }
                    val libraryPages = remember {
                        Definitions.createLibraryPages { selectedDemo.value = -1 }
                    }
                    demoStateHolder.SaveableStateProvider(selectedDemo.value) {
                        when (selectedDemo.value) {
                            -1 -> {
                                SelectDemo(
                                    pages = pages.map { it.first },
                                    libraryDemos = libraryPages.map { it.first },
                                    selectedDemo = selectedDemo
                                )
                            }

                            in pages.indices -> {
                                pages[selectedDemo.value].second.invoke()
                            }

                            else -> {
                                libraryPages.getOrNull(selectedDemo.value - pages.size)
                                    ?.second?.invoke()
                            }
                        }
                    }
                }
            }

            // 2) setup dialog
            StyledDialog(
                state = dialogSetup,
                properties = StyledDialog.Properties(
                    dismissOnBackPress = false, // not dismissable
                    dismissOnClickOutside = false, // not dismissable
                ),
                style = StyledDialog.Style.FloatingWindow(
                    initialSize = DpSize(640.dp, 420.dp),
                    initiallyMinimized = true,
                    minSize = DpSize(120.dp, 240.dp),
                    draggable = true,
                    resizable = true,
                    showMaximizeButton = true,
                    showCloseButton = false, // not dismissable, so no close button
                    icon = Icons.Default.Settings.toIconComposable(),
                    bubble = StyledDialog.BubbleConfig(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        borderColor = null,
                        shape = RoundedCornerShape(50),
                        shadowElevation = 20.dp,
                    ),
                    initialBubblePosition = StyledDialog.FloatingPosition(
                        alignment = Alignment.TopEnd
                    ),
                    backgroundMode = StyledDialog.FloatingWindowBackgroundMode.Minimize(
                        showScrim = true,
                    ),
                ),
                title = { StyledText("Setup") },
            ) { contentPadding ->
                StyledVerticalScrollContainer {
                    StyledColumn(
                        itemSpacing = StyledTheme.spacing.medium,
                        modifier = Modifier.padding(contentPadding),
                    ) {
                        if (showSetupPanel) {
                            DemoSetup(
                                platforms = Platform.entries.map { it.name },
                                implementations = Definitions.ImplementationCollections.map { it.first },
                                themeCollections = Definitions.ThemeCollections,
                                selectedPlatformIndex = selectedPlatformIndex,
                                selectedImplementationIndex = selectedImplementationIndex,
                                themeColors = themeColors,
                                themeVariant = themeVariant,
                                themeAppearance = themeAppearance,
                                themeContrast = themeContrast,
                            )
                        } else {
                            SubTitle("Template (not adjustable) - shows a easy start point for your own app")
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun SelectDemo(
    pages: List<String>,
    libraryDemos: List<String>,
    selectedDemo: MutableState<Int>,
) {
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
                title = "ComposeStyled",
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
            StyledText("Demos", style = StyledTheme.typography.titleLarge)

            val cols = 2
            val rowHeight = 64.dp

            val cellShape = RoundedCornerShape(size = 8.dp)

            val rows = ceil(pages.size.toFloat() / cols.toFloat()).toInt()
            StyledGrid(
                columns = cols,
                rows = rows,
                modifier = Modifier.fillMaxWidth()
                    .height(rowHeight * rows),
                spacing = StyledTheme.spacing.medium,
                contentPadding = PaddingValues(0.dp),
            ) { row, column ->
                val demoIndex = row * 2 + column
                if (demoIndex < pages.size) {
                    StyledButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { selectedDemo.value = demoIndex },
                        shape = cellShape,
                    ) {
                        StyledText(pages[demoIndex], textAlign = TextAlign.Center)
                    }
                }
            }

            StyledText("Libraries", style = StyledTheme.typography.titleLarge)

            val rows2 = ceil(libraryDemos.size.toFloat() / cols.toFloat()).toInt()
            StyledGrid(
                columns = cols,
                rows = rows2,
                modifier = Modifier.fillMaxWidth().height(rowHeight * rows2),
                spacing = StyledTheme.spacing.medium,
                contentPadding = PaddingValues(0.dp),
            ) { row, column ->
                val demoIndex = row * 2 + column
                if (demoIndex < libraryDemos.size) {
                    StyledButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { selectedDemo.value = pages.size + demoIndex },
                        shape = cellShape,
                    ) {
                        StyledText(libraryDemos[demoIndex])
                    }
                }
            }
        }
    }
}
