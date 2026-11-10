package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.composestyled.utils.ExpandState

@Composable
internal fun DemoIcons(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Icons",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Theme Icons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledIcon(icon = StyledIcons.NavigateBack, contentDescription = "Navigate back")
            StyledIcon(icon = StyledIcons.NavigateForward, contentDescription = "Navigate forward")
            StyledIcon(icon = StyledIcons.Submenu, contentDescription = "Open submenu")
            StyledIcon(icon = StyledIcons.SubmenuBack, contentDescription = "Return to parent menu")
            StyledIcon(icon = StyledIcons.Cancel, contentDescription = "Cancel")
            StyledIcon(icon = StyledIcons.Check, contentDescription = "Check")
            StyledIcon(icon = StyledIcons.Add, contentDescription = "Add")
            StyledIcon(icon = StyledIcons.Delete, contentDescription = "Delete")
            StyledIcon(icon = StyledIcons.Edit, contentDescription = "Edit")
            StyledIcon(icon = StyledIcons.Search, contentDescription = "Search")
            StyledIcon(icon = StyledIcons.Menu, contentDescription = "Menu")
            StyledIcon(icon = StyledIcons.More, contentDescription = "More")
            StyledIcon(icon = StyledIcons.Refresh, contentDescription = "Refresh")
            StyledIcon(icon = StyledIcons.Save, contentDescription = "Save")
            StyledIcon(icon = StyledIcons.Home, contentDescription = "Home")
            StyledIcon(icon = StyledIcons.Settings, contentDescription = "Settings")
            StyledIcon(icon = StyledIcons.Account, contentDescription = "Account")
            StyledIcon(icon = StyledIcons.Notifications, contentDescription = "Notifications")
            StyledIcon(icon = StyledIcons.Info, contentDescription = "Info")
            StyledIcon(icon = StyledIcons.Help, contentDescription = "Help")
            StyledIcon(icon = StyledIcons.Favorite, contentDescription = "Favorite")
            StyledIcon(icon = StyledIcons.Share, contentDescription = "Share")
            StyledIcon(icon = StyledIcons.Filter, contentDescription = "Filter")
            StyledIcon(icon = StyledIcons.Sort, contentDescription = "Sort")
            StyledIcon(icon = StyledIcons.Copy, contentDescription = "Copy")
            StyledIcon(icon = StyledIcons.Paste, contentDescription = "Paste")
            StyledIcon(icon = StyledIcons.Cut, contentDescription = "Cut")
        }
        SubTitle("Custom Icons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledIcon(imageVector = Icons.Default.Check, contentDescription = "Check")
            StyledIcon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
        SubTitle("Icons with Customization")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledIcon(imageVector = Icons.Default.Check, tint = Color.Magenta, size = 32.dp, contentDescription = "Custom check")
            StyledIcon(imageVector = Icons.Default.Add, tint = Color.Blue, size = 48.dp, contentDescription = "Custom add")
        }
    }
}
