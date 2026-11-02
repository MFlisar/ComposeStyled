package com.michaelflisar.demo.demos.regions

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledInputButton
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoInteractionBlocker(
    baseId: Int,
    expandState: ExpandState,
    showMessage: (String) -> Unit,
) {
    RegionContainer(
        title = "Interaction Blocker",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Clickable Input")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledInputButton(
                value = "Click me",
                onClick = {
                    showMessage("Clicked!")
                }
            )
            StyledInputButton(
                value = "Click me",
                label = "Label",
                onClick = {
                    showMessage("Clicked!")
                }
            )
        }
    }
}
