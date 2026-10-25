package com.michaelflisar.demo.demos

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyledcomponents.ui.components.StyledInputButton
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.demo.helper.ExpandState
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
        StyledSpacedFlowRow {
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
