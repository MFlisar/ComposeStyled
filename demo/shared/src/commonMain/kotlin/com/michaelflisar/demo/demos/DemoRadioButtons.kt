package com.michaelflisar.demo.demos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.michaelflisar.composestyledcomponents.ui.components.StyledRadioButton
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoRadioButtons(baseId: Int, expandState: ExpandState) {
    val selectedOption = remember { mutableStateOf(0) }
    RegionContainer(
        title = "Radio buttons",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Default Radio Buttons")
        StyledSpacedFlowRow {
            listOf("First", "Second").forEachIndexed { index, label ->
                StyledRadioButton(
                    selected = selectedOption.value == index,
                    onClick = { selectedOption.value = index },
                    modifier = Modifier.semantics {
                        contentDescription = "$label radio button"
                    },
                )
                StyledText(label)
            }
            StyledText("Selected: ${selectedOption.value + 1}")
        }
        SubTitle("Disabled Radio Buttons")
        StyledSpacedFlowRow {
            listOf(false, true).forEach { value ->
                StyledRadioButton(
                    selected = value,
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.semantics {
                        contentDescription = "Disabled radio button: $value"
                    },
                )
            }
        }
        SubTitle("Radio Button with Customization")
        StyledSpacedFlowRow {
            StyledRadioButton(
                selected = true,
                onClick = {},
                tint = Color.Magenta,
                modifier = Modifier.semantics { contentDescription = "Custom radio button" },
            )
            StyledText("Custom tint")
        }
    }
}
