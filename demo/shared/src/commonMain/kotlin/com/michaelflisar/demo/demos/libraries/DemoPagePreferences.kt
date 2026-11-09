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
import com.michaelflisar.composepreferences.styled.layout.StyledPreferenceScreen
import com.michaelflisar.composepreferences.styled.StyledPreferenceBool
import com.michaelflisar.composepreferences.styled.StyledPreferenceCustomItem
import com.michaelflisar.composepreferences.styled.StyledPreferenceInfo
import com.michaelflisar.composepreferences.styled.StyledPreferenceInputText
import com.michaelflisar.composepreferences.styled.StyledPreferenceItem
import com.michaelflisar.composepreferences.styled.StyledPreferenceList
import com.michaelflisar.composepreferences.styled.StyledPreferenceListMultiSelect
import com.michaelflisar.composepreferences.styled.StyledPreferenceNumber
import com.michaelflisar.composepreferences.styled.StyledPreferenceSplitSwitch
import com.michaelflisar.composepreferences.styled.layout.StyledPreferenceGroup
import com.michaelflisar.composepreferences.styled.layout.StyledPreferenceSection
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
        openedGroupHeader = PreferenceOpenGroupHeaderStyle.Disabled
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
            StyledPreferenceScreen(
                modifier = Modifier.weight(1f),
                state = preferenceState,
                filter = filter,
                style = preferenceStyle
            ) {
                StyledPreferenceSection(
                    title = "General",
                    isExpandable = false,
                    filterTags = listOf("Basics", "Preferences"),
                ) {
                    StyledPreferenceBool(
                        value = notifications,
                        title = "Notifications",
                        subtitle = "Show updates and reminders",
                        filterTags = listOf("Alerts"),
                        icon = StyledIcons.Notifications
                    )
                    StyledPreferenceList(
                        items = themeModes,
                        selected = themeMode,
                        title = "Theme mode",
                        subtitle = "Choose how the app follows your system appearance",
                        filterTags = listOf("Appearance", "Light", "Dark"),
                        icon = StyledIcons.Settings
                    )
                    StyledPreferenceList(
                        items = languages,
                        selected = language,
                        title = "Language",
                        subtitle = "Language used for menus and messages",
                        filterTags = listOf("Localization", "Locale"),
                        icon = StyledIcons.Settings
                    )
                }

                StyledPreferenceSection(
                    title = "Section with groups",
                    isExpandable = false,
                    filterTags = listOf("Basics", "Preferences"),
                ) {
                    StyledPreferenceGroup(
                        title = "Appearance",
                        subtitle = "Customize theme colors, typography, and layout",
                        icon = StyledIcons.Settings,
                        filterTags = listOf("Visuals", "Theme", "Design"),
                    ) {
                        StyledPreferenceSection(
                            title = "Theme Colors",
                            filterTags = listOf("Palette", "Brand", "Background", "Foreground"),
                        ) {
                            StyledPreferenceList(
                                items = brandColors,
                                selected = primaryColor,
                                title = "Primary Color",
                                subtitle = "Main brand color for prominent actions",
                                filterTags = listOf("Accent", "Brand", "Actions"),
                                presentation = StyledPreferenceList.Presentation.Dropdown(
                                    clickBehavior =
                                        StyledPreferenceList.Presentation.Dropdown.ClickBehavior.DropdownOnly,
                                ),
                            )
                            StyledPreferenceList(
                                items = secondaryColors,
                                selected = secondaryColor,
                                title = "Secondary Color",
                                subtitle = "Supporting accent for less prominent elements",
                                filterTags = listOf("Accent", "Supporting", "Brand"),
                            )
                            StyledPreferenceList(
                                items = backgroundColors,
                                selected = backgroundColor,
                                title = "Background Color",
                                subtitle = "Default surface behind preference content",
                                filterTags = listOf("Canvas", "Surface", "Dark mode"),
                            )
                            StyledPreferenceList(
                                items = foregroundColors,
                                selected = foregroundColor,
                                title = "Foreground Color",
                                subtitle = "Text and icon color shown on surfaces",
                                filterTags = listOf("Text", "Contrast", "Accessibility"),
                            )
                        }
                        StyledPreferenceSection(
                            title = "Typography & Layout",
                            filterTags = listOf("Font", "Text", "Spacing", "Density"),
                        ) {
                            StyledPreferenceNumber(
                                value = fontScale,
                                title = "Font Scale",
                                subtitle = "Adjust the overall text size",
                                style = StyledPreferenceNumber.Style.Slider,
                                valueRange = 0.75f..1.5f,
                                stepSize = 0.05f,
                                filterTags = listOf("Typography", "Accessibility", "Text size"),
                            )
                            StyledPreferenceList(
                                items = densities,
                                selected = density,
                                title = "Layout Density",
                                subtitle = "Choose the spacing between controls",
                                filterTags = listOf("Compact", "Spacing"),
                            )
                            StyledPreferenceListMultiSelect(
                                items = areas,
                                selected = visibleAreas,
                                title = "Visible Areas",
                                presentation = StyledPreferenceList.Presentation.Dropdown(),
                                filter = StyledPreferenceList.Filter(label = "Filter areas"),
                                filterTags = listOf("Navigation", "Menu"),
                            )
                        }
                    }

                    StyledPreferenceGroup(
                        title = "Privacy & Data",
                        subtitle = "Manage sync and diagnostic information",
                        filterTags = listOf("Account", "Cloud", "Diagnostics"),
                    ) {
                        StyledPreferenceSection(
                            title = "Data & Privacy",
                            filterTags = listOf("Permissions", "Telemetry"),
                        ) {
                            StyledPreferenceBool(
                                value = cloudSync,
                                title = "Cloud Sync",
                                subtitle = "Keep preferences in sync across devices",
                                filterTags = listOf("Backup", "Account"),
                            )
                            StyledPreferenceBool(
                                value = usageAnalytics,
                                title = "Usage Analytics",
                                subtitle = "Share anonymous diagnostics to improve the app",
                                filterTags = listOf("Telemetry", "Diagnostics"),
                            )
                        }
                    }
                }

                StyledPreferenceGroup(
                    title = "Advanced",
                    subtitle = "Developer options and custom settings",
                    filterTags = listOf("Developer", "Expert"),
                ) {
                    StyledPreferenceSection(
                        title = "Developer Options",
                        isExpandable = false,
                        filterTags = listOf("Debug", "Testing"),
                    ) {
                        StyledPreferenceSplitSwitch(
                            value = advancedMode,
                            title = "Advanced Mode",
                            subtitle = "Open details or toggle the mode",
                            onOpen = { detailDialog.show() },
                            filterTags = listOf("Experimental"),
                        )
                        StyledPreferenceInputText(
                            value = profileLabel,
                            title = "Profile Label",
                            subtitle = "Custom label used in the account menu",
                            filterTags = listOf("Account", "Name"),
                        )
                        StyledPreferenceItem(
                            title = "Custom Content",
                            subtitle = "Trailing and inline content can be combined",
                            content = {
                                StyledText("Trailing")
                            },
                            inlineContent = {
                                StyledText("This content appears below the preference heading.")
                            },
                            filterTags = listOf("Example", "Inline", "Trailing"),
                        )
                        StyledPreferenceCustomItem(
                            filterTags = listOf("Example", "Custom"),
                        ) {
                            StyledText("This item owns its complete content layout.")
                            StyledText("It has no built-in title or subtitle.")
                        }
                    }
                }

                StyledPreferenceSection(
                    title = "More",
                    isExpandable = true,
                    isInitiallyExpanded = false,
                    filterTags = listOf("Debug", "Testing"),
                ) {
                    StyledPreferenceInfo(
                        title = "More",
                        text = "Item in section that is not initially expanded"
                    )
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
