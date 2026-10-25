package com.michaelflisar.demo.demos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.components.StyledCheckbox
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.ExpandState

@Composable
internal fun DemoCheckboxes(baseId: Int, expandState: ExpandState) {
    val checked = remember { mutableStateOf(false) }
    val customChecked = remember { mutableStateOf(true) }
    RegionContainer(
        title = "Checkboxes",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Checkboxes")
        StyledSpacedFlowRow{
            StyledCheckbox(
                checked = checked,
                modifier = Modifier.semantics { contentDescription = "Default checkbox" },
            )
            StyledText("Checked: ${checked.value}")
        }
        SubTitle("Disabled Checkboxes")
        StyledSpacedFlowRow {
            listOf(false, true).forEach { value ->
                StyledCheckbox(
                    checked = value,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.semantics {
                        contentDescription = "Disabled checkbox: $value"
                    },
                )
            }
        }
        SubTitle("Checkboxes with Customization")
        StyledSpacedFlowRow{
            StyledCheckbox(
                checked = customChecked,
                tint = Color.Magenta,
                onTint = Color.White,
                modifier = Modifier.semantics { contentDescription = "Custom checkbox" },
            )
            StyledText("Custom tint")
        }
    }
}
