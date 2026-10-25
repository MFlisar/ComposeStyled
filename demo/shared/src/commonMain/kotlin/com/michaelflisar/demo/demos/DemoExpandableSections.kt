package com.michaelflisar.demo.demos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledExpandableSection
import com.michaelflisar.composestyledcomponents.ui.components.rememberStyledExpandableSectionStyle
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoExpandableSections(baseId: Int, expandState: ExpandState) {
    val expanded = remember { mutableStateOf(false) }
    val customExpanded = remember { mutableStateOf(true) }

    RegionContainer(
        title = "Expandable Sections",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Default section")
        StyledExpandableSection(
            title = { StyledText("Account details") },
            expanded = expanded,
        ) {
            StyledText("This section can be expanded and collapsed.")
        }

        SubTitle("Custom section")
        StyledExpandableSection(
            title = { StyledText("Preferences") },
            style = rememberStyledExpandableSectionStyle(
                iconPlacement = StyledExpandableSection.IconPlacement.Right,
                containerColor = StyledTheme.colorScheme.surfaceVariant,
                contentColor = StyledTheme.colorScheme.onSurfaceVariant,
            ),
            expanded = customExpanded,
        ) {
            StyledText("The icon placement and colors can be customized.")
        }
    }
}
