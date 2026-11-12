package com.michaelflisar.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composepreferences.styled.PreferenceItem
import com.michaelflisar.composepreferences.styled.PreferenceOpenGroupHeaderStyle
import com.michaelflisar.composepreferences.styled.PreferenceCustomItem
import com.michaelflisar.composepreferences.styled.layout.PreferenceGroup
import com.michaelflisar.composepreferences.styled.layout.PreferenceScreen
import com.michaelflisar.composepreferences.styled.layout.PreferenceSection
import com.michaelflisar.composepreferences.styled.layout.rememberPreferenceScreenState
import com.michaelflisar.composepreferences.styled.navigation.PreferenceBreadcrumb
import com.michaelflisar.composepreferences.styled.rememberPreferenceFilter
import com.michaelflisar.composepreferences.styled.rememberPreferenceStyle
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText

@Composable
fun PreferenceTest(
    paddingValues: PaddingValues = PaddingValues(),
    supportFiltering: Boolean = false,
    breadcrumbs: Boolean = true,
    custom: @Composable () -> Unit = {},
) {
    val filter = rememberPreferenceFilter()
    val preferenceState = rememberPreferenceScreenState()
    val preferenceStyle = rememberPreferenceStyle(
        openedGroupHeader = PreferenceOpenGroupHeaderStyle.Disabled,
    )

    StyledColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        itemSpacing = StyledTheme.spacing.medium,
    ) {
        if (supportFiltering) {
            StyledInput(
                value = filter.query,
                onValueChange = { filter.query = it },
                modifier = Modifier.fillMaxWidth().padding(preferenceStyle.outerPadding),
                label = { StyledText("Filter settings") },
            )
        }

        if (breadcrumbs) {
            PreferenceBreadcrumb(
                state = preferenceState,
                modifier = Modifier.fillMaxWidth(),
                filter = filter,
            )
        }

        PreferenceScreen(
            modifier = Modifier.weight(1f).background(Color.Red, StyledTheme.shapes.large),
            state = preferenceState,
            filter = filter,
            style = preferenceStyle,
        ) {
            PreferenceTestSettings(custom)
        }
    }
}

@Composable
private fun PreferenceTestSettings(
    custom: @Composable () -> Unit,
) {
    PreferenceTestHeader()

    PreferenceSection(
        title = "Style",
        isExpandable = false,
    ) {
        PreferenceGroup(title = "Theme") {
            PreferenceItem(title = "Appearance")
            PreferenceItem(title = "Toolbar style")
            PreferenceItem(title = "Contrast")
            PreferenceItem(title = "Theme colors")
            PreferenceItem(title = "Theme variant")
            PreferenceItem(title = "Dynamic colors")
        }
    }

    custom()

    PreferenceSection(
        title = "Language",
        isExpandable = false,
    ) {
        PreferenceItem(title = "Change language")
    }

    PreferenceSection(
        title = "About",
        isExpandable = false,
    ) {
        PreferenceItem(title = "Changelog")
        PreferenceItem(
            title = "Feedback",
            subtitle = "Send feedback about the app",
        )
        PreferenceGroup(title = "Backup") {
            PreferenceItem(title = "Create backup")
            PreferenceItem(title = "Restore backup")
        }
        PreferenceGroup(
            title = "Others",
            subtitle = "More information and links",
        ) {
            PreferenceItem(title = "Open Play Store")
        }
        PreferenceGroup(title = "Privacy") {
            PreferenceItem(title = "Privacy policy")
            PreferenceItem(title = "Advertising settings")
        }
        PreferenceGroup(title = "Developer Settings") {
            PreferenceItem(title = "Debug Drawer")
            PreferenceItem(title = "Force Pro Version")
            PreferenceItem(title = "Show Log File")
            PreferenceItem(title = "Restart App")
            PreferenceItem(title = "Kill App")
            PreferenceItem(title = "Hide Developer Settings")
        }
    }
}

@Composable
private fun PreferenceTestHeader() {
    val contentColor = LocalStyledContentColor.current

    PreferenceCustomItem(
        containerColor = Color.Transparent,
        contentColor = contentColor,
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
    ) {
        StyledRow(
            modifier = Modifier.fillMaxWidth(),
            itemSpacing = StyledTheme.spacing.medium,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(StyledTheme.colorScheme.primary, CircleShape),
            )
            StyledColumn(modifier = Modifier.weight(1f)) {
                StyledText("Toolbox", style = StyledTheme.typography.titleLarge)
                StyledText("1.0 (1)", style = StyledTheme.typography.bodySmall)
                StyledText("By Developer", style = StyledTheme.typography.bodySmall)
            }
        }
    }
}
