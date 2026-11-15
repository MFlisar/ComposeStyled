package com.michaelflisar.demo.demos.libraries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composepreferences.styled.navigation.PreferenceBreadcrumb
import com.michaelflisar.composepreferences.styled.PreferenceFilter
import com.michaelflisar.composepreferences.styled.PreferenceOpenGroupHeaderStyle
import com.michaelflisar.composepreferences.styled.PreferenceSectionContentSeparatorStyle
import com.michaelflisar.composepreferences.styled.PreferenceStyleDefaults
import com.michaelflisar.composepreferences.styled.layout.PreferenceScreen
import com.michaelflisar.composepreferences.styled.PreferenceBool
import com.michaelflisar.composepreferences.styled.PreferenceCustomItem
import com.michaelflisar.composepreferences.styled.PreferenceInputText
import com.michaelflisar.composepreferences.styled.PreferenceItem
import com.michaelflisar.composepreferences.styled.PreferenceList
import com.michaelflisar.composepreferences.styled.PreferenceListMultiSelect
import com.michaelflisar.composepreferences.styled.PreferenceNumber
import com.michaelflisar.composepreferences.styled.PreferenceSplitSwitch
import com.michaelflisar.composepreferences.styled.layout.PreferenceGroup
import com.michaelflisar.composepreferences.styled.layout.PreferenceSection
import com.michaelflisar.composepreferences.styled.rememberPreferenceFilter
import com.michaelflisar.composepreferences.styled.layout.rememberPreferenceScreenState
import com.michaelflisar.composepreferences.styled.rememberPreferenceStyle
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledOutlinedInput
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdown
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.demo.helper.DemoTopBar

@Composable
internal fun DemoPagePreferences(onBack: () -> Unit) {
    // "preferences"
    val notifications = rememberSaveable { mutableStateOf(true) }
    val cloudSync = rememberSaveable { mutableStateOf(true) }
    val usageAnalytics = rememberSaveable { mutableStateOf(false) }
    val advancedMode = rememberSaveable { mutableStateOf(false) }
    val fontScale = rememberSaveable { mutableFloatStateOf(1f) }
    val themeMode = rememberSaveable { mutableStateOf("System") }
    val primaryColor = rememberSaveable { mutableStateOf("Indigo") }
    val secondaryColor = rememberSaveable { mutableStateOf("Teal") }
    val backgroundColor = rememberSaveable { mutableStateOf("Surface") }
    val foregroundColor = rememberSaveable { mutableStateOf("On surface") }
    val density = rememberSaveable { mutableStateOf("Comfortable") }
    val language = rememberSaveable { mutableStateOf("English") }
    val visibleAreas = rememberSaveable {
        mutableStateOf(listOf("Home", "Search", "Settings"))
    }
    val profileLabel = rememberSaveable { mutableStateOf("My profile") }
    val detailDialog = rememberDialogState()
    val themeModes = remember { listOf("System", "Light", "Dark") }
    val brandColors = remember { listOf("Indigo", "Blue", "Teal", "Violet") }
    val secondaryColors = remember { listOf("Teal", "Orange", "Rose", "Lime") }
    val backgroundColors = remember { listOf("Surface", "Surface variant", "Dark surface") }
    val foregroundColors = remember { listOf("On surface", "On surface variant", "High contrast") }
    val densities = remember { listOf("Compact", "Comfortable", "Spacious") }
    val languages = remember { listOf("English", "Deutsch", "Français") }
    val areas = remember { listOf("Home", "Search", "Favorites", "Settings", "Help") }
    val filterScopes = remember { listOf("Current page", "Current branch", "Everywhere") }

    // Preference State/Style
    val filter = rememberPreferenceFilter()
    val preferenceState = rememberPreferenceScreenState()
    val preferenceStyle = rememberPreferenceStyle(
        openedGroupHeader = PreferenceOpenGroupHeaderStyle.Disabled,
        spacing = PreferenceStyleDefaults.spacing(
            sectionContentSeparator = PreferenceSectionContentSeparatorStyle.Divider(
                verticalPadding = 4.dp,
            ),
        ),
    )

    val scaffoldColor = StyledTheme.colorScheme.surface
    val topBarColor = StyledTheme.colorScheme.primary
    SystemBarStyleEffectIfNeeded(
        statusBarColor = topBarColor,
        navigationBarColor = scaffoldColor,
        backgroundColor = scaffoldColor,
    )

    StyledScaffold(
        topBar = {
            DemoTopBar(
                containerColor = topBarColor,
                title = "Preferences",
                onBack = onBack,
            )
        },
        containerColor = scaffoldColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
        ) {

            // 1) Filter (optional)
            StyledOutlinedInput(
                value = filter.query,
                onValueChange = { filter.query = it },
                modifier = Modifier.fillMaxWidth().padding(preferenceStyle.outerPadding),
                label = { StyledText("Search preferences") },
            )

            StyledRow(
                itemSpacing = StyledTheme.spacing.medium,
                modifier = Modifier.fillMaxWidth().padding(preferenceStyle.outerPadding),
            ) {

                val selectedFilterScope = when (filter.scope) {
                    PreferenceFilter.Scope.CurrentLevel -> filterScopes[0]
                    PreferenceFilter.Scope.CurrentBranch -> filterScopes[1]
                    PreferenceFilter.Scope.Global -> filterScopes[2]
                }
                StyledPopupDropdown(
                    items = filterScopes,
                    selected = selectedFilterScope,
                    variant = StyledButton.Variant.Outlined,
                    modifier = Modifier.weight(1f),
                    title = "Search scope",
                    onSelectionChange = { _, selected ->
                        filter.scope = when (selected) {
                            filterScopes[0] -> PreferenceFilter.Scope.CurrentLevel
                            filterScopes[1] -> PreferenceFilter.Scope.CurrentBranch
                            else -> PreferenceFilter.Scope.Global
                        }
                    },
                )

                StyledPopupMenu(
                    triggerContent = {
                        StyledIcon(StyledIcons.Settings)
                    },
                    modifier = Modifier,//.fillMaxHeight(),
                ) {
                    item(
                        text = "Include group and section names",
                        //leadingIcon = {
                        //    StyledIcon(
                        //        icon = Icons.Default.FilterAlt.toIconComposable(),
                        //        contentDescription = null
                        //    )
                        //},
                        trailingIcon = {
                            StyledIcon(
                                icon = if (filter.includeHierarchyInFilter) {
                                    StyledIcons.Check
                                } else StyledIcons.Cancel,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            filter.includeHierarchyInFilter = !filter.includeHierarchyInFilter
                        },
                        closeOnClick = false,
                    )
                }
            }

            // 2) Breadcrumbs (optional)
            PreferenceBreadcrumb(
                state = preferenceState,
                modifier = Modifier.fillMaxWidth(),
                filter = filter,
            )

            // 3) Preference Screen
            PreferenceScreen(
                modifier = Modifier.weight(1f),
                state = preferenceState,
                filter = filter,
                style = preferenceStyle
            ) {
                PreferenceSection(
                    title = "Boolean Preferences",
                    isExpandable = false,
                    filterTags = listOf("Toggle", "Switch", "Preferences"),
                ) {
                    PreferenceGroup(title = "Boolean preference") {
                        PreferenceBool(
                            value = notifications,
                            title = "Notifications",
                            subtitle = "Show updates and reminders",
                            filterTags = listOf("Alerts"),
                            icon = StyledIcons.Notifications
                        )
                        PreferenceBool(
                            value = cloudSync,
                            title = "Cloud Sync",
                            subtitle = "Keep preferences in sync across devices",
                            filterTags = listOf("Backup", "Account"),
                        )
                        PreferenceBool(
                            value = usageAnalytics,
                            title = "Usage Analytics",
                            subtitle = "Share anonymous diagnostics to improve the app",
                            filterTags = listOf("Telemetry", "Diagnostics"),
                        )
                    }
                }

                PreferenceSection(
                    title = "Selection Preferences",
                    isExpandable = false,
                    filterTags = listOf("List", "Dropdown", "Selection"),
                ) {
                    PreferenceGroup(
                        title = "Single selection",
                        subtitle = "Choose one option from a list or dropdown",
                        filterTags = listOf("Single-select", "List", "Dropdown"),
                    ) {
                        PreferenceList(
                            items = themeModes,
                            selected = themeMode,
                            title = "Theme mode",
                            subtitle = "Choose how the app follows your system appearance",
                            filterTags = listOf("Appearance", "Light", "Dark"),
                            icon = StyledIcons.Settings
                        )
                        PreferenceList(
                            items = languages,
                            selected = language,
                            title = "Language",
                            subtitle = "Language used for menus and messages",
                            filterTags = listOf("Localization", "Locale"),
                            icon = StyledIcons.Settings
                        )
                        PreferenceList(
                            items = brandColors,
                            selected = primaryColor,
                            title = "Primary Color",
                            subtitle = "Main brand color for prominent actions",
                            filterTags = listOf("Accent", "Brand", "Actions"),
                            presentation = PreferenceList.Presentation.Dropdown(
                                clickBehavior =
                                    PreferenceList.Presentation.Dropdown.ClickBehavior.DropdownOnly,
                            ),
                            itemLeadingContent = {
                                StyledIcon(icon = StyledIcons.Check)
                            },
                        )
                        PreferenceList(
                            items = secondaryColors,
                            selected = secondaryColor,
                            title = "Secondary Color",
                            subtitle = "Supporting accent for less prominent elements",
                            filterTags = listOf("Accent", "Supporting", "Brand"),
                        )
                        PreferenceList(
                            items = backgroundColors,
                            selected = backgroundColor,
                            title = "Background Color",
                            subtitle = "Default surface behind preference content",
                            filterTags = listOf("Canvas", "Surface", "Dark mode"),
                        )
                        PreferenceList(
                            items = foregroundColors,
                            selected = foregroundColor,
                            title = "Foreground Color",
                            subtitle = "Text and icon color shown on surfaces",
                            filterTags = listOf("Text", "Contrast", "Accessibility"),
                        )
                        PreferenceList(
                            items = densities,
                            selected = density,
                            title = "Layout Density",
                            subtitle = "Choose the spacing between controls",
                            filterTags = listOf("Compact", "Spacing")
                        )
                    }
                    PreferenceGroup(
                        title = "Multiple selection",
                        subtitle = "Select several options from a filtered dropdown",
                        filterTags = listOf("Multi-select", "Dropdown", "Filter"),
                    ) {
                        PreferenceListMultiSelect(
                            items = areas,
                            selected = visibleAreas,
                            title = "Visible Areas",
                            presentation = PreferenceList.Presentation.Dropdown(),
                            filter = PreferenceList.Filter(label = "Filter areas"),
                            filterTags = listOf("Navigation", "Menu"),
                        )
                    }
                }

                PreferenceSection(
                    title = "Value Preferences",
                    isExpandable = false,
                    filterTags = listOf("Input", "Value", "Preferences"),
                ) {
                    PreferenceGroup(title = "Numeric preference") {
                        PreferenceNumber(
                            value = fontScale,
                            title = "Font Scale",
                            subtitle = "Adjust the overall text size",
                            style = PreferenceNumber.Style.Slider,
                            valueRange = 0.75f..1.5f,
                            stepSize = 0.05f,
                            filterTags = listOf("Typography", "Accessibility", "Text size"),
                        )
                    }
                    PreferenceGroup(title = "Text input preference") {
                        PreferenceInputText(
                            value = profileLabel,
                            title = "Profile Label",
                            subtitle = "Custom label used in the account menu",
                            filterTags = listOf("Account", "Name"),
                            shape = StyledTheme.shapes.small,
                        )
                    }
                }

                PreferenceSection(
                    title = "Actions",
                    isExpandable = false,
                    filterTags = listOf("Switch", "Dialog", "Interaction"),
                ) {
                    PreferenceGroup(
                        title = "Split switch",
                        subtitle = "Open details or toggle the value independently",
                        filterTags = listOf("Dialog", "Advanced"),
                    ) {
                        PreferenceSplitSwitch(
                            value = advancedMode,
                            title = "Advanced Mode",
                            subtitle = "Open details or toggle the mode",
                            onOpen = { detailDialog.show() },
                            filterTags = listOf("Experimental"),
                            shape = StyledTheme.shapes.small,
                        )
                    }
                }

                PreferenceSection(
                    title = "Item Content",
                    isExpandable = false,
                    filterTags = listOf("Custom", "Content", "Layout"),
                ) {
                    PreferenceGroup(
                        title = "Standard item variations",
                        subtitle = "Shape, trailing content, inline content, and titleless items",
                        filterTags = listOf("Subtitle", "Inline", "Trailing", "Titleless"),
                    ) {
                        PreferenceItem(
                            title = "Custom shape",
                            subtitle = "This row separates the section shape",
                            contentColor = StyledTheme.colorScheme.warning,
                            shape = StyledTheme.shapes.small,
                        )
                        PreferenceItem(
                            title = "Trailing and inline content",
                            subtitle = "Combine custom content slots",
                            content = {
                                StyledText("Trailing")
                            },
                            inlineContent = {
                                StyledText("This content appears below the preference heading.")
                            },
                            filterTags = listOf("Example", "Inline", "Trailing"),
                        )
                        PreferenceItem(
                            subtitle = "This preference has no title",
                            filterTags = listOf("Example", "Titleless"),
                        )
                    }
                    PreferenceGroup(
                        title = "Custom item",
                        subtitle = "Provide the complete content layout",
                        filterTags = listOf("Example", "Custom"),
                    ) {
                        PreferenceCustomItem {
                            StyledText("This item owns its complete content layout.")
                            StyledText("It has no built-in title or subtitle.")
                        }
                    }
                }

                PreferenceSection(
                    title = "Sections and Groups",
                    isExpandable = false,
                    filterTags = listOf("Layout", "Nested", "Expandable"),
                ) {
                    PreferenceGroup(
                        title = "Nested groups",
                        subtitle = "Place related items in nested groups",
                    ) {
                        PreferenceItem(title = "Item before nested group")
                        PreferenceGroup(
                            title = "Nested group",
                            subtitle = "A group inside another group",
                        ) {
                            PreferenceItem(title = "Nested item 1")
                            PreferenceItem(title = "Nested item 2")
                        }
                        PreferenceItem(title = "Item after nested group")
                    }
                    PreferenceGroup(
                        title = "Expandable section",
                        subtitle = "Sections can start collapsed",
                    ) {
                        PreferenceSection(
                            title = "More options",
                            isExpandable = true,
                            isInitiallyExpanded = false,
                            filterTags = listOf("Debug", "Testing"),
                        ) {
                            PreferenceItem(
                                title = "Hidden until expanded",
                                subtitle = "Expand the section to see this item",
                            )
                        }
                    }
                    PreferenceGroup(
                        title = "Section without a title",
                        subtitle = "Items can also be grouped without a section heading",
                    ) {
                        PreferenceSection {
                            PreferenceItem(title = "Grouped without a section title")
                            PreferenceItem(title = "Another grouped item")
                        }
                    }
                }
            }
        }
    }

    StyledDialog(
        state = detailDialog,
        title = { StyledText("Advanced mode details") },
    ) {
        StyledText("This action opens a nested detail view without toggling the switch.")
    }
}
