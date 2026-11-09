package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledTooltip
import com.michaelflisar.composestyled.ui.components.rememberStyledTooltipState
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer

@Composable
internal fun DemoTooltip(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Tooltips",
        id = baseId + 1,
        expanded = expandState,
    ) {
        val tooltipState = rememberStyledTooltipState()
        val persistentTooltipState = rememberStyledTooltipState(isPersistent = true)

        StyledTooltip(
            state = tooltipState,
            tooltip = {
                StyledText("A short description for this control.")
            },
        ) {
            StyledButton(onClick = {}) {
                StyledText("Hover or long-press")
            }
        }
        StyledTooltip(
            state = persistentTooltipState,
            shape = StyledTheme.shapes.large,
            containerColor = StyledTheme.colorScheme.primarySurface,
            contentColor = StyledTheme.colorScheme.onPrimarySurface,
            maxWidth = 280.dp,
            tooltip = {
                StyledText("A customized persistent tooltip.")
            },
        ) {
            StyledButton(onClick = {}) {
                StyledText("Customized tooltip")
            }
        }
    }
}
