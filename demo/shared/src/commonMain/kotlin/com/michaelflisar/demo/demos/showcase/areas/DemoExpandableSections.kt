package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.specials.StyledExpandableSection
import com.michaelflisar.composestyled.ui.specials.rememberStyledExpandableSectionStyle
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoExpandableSections(baseId: Int, expandState: ExpandState) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val customExpanded = rememberSaveable { mutableStateOf(true) }

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
