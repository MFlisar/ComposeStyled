package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.layout.StyledSideSheet
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoSideSheet(baseId: Int, expandState: ExpandState) {
    RegionContainer(title = "Side sheet", id = baseId + 1, expanded = expandState) {
        val expanded = rememberSaveable { mutableStateOf(false) }

        SubTitle("Swipe in from the start edge")
        StyledText("Open the sheet with the button or drag inward from the start edge.")
        StyledButton(onClick = { expanded.value = true }) {
            StyledText("Open side sheet")
        }
        StyledSurface(
            color = StyledTheme.colorScheme.primarySurface,
            contentColor = StyledTheme.colorScheme.onPrimarySurface
        ) {
            Box(
                Modifier.fillMaxWidth().height(280.dp)
            ) {
                StyledRow {
                    StyledText(
                        text = "The sheet overlays this content. Tap the scrim or press Back to close it.",
                        modifier = Modifier.padding(16.dp),
                    )
                }
                StyledSideSheet(
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = it },
                    side = StyledSideSheet.Side.Start,
                    expandedWidth = StyledSideSheet.Width.Fraction(0.82f),
                    triggerWidth = 28.dp,
                    containerColor = StyledTheme.colorScheme.secondarySurface,
                    contentColor = StyledTheme.colorScheme.onSecondarySurface,
                ) {
                    StyledColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        itemSpacing = 12.dp,
                    ) {
                        StyledText("Side sheet content")
                        StyledText("Its width is 82% of the available area.")
                        StyledButton(onClick = { expanded.value = false }) {
                            StyledText("Close")
                        }
                    }
                }
            }
        }
    }
}
