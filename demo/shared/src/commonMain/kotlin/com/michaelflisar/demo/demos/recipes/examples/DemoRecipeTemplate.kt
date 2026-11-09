package com.michaelflisar.demo.demos.recipes.examples

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.adaptive.StyledAdaptive
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveInfo
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveContainer
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveMasterDetailMode
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveScaffold
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveScaffoldState
import com.michaelflisar.composestyled.classes.StyledNavigationMode
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.cupertino.flat.rememberCupertinoFlatThemeProvider
import com.michaelflisar.composestyled.fluent2.rememberFluent2ThemeProvider
import com.michaelflisar.composestyled.material3.rememberMaterial3ThemeProvider
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.PlatformDefaults
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.platform.currentPlatform
import com.michaelflisar.composestyled.themes.StyledThemeAppearance
import com.michaelflisar.composestyled.themes.StyledThemeColors
import com.michaelflisar.composestyled.themes.StyledThemeColorsCollection
import com.michaelflisar.composestyled.themes.StyledThemeContrast
import com.michaelflisar.composestyled.themes.StyledThemeVariant
import com.michaelflisar.composestyled.themes.rememberThemeColors
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledCard
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSwitch
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledMasterDetail
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.layout.StyledScaffoldDefaults
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollContainer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.specials.StyledInformationRow
import com.michaelflisar.composestyled.ui.specials.StyledThemeColorsPicker
import com.michaelflisar.composestyled.ui.state.StyledExpandableTopBarScrollBehavior
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.composestyled.ui.state.rememberStyledExpandableTopBarScrollBehavior
import com.michaelflisar.demo.Definitions
import com.michaelflisar.demo.helper.DemoBottomNavigation
import com.michaelflisar.demo.helper.DemoSideNavigation
import com.michaelflisar.demo.helper.DemoTopBar
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoPageTemplate(
    onBack: () -> Unit,
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

    // theme states
    val themeColors = rememberThemeColors(themeProvider, Definitions.ThemeCollections)
    val themeVariant = rememberSaveable { mutableStateOf(StyledThemeVariant.Colorful) }
    val themeAppearance = rememberSaveable { mutableStateOf(StyledThemeAppearance.System) }
    val themeContrast = rememberSaveable { mutableStateOf(StyledThemeContrast.System) }

    // available theme collections
    val themeColorsCollections =
        remember(hostPlatform) { Definitions.ThemeCollections.filter { it.fits(hostPlatform) } }

    // shared demo state
    val adaptive = rememberSaveable { mutableStateOf(false) }
    val showToolbar = rememberSaveable { mutableStateOf(true) }
    val expandableToolbar = rememberSaveable { mutableStateOf(false) }
    val toolbarScrollBehavior = rememberStyledExpandableTopBarScrollBehavior()
    LaunchedEffect(expandableToolbar.value) {
        if (!expandableToolbar.value) toolbarScrollBehavior.state.expand()
    }
    val primaryToolbar = rememberSaveable { mutableStateOf(true) }
    val translucentToolbar = rememberSaveable { mutableStateOf(false) }
    val showNavigation = rememberSaveable { mutableStateOf(true) }
    val primaryNavigation = rememberSaveable { mutableStateOf(false) }
    val translucentBottomNavigation = rememberSaveable { mutableStateOf(false) }
    val selectedDestination = rememberSaveable { mutableStateOf(0) }
    val sideNavigationExpanded = rememberSaveable { mutableStateOf(true) }
    val navigationItems = createNavigationItems()

    StyledTheme(
        platform = hostPlatform,
        themeProvider = themeProvider,
        themeColors = themeColors.value,
        themeVariant = themeVariant.value,
        themeAppearance = themeAppearance.value,
        themeContrast = themeContrast.value,
        behavior = StyledTheme.Behavior(
            showScrollbars = PlatformDefaults.showScrollbars(hostPlatform),
        )
    ) {
        val scaffoldColor = StyledTheme.colorScheme.surface
        val topBarColor = (if (primaryToolbar.value) {
            StyledTheme.colorScheme.primary
        } else {
            StyledTheme.colorScheme.surfaceVariant
        }).copy(alpha = if (translucentToolbar.value) 0.65f else 1f)
        val navigationColor = if (primaryNavigation.value) {
            StyledTheme.colorScheme.primary
        } else {
            StyledTheme.colorScheme.surfaceVariant
        }
        val bottomNavigationColor = navigationColor.copy(
            alpha = if (translucentBottomNavigation.value) 0.65f else 1f,
        )

        val content: @Composable (PaddingValues, State<StyledAdaptiveInfo>?) -> Unit =
            { paddingValues, adaptiveInfo ->
                TemplateContent(
                    paddingValues = paddingValues,
                    adaptiveInfo = adaptiveInfo,
                    selectedDestination = selectedDestination,
                    adaptive = adaptive,
                    themeColors = themeColors,
                    themeVariant = themeVariant,
                    themeAppearance = themeAppearance,
                    themeContrast = themeContrast,
                    themeColorsCollections = themeColorsCollections,
                    showToolbar = showToolbar,
                    expandableToolbar = expandableToolbar,
                    primaryToolbar = primaryToolbar,
                    translucentToolbar = translucentToolbar,
                    showNavigation = showNavigation,
                    primaryNavigation = primaryNavigation,
                    translucentBottomNavigation = translucentBottomNavigation,
                )
            }

        if (adaptive.value) {
            AdaptiveScaffoldDemo(
                platform = hostPlatform,
                navigationItems = navigationItems,
                selectedDestination = selectedDestination,
                sideNavigationExpanded = sideNavigationExpanded,
                showToolbar = showToolbar,
                expandableToolbar = expandableToolbar,
                toolbarScrollBehavior = toolbarScrollBehavior,
                showNavigation = showNavigation,
                topBarColor = topBarColor,
                navigationColor = navigationColor,
                bottomNavigationColor = bottomNavigationColor,
                scaffoldColor = scaffoldColor,
                onBack = onBack,
                content = content,
            )
        } else {
            DefaultScaffoldDemo(
                navigationItems = navigationItems,
                selectedDestination = selectedDestination,
                showToolbar = showToolbar,
                expandableToolbar = expandableToolbar,
                toolbarScrollBehavior = toolbarScrollBehavior,
                showNavigation = showNavigation,
                topBarColor = topBarColor,
                navigationColor = bottomNavigationColor,
                scaffoldColor = scaffoldColor,
                onBack = onBack,
                content = content,
            )
        }
    }
}

@Composable
private fun DefaultScaffoldDemo(
    navigationItems: List<StyledNavigationItem<Int>>,
    selectedDestination: MutableState<Int>,
    showToolbar: MutableState<Boolean>,
    expandableToolbar: MutableState<Boolean>,
    toolbarScrollBehavior: StyledExpandableTopBarScrollBehavior,
    showNavigation: MutableState<Boolean>,
    topBarColor: Color,
    navigationColor: Color,
    scaffoldColor: Color,
    onBack: () -> Unit,
    content: @Composable (PaddingValues, State<StyledAdaptiveInfo>?) -> Unit,
) {
    val toolbarContentColor = if (showToolbar.value) {
        StyledTheme.contentColorFor(topBarColor.copy(alpha = 1f))
    } else {
        StyledTheme.contentColorFor(scaffoldColor)
    }
    val navigationContentColor = if (showNavigation.value) {
        StyledTheme.contentColorFor(navigationColor.copy(alpha = 1f))
    } else {
        StyledTheme.contentColorFor(scaffoldColor)
    }
    SystemBarStyleEffectIfNeeded(
        statusBarColor = if (showToolbar.value) topBarColor else scaffoldColor,
        navigationBarColor = if (showNavigation.value) navigationColor else scaffoldColor,
        backgroundColor = scaffoldColor,
        statusBarContentColor = toolbarContentColor,
        navigationBarContentColor = navigationContentColor,
    )

    StyledScaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (showToolbar.value && expandableToolbar.value) {
                    Modifier.nestedScroll(toolbarScrollBehavior.nestedScrollConnection)
                } else {
                    Modifier
                },
            ),
        containerColor = scaffoldColor,
        topBar = {
            if (showToolbar.value) {
                DemoTopBar(
                    containerColor = topBarColor,
                    contentColor = StyledTheme.contentColorFor(topBarColor.copy(alpha = 1f)),
                    title = "Default scaffold",
                    onBack = onBack,
                    scrollBehavior = toolbarScrollBehavior.takeIf { expandableToolbar.value },
                )
            }
        },
        bottomBar = {
            if (showNavigation.value) {
                DemoBottomNavigation(
                    navigationItems = navigationItems,
                    selectedDestination = selectedDestination,
                    containerColor = navigationColor,
                    contentColor = StyledTheme.contentColorFor(navigationColor.copy(alpha = 1f)),
                )
            }
        },
        contentWindowInsets = StyledScaffoldDefaults.contentWindowInsets,
        content = { paddingValues -> content(paddingValues, null) },
    )
}

@Composable
private fun AdaptiveScaffoldDemo(
    platform: Platform,
    navigationItems: List<StyledNavigationItem<Int>>,
    selectedDestination: MutableState<Int>,
    sideNavigationExpanded: MutableState<Boolean>,
    showToolbar: MutableState<Boolean>,
    expandableToolbar: MutableState<Boolean>,
    toolbarScrollBehavior: StyledExpandableTopBarScrollBehavior,
    showNavigation: MutableState<Boolean>,
    topBarColor: Color,
    navigationColor: Color,
    bottomNavigationColor: Color,
    scaffoldColor: Color,
    onBack: () -> Unit,
    content: @Composable (PaddingValues, State<StyledAdaptiveInfo>?) -> Unit,
) {
    val scaffoldState = remember { StyledAdaptiveScaffoldState() }
    val adaptiveNavigationMode = scaffoldState.navigationMode
    if (adaptiveNavigationMode != null) {
        SystemBarStyleEffectIfNeeded(
            statusBarColor = if (showToolbar.value) topBarColor else scaffoldColor,
            navigationBarColor = if (showNavigation.value && adaptiveNavigationMode.isBottom) {
                bottomNavigationColor
            } else {
                scaffoldColor
            },
            backgroundColor = scaffoldColor,
            statusBarContentColor = if (showToolbar.value) {
                StyledTheme.contentColorFor(topBarColor.copy(alpha = 1f))
            } else {
                StyledTheme.contentColorFor(scaffoldColor)
            },
            navigationBarContentColor = if (showNavigation.value && adaptiveNavigationMode.isBottom) {
                StyledTheme.contentColorFor(bottomNavigationColor.copy(alpha = 1f))
            } else {
                StyledTheme.contentColorFor(scaffoldColor)
            },
        )
    }

    StyledAdaptiveScaffold(
        platform = platform,
        state = scaffoldState,
        sideNavigationExpanded = sideNavigationExpanded,
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (showToolbar.value && expandableToolbar.value) {
                    Modifier.nestedScroll(toolbarScrollBehavior.nestedScrollConnection)
                } else {
                    Modifier
                },
            ),
        containerColor = scaffoldColor,
        topBar = {
            if (showToolbar.value) {
                DemoTopBar(
                    containerColor = topBarColor,
                    contentColor = StyledTheme.contentColorFor(topBarColor.copy(alpha = 1f)),
                    title = "Adaptive scaffold",
                    onBack = onBack,
                    scrollBehavior = toolbarScrollBehavior.takeIf { expandableToolbar.value },
                )
            }
        },
        navigation = { navigationMode ->
            if (showNavigation.value) {
                when (navigationMode) {
                    StyledNavigationMode.Bottom -> DemoBottomNavigation(
                        navigationItems = navigationItems,
                        selectedDestination = selectedDestination,
                        containerColor = bottomNavigationColor,
                        contentColor = StyledTheme.contentColorFor(navigationColor),
                    )

                    is StyledNavigationMode.Side -> DemoSideNavigation(
                        navigationItems = navigationItems,
                        selectedDestination = selectedDestination,
                        mode = navigationMode.mode,
                        expanded = sideNavigationExpanded,
                        containerColor = navigationColor,
                    )
                }
            }
        },
        contentWindowInsets = StyledScaffoldDefaults.contentWindowInsets,
    ) { paddingValues, adaptiveInfo ->
        content(paddingValues, adaptiveInfo)
    }
}

@Composable
private fun TemplateContent(
    paddingValues: PaddingValues,
    adaptiveInfo: State<StyledAdaptiveInfo>?,
    selectedDestination: MutableState<Int>,
    adaptive: MutableState<Boolean>,
    themeColors: MutableState<StyledThemeColors>,
    themeVariant: MutableState<StyledThemeVariant>,
    themeAppearance: MutableState<StyledThemeAppearance>,
    themeContrast: MutableState<StyledThemeContrast>,
    themeColorsCollections: List<StyledThemeColorsCollection>,
    showToolbar: MutableState<Boolean>,
    expandableToolbar: MutableState<Boolean>,
    primaryToolbar: MutableState<Boolean>,
    translucentToolbar: MutableState<Boolean>,
    showNavigation: MutableState<Boolean>,
    primaryNavigation: MutableState<Boolean>,
    translucentBottomNavigation: MutableState<Boolean>,
) {
    val destinationStateHolder = rememberSaveableStateHolder()
    val layoutDirection = LocalLayoutDirection.current
    val behindToolbar = showToolbar.value && translucentToolbar.value
    val behindBottomNavigation =
        showNavigation.value && translucentBottomNavigation.value && (adaptiveInfo == null || adaptiveInfo.value.navigationMode.isBottom)

    val viewportPadding = PaddingValues(
        start = paddingValues.calculateStartPadding(layoutDirection),
        top = if (behindToolbar) 0.dp else paddingValues.calculateTopPadding(),
        end = paddingValues.calculateEndPadding(layoutDirection),
        bottom = if (behindBottomNavigation) 0.dp else paddingValues.calculateBottomPadding(),
    )
    val themePadding = StyledTheme.padding.content

    // Overlay-bar padding belongs inside scrolling content, not outside its viewport.
    val contentPadding = PaddingValues(
        start = themePadding.calculateStartPadding(layoutDirection),
        top = themePadding.calculateTopPadding() +
                if (behindToolbar) paddingValues.calculateTopPadding() else 0.dp,
        end = themePadding.calculateEndPadding(layoutDirection),
        bottom = themePadding.calculateBottomPadding() +
                if (behindBottomNavigation) paddingValues.calculateBottomPadding() else 0.dp,
    )

    Crossfade(
        modifier = Modifier
            .fillMaxSize()
            .padding(viewportPadding)
            .consumeWindowInsets(paddingValues),
        targetState = selectedDestination.value,
        label = "DemoTemplateContent",
    ) {
        destinationStateHolder.SaveableStateProvider(it) {
            when (it) {
                0 -> SubPageSettings(
                    adaptive = adaptive,
                    adaptiveInfo = adaptiveInfo?.value,
                    expandableToolbar = expandableToolbar,
                    contentPadding = contentPadding,
                )

                1 -> SubPageStyling(
                    themeColors = themeColors,
                    themeVariant = themeVariant,
                    themeAppearance = themeAppearance,
                    themeContrast = themeContrast,
                    themeColorsCollections = themeColorsCollections,
                    showToolbar = showToolbar,
                    primaryToolbar = primaryToolbar,
                    translucentToolbar = translucentToolbar,
                    showNavigation = showNavigation,
                    primaryNavigation = primaryNavigation,
                    translucentBottomNavigation = translucentBottomNavigation,
                    contentPadding = contentPadding,
                )

                2 -> SubPageMasterPane(contentPadding)
                else -> SubPagePlaceholder(contentPadding)
            }
        }
    }
}

@Composable
private fun SubPageSettings(
    adaptive: MutableState<Boolean>,
    adaptiveInfo: StyledAdaptiveInfo?,
    expandableToolbar: MutableState<Boolean>,
    contentPadding: PaddingValues,
) {
    StyledVerticalScrollContainer(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            StyledCard(modifier = Modifier.fillMaxWidth()) {
                StyledColumn(itemSpacing = StyledTheme.spacing.medium) {

                    SubTitle("Scaffold")
                    TemplateSwitchRow("Adaptive scaffold", adaptive)
                    StyledText("Toggle adaptive layout and observe the scaffold decisions.")

                    SubTitle("Toolbar")
                    TemplateSwitchRow("Expandable top toolbar", expandableToolbar)
                    StyledText(
                        "When enabled, scrolling collapses the expanded title into the top bar. " +
                                "Turn off the toolbar in Styling to disable it temporarily.",
                    )
                }
            }
            adaptiveInfo?.let { info ->
                StyledCard(modifier = Modifier.fillMaxWidth()) {
                    StyledColumn(itemSpacing = StyledTheme.spacing.small) {
                        SubTitle("Adaptive info")
                        StyledInformationRow(
                            label = "Platform",
                            info = "${info.context.platform}",
                        )
                        StyledInformationRow(
                            label = "Layout",
                            info = "${info.context.layoutSize.width} x " +
                                    "${info.context.layoutSize.height} " +
                                    "(${info.context.layoutSize.widthClass})",
                        )
                        StyledInformationRow(
                            label = "Window",
                            info = "${info.context.windowSize.width} x " +
                                    "${info.context.windowSize.height} " +
                                    "(${info.context.windowSize.widthClass})",
                        )
                        StyledInformationRow(
                            label = "Navigation mode",
                            info = "${info.navigationMode}",
                        )
                        StyledInformationRow(
                            label = "Master/detail mode",
                            info = "${info.masterDetailMode}",
                        )
                        StyledInformationRow(
                            label = "Dialog style",
                            info = "${info.dialogStyle}",
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubPageStyling(
    themeColors: MutableState<StyledThemeColors>,
    themeVariant: MutableState<StyledThemeVariant>,
    themeAppearance: MutableState<StyledThemeAppearance>,
    themeContrast: MutableState<StyledThemeContrast>,
    themeColorsCollections: List<StyledThemeColorsCollection>,
    showToolbar: MutableState<Boolean>,
    primaryToolbar: MutableState<Boolean>,
    translucentToolbar: MutableState<Boolean>,
    showNavigation: MutableState<Boolean>,
    primaryNavigation: MutableState<Boolean>,
    translucentBottomNavigation: MutableState<Boolean>,
    contentPadding: PaddingValues,
) {
    StyledVerticalScrollContainer(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            StyledCard(modifier = Modifier.fillMaxWidth()) {
                StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
                    SubTitle("Theme")
                    StyledThemeColorsPicker(
                        themeColors = themeColors,
                        themeVariant = themeVariant,
                        themeAppearance = themeAppearance,
                        themeContrast = themeContrast,
                        modifier = Modifier.fillMaxWidth(),
                        collections = themeColorsCollections,
                    )
                }
            }

            StyledCard(modifier = Modifier.fillMaxWidth()) {
                StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
                    SubTitle("Options")
                    TemplateSwitchRow("Show toolbar", showToolbar)
                    TemplateSwitchRow("Primary toolbar", primaryToolbar)
                    TemplateSwitchRow(
                        "Semi-transparent toolbar + content behind",
                        translucentToolbar,
                    )
                    TemplateSwitchRow("Show navigation", showNavigation)
                    TemplateSwitchRow("Primary navigation", primaryNavigation)
                    TemplateSwitchRow(
                        "Semi-transparent bottom navigation + content behind",
                        translucentBottomNavigation,
                    )
                    StyledText(
                        "Scroll the colored content behind the bars to check transparency " +
                                "and system-bar icon contrast. In adaptive side-navigation mode, " +
                                "the bottom-navigation option has no effect.",
                    )
                }
            }
            TemplateScrollPreview()
        }
    }
}

@Composable
private fun TemplateScrollPreview() {
    for (index in 1..12) {
        val color = if (index % 2 == 0) {
            StyledTheme.colorScheme.primarySurface
        } else {
            StyledTheme.colorScheme.surfaceVariant
        }
        StyledColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(StyledTheme.padding.large),
            itemSpacing = StyledTheme.spacing.small,
        ) {
            StyledText("Scroll preview $index / 12", color = StyledTheme.contentColorFor(color))
            StyledText(
                "Watch this content move behind the semi-transparent bars.",
                color = StyledTheme.contentColorFor(color),
            )
        }
    }
}

@Composable
private fun TemplateSwitchRow(
    label: String,
    checked: MutableState<Boolean>,
) {
    StyledRow(itemSpacing = StyledTheme.spacing.medium) {
        StyledText(
            modifier = Modifier.weight(1f),
            text = label,
        )
        StyledSwitch(checked = checked)
    }
}

@Composable
private fun SubPageMasterPane(contentPadding: PaddingValues) {
    val selectedItem = rememberSaveable { mutableStateOf<Int?>(null) }
    StyledAdaptiveContainer(Modifier.fillMaxSize()) { adaptiveInfo ->
        val masterDetailMode = StyledAdaptive.masterDetail(adaptiveInfo)
        StyledMasterDetail(
            modifier = Modifier.fillMaxSize(),
            mode = masterDetailMode.value,
            detailData = selectedItem.value,
            master = {
                StyledVerticalScrollContainer(
                    modifier = Modifier.fillMaxSize()
                        .background(StyledTheme.colorScheme.surface),
                    contentPadding = contentPadding,
                ) {
                    StyledColumn(
                        modifier = Modifier.fillMaxWidth(),
                        itemSpacing = StyledTheme.spacing.medium,
                    ) {
                        SubTitle("Items")
                        for (item in 1..5) {
                            StyledListItem(
                                headline = { StyledText("Item $item") },
                                supporting = { StyledText("Select to view details") },
                                onClick = { selectedItem.value = item },
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = if (selectedItem.value == item) {
                                    StyledTheme.colorScheme.primarySurface
                                } else {
                                    Color.Unspecified
                                },
                            )
                        }
                    }
                }
            },
            detail = { item ->
                if (item == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(StyledTheme.colorScheme.surfaceVariant)
                            .padding(contentPadding),
                        contentAlignment = Alignment.Center,
                    ) {
                        StyledText("Select an item to see its details.")
                    }
                } else {
                    StyledColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(StyledTheme.colorScheme.surfaceVariant)
                            .padding(contentPadding),
                        itemSpacing = StyledTheme.spacing.medium,
                    ) {
                        StyledText(
                            text = "Details $item",
                        )
                        if (masterDetailMode.value == StyledAdaptiveMasterDetailMode.SinglePane) {
                            StyledButton(
                                onClick = { selectedItem.value = null }
                            ) {
                                StyledText("Back to items")
                            }
                        }
                    }
                }
            },
        )
    }
}

@Composable
private fun SubPagePlaceholder(
    contentPadding: PaddingValues,
) {
    val fullscreenSettingsDialog = rememberDialogState()
    StyledVerticalScrollContainer(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        StyledCard(modifier = Modifier.fillMaxWidth()) {
            StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
                StyledButton(onClick = fullscreenSettingsDialog::show) {
                    StyledText("Open Test Fullscreen Test Settings")
                }
            }
        }
    }
    StyledDialog(
        state = fullscreenSettingsDialog,
        style = StyledDialog.Style.Fullscreen(
            animation = StyledDialog.FullscreenAnimation.Slide(
                direction = StyledDialog.FullscreenAnimation.SlideDirection.FromBottom
            ),
            expandableHeader = StyledDialog.Style.Fullscreen.ExpandableHeader(
            )
        ),
        title = {
            StyledText("Fullscreen Settings")
        },
        buttons = StyledDialog.Buttons(
            negative = StyledDialog.Button(
                label = "Back",
                icon = StyledIcons.NavigateBack,
                onClick = fullscreenSettingsDialog::dismiss
            )
        ),
    ) { dialogContentPadding ->
        StyledVerticalScrollContainer(
            modifier = Modifier.fillMaxSize(),
            contentPadding = dialogContentPadding,
        ) {
            StyledColumn {
                repeat(5) {
                    StyledText("Settings Content $it")
                }
            }
        }
    }
}

@Composable
private fun createNavigationItems(): List<StyledNavigationItem<Int>> = remember {
    listOf(
        StyledNavigationItem(
            key = 0,
            label = { StyledText("Einstellungen") },
            icon = {
                StyledIcon(
                    Icons.Default.Settings.toIconComposable(),
                    contentDescription = null
                )
            },
        ),
        StyledNavigationItem(
            key = 1,
            label = { StyledText("Styling") },
            icon = {
                StyledIcon(
                    Icons.Default.Style.toIconComposable(),
                    contentDescription = null
                )
            },
        ),
        StyledNavigationItem(
            key = 2,
            label = { StyledText("MasterPane") },
            icon = {
                StyledIcon(
                    Icons.Default.GridView.toIconComposable(),
                    contentDescription = null
                )
            },
        ),
        StyledNavigationItem(
            key = 3,
            label = { StyledText("Settings") },
            icon = {
                StyledIcon(
                    Icons.Default.Settings.toIconComposable(),
                    contentDescription = null
                )
            },
        ),
    )
}