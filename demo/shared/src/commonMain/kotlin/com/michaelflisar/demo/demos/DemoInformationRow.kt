package com.michaelflisar.demo.demos

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.specials.StyledInformationRow
import com.michaelflisar.composestyledcomponents.ui.specials.StyledInformationRowHorizontal
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoInformationRow(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Information Row",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledInformationRow(
            label = "Status",
            info = "Ready",
        )
        StyledInformationRow(
            label = "Details",
            backgroundColor = StyledTheme.colorScheme.surfaceVariant,
        ) { modifier ->
            StyledText(modifier = modifier, text = "Rendered through a content slot")
        }
        StyledInformationRowHorizontal(
            label = "Version",
            info = "1.0",
            endContent = { StyledText("Latest") },
        )
        StyledInformationRowHorizontal(
            label = "Custom",
        ) { modifier ->
            StyledText(modifier = modifier, text = "Horizontal content slot")
        }
    }
}
