package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.ColorToken
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.components.StyledOutlinedCard
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBarDefaults
import com.michaelflisar.composestyled.ui.components.StyledCenterAlignedTopBar
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownIndex
import com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledLargeFloatingActionButton
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationDefaults
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSmallFloatingActionButton
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledSnackbarData
import com.michaelflisar.composestyled.ui.components.StyledSnackbarDuration
import com.michaelflisar.composestyled.ui.components.StyledSwitch
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon
import com.michaelflisar.composestyled.ui.state.rememberStyledExpandableTopBarScrollBehavior
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun ScaffoldExamples(
    baseId: Int,
    expandState: ExpandState,
    showSnackbar: () -> Unit,
) {
    RegionContainer(
        title = "Scaffold",
        id = baseId,
        expanded = expandState,
    ) {
        SubTitle("Top bar, content and floating action button")
        StyledScaffold(
            modifier = Modifier.fillMaxWidth().height(240.dp),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                StyledTopBar(
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = { StyledText("Scaffold preview") },
                )
            },
            floatingActionButton = {
                StyledSmallFloatingActionButton(
                    onClick = showSnackbar,
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "Show snackbar",
                    )
                }
            },
            content = { paddingValues ->
                StyledText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                    text = "Scaffold applies content padding around its slots.",
                )
            },
        )

        SubTitle("Expandable top bar")
        val scrollBehavior = rememberStyledExpandableTopBarScrollBehavior()
        StyledScaffold(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                StyledExpandableTopBar(
                    behavior = scrollBehavior,
                    expandedHeight = 180.dp,
                    expandedContainerColor = StyledTheme.colorScheme.primarySurface,
                    expandedContentColor = StyledTheme.colorScheme.onPrimarySurface,
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = { StyledText("Expandable toolbar") },
                )
            },
            content = { paddingValues ->
                StyledColumn(
                    itemSpacing = StyledTheme.spacing.medium,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                        .verticalScroll(rememberScrollState()),
                ) {
                    repeat(12) { index ->
                        StyledText("Scrollable content item ${index + 1}")
                    }
                }
            },
        )
    }
}

@Composable
internal fun DemoComponentParts(
    baseId: Int,
    expandState: ExpandState,
    showSnackbar: () -> Unit,
) {
    RegionContainer(
        title = "Top bars",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Top Bars (primary surface")
        StyledTopBar.Variant.entries.forEach { variant ->
            val title: @Composable () -> Unit = { StyledText(variant.name) }
            if (variant == StyledTopBar.Variant.Standard) {
                StyledTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = title,
                    containerColor = StyledTheme.colorScheme.primarySurface,
                    contentColor = StyledTheme.colorScheme.onPrimarySurface,
                )
            } else {
                StyledCenterAlignedTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = title,
                    containerColor = StyledTheme.colorScheme.primarySurface,
                    contentColor = StyledTheme.colorScheme.onPrimarySurface,
                )
            }
        }
        SubTitle("Top Bars with Customization")
        StyledTopBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = StyledTheme.colorScheme.primarySurface,
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                StyledButton(onClick = showSnackbar) {
                    StyledIcon(
                        icon = Icons.AutoMirrored.Filled.ArrowBack.toIconComposable(),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                StyledButton(onClick = showSnackbar) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "Add"
                    )
                }
            },
            title = { StyledText("Navigation and actions") },
        )
    }
    RegionContainer(
        title = "Navigation",
        id = baseId + 2,
        expanded = expandState
    ) {
        val selected = rememberSaveable { mutableStateOf(0) }
        val items = remember {
            listOf(
                StyledNavigationItem(
                    key = 0,
                    label = { StyledText("Home") },
                    icon = {
                        StyledIcon(
                            icon = Icons.Default.Check.toIconComposable(),
                            contentDescription = null
                        )
                    },
                ),
                StyledNavigationItem(
                    key = 1,
                    label = { StyledText("More") },
                    icon = {
                        StyledIcon(
                            icon = Icons.Default.Add.toIconComposable(),
                            contentDescription = null
                        )
                    },
                ),
                StyledNavigationItem(
                    key = 2,
                    label = { StyledText("Disabled") },
                    icon = {
                        StyledIcon(
                            icon = Icons.Default.Add.toIconComposable(),
                            contentDescription = null
                        )
                    },
                    enabled = false,
                ),
            )
        }
        SubTitle("Bottom Navigation")
        StyledBottomNavigationBar(
            items = items,
            selectedKey = selected.value,
            onItemClick = { selected.value = it.key },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        SubTitle("Bottom navigation with separate icon and text colors")
        StyledBottomNavigationBar(
            items = items,
            selectedKey = selected.value,
            onItemClick = { selected.value = it.key },
            containerColor = StyledTheme.colorScheme.primary,
            contentColor = StyledTheme.colorScheme.onPrimary,
            customization = StyledBottomNavigationBarDefaults.customize(
                selectedItemIconColor = ColorToken.Theme.OnPrimarySurface,
                selectedItemTextColor = ColorToken.Theme.OnPrimary,
                unselectedItemIconColor = ColorToken.Theme.OnPrimary,
                unselectedItemTextColor = ColorToken.Theme.OnPrimary,
                selectedItemIndicatorColor = ColorToken.Theme.PrimarySurface,
            ),
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        val longItems = remember(items) {
            List(18) { index ->
                items[index % items.size].copy(
                    key = index,
                    label = { StyledText("Destination ${index + 1}") },
                )
            }
        }
        SubTitle("Scrollable bottom navigation")
        StyledBottomNavigationBar(
            items = longItems,
            selectedKey = selected.value,
            onItemClick = { selected.value = it.key },
            scrollable = true,
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        SubTitle("Side navigation")

        val selectedMode = rememberSaveable { mutableStateOf(1) }
        val selectedLabels = rememberSaveable { mutableStateOf(0) }
        val selectedToggle = rememberSaveable { mutableStateOf(0) }
        val scrollable = rememberSaveable { mutableStateOf(true) }
        val customColors = rememberSaveable { mutableStateOf(false) }
        val showHeader = rememberSaveable { mutableStateOf(true) }
        val showFooter = rememberSaveable { mutableStateOf(true) }

        StyledOutlinedCard {
            StyledFlowRow(
                spacing = StyledTheme.spacing.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                StyledPopupDropdownIndex(
                    items = StyledSideNavigation.Mode.entries.map { it.name },
                    selectedIndex = selectedMode,
                )
                StyledPopupDropdownIndex(
                    items = StyledSideNavigation.CompactLabelVisibility.entries.map { it.name },
                    selectedIndex = selectedLabels,
                )
                StyledPopupDropdownIndex(
                    items = listOf("Menu", "Rotating arrow", "Menu / close"),
                    selectedIndex = selectedToggle,
                )
                StyledSwitch(checked = scrollable, label = { StyledText("Scrollable") })
                StyledSwitch(checked = customColors, label = { StyledText("Custom colors") })

                StyledSwitch(checked = showHeader, label = { StyledText("Header") })
                StyledSwitch(checked = showFooter, label = { StyledText("Footer") })
            }
        }

        StyledScaffold(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .border(1.dp, StyledTheme.colorScheme.outline),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            sideNavigation = {
                StyledSideNavigation(
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    mode = StyledSideNavigation.Mode.entries[selectedMode.value],
                    compactLabelVisibility = StyledSideNavigation.CompactLabelVisibility.entries[selectedLabels.value],
                    scrollable = scrollable.value,
                    containerColor = if (customColors.value) StyledTheme.colorScheme.primary else StyledTheme.colorScheme.surfaceVariant,
                    contentColor = if (customColors.value) StyledTheme.colorScheme.onPrimary else StyledTheme.contentColorFor(
                        StyledTheme.colorScheme.surfaceVariant
                    ),
                    customization = if (customColors.value) StyledSideNavigationDefaults.customize(
                        selectedItemContentColor = ColorToken.Theme.OnPrimarySurface,
                        unselectedItemContentColor = ColorToken.Theme.OnPrimary,
                        selectedItemIndicatorColor = ColorToken.Theme.PrimarySurface,
                    ) else null,
                    title = { StyledText("Destinations") },
                    toggleIcon = { state ->
                        when (selectedToggle.value) {
                            0 -> StyledSideNavigationDefaults.ToggleIcon(state)
                            1 -> StyledExpandIcon(
                                expansionProgress = state.expansionProgress,
                                expandDirection = StyledExpandIcon.ExpandDirection.End,
                            )
                            else -> StyledExpandIcon(
                                expanded = state.targetExpanded,
                                iconMode = StyledExpandIcon.IconMode.Replacing(
                                    collapsedIcon = Icons.Default.Menu.toIconComposable(),
                                    expandedIcon = Icons.Default.Close.toIconComposable(),
                                ),
                            )
                        }
                    },
                    header = if (showHeader.value) {
                        {
                            StyledColumn(
                                itemSpacing = StyledTheme.spacing.medium,
                                modifier = Modifier.padding(
                                    horizontal = StyledTheme.padding.medium,
                                    vertical = StyledTheme.padding.small
                                ).background(StyledTheme.colorScheme.primarySurface)
                            ) {
                                StyledText("My workspace")
                                StyledText("A multiline header with stable width while expanding and collapsing.")
                            }
                        }
                    } else null,
                    footer = if (showFooter.value) {
                        {
                            StyledColumn(
                                itemSpacing = StyledTheme.spacing.medium,
                                modifier = Modifier.padding(
                                    horizontal = StyledTheme.padding.medium,
                                    vertical = StyledTheme.padding.small
                                ).background(StyledTheme.colorScheme.primarySurface)
                            ) {
                                StyledText("Signed in")
                                StyledText("Footer stays pinned below the scrolling destinations.")
                            }
                        }
                    } else null,
                ) {
                    longItems.forEach { item ->
                        androidx.compose.runtime.key(item.key) {
                            StyledSideNavigationItem(
                                selected = item.key == selected.value,
                                onClick = { selected.value = item.key },
                                enabled = item.enabled,
                                label = item.label,
                                icon = item.icon,
                            )
                        }
                    }
                }
            },
        ) { padding ->
            StyledText(
                text = "Content follows the measured navigation width. Toggle quickly to reverse the animation.",
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .padding(StyledTheme.padding.content)
            )
        }
    }
    RegionContainer(
        title = "Floating action buttons",
        id = baseId + 3,
        expanded = expandState
    ) {
        SubTitle("Default Floating Action Buttons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledFloatingActionButton.Variant.entries.forEach { variant ->
                val content: @Composable () -> Unit = {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = variant.name
                    )
                }
                when (variant) {
                    StyledFloatingActionButton.Variant.Small -> StyledSmallFloatingActionButton(
                        onClick = showSnackbar,
                        content = content
                    )

                    StyledFloatingActionButton.Variant.Standard -> StyledFloatingActionButton(
                        onClick = showSnackbar,
                        content = content
                    )

                    StyledFloatingActionButton.Variant.Large -> StyledLargeFloatingActionButton(
                        onClick = showSnackbar,
                        content = content
                    )
                }
            }
        }
        SubTitle("Floating Action Buttons with Customization")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledFloatingActionButton(
                onClick = showSnackbar,
                containerColor = Color.Magenta,
                contentColor = Color.White,
                shape = StyledTheme.shapes.small,
            ) {
                StyledIcon(
                    icon = Icons.Default.Add.toIconComposable(),
                    contentDescription = "Custom action"
                )
            }
        }
    }
    RegionContainer(
        title = "Snackbars",
        id = baseId + 4,
        expanded = expandState
    ) {
        SubTitle("Default Snackbar")
        StyledSnackbar { StyledText("Changes saved") }
        SubTitle("Same message with dismiss icon")
        val dismissibleData = remember(showSnackbar) {
            object : StyledSnackbarData {
                override val message = "Changes saved"
                override val actionLabel: String? = null
                override val withDismissAction = true
                override val duration = StyledSnackbarDuration.Indefinite
                override fun performAction() = showSnackbar()
                override fun dismiss() = showSnackbar()
            }
        }
        StyledSnackbar(data = dismissibleData)
        SubTitle("Snackbar with Customization")
        StyledSnackbar(
            containerColor = StyledTheme.colorScheme.primarySurface,
            contentColor = StyledTheme.colorScheme.onPrimarySurface,
            shape = StyledTheme.shapes.large,
            action = {
                StyledButton(onClick = showSnackbar) {
                    StyledText("Undo")
                }
            },
        ) {
            StyledText("Changes saved with action")
        }
        SubTitle("Scaffold Snackbar Host")
        StyledButton(onClick = showSnackbar, variant = StyledButton.Variant.Filled) {
            StyledText("Show snackbar with action")
        }
    }
}
