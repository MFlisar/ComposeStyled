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
import com.michaelflisar.composestyledcomponents.ui.components.StyledSwitch
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoSwitches(baseId: Int, expandState: ExpandState) {
    val switched = remember { mutableStateOf(true) }
    val customSwitched = remember { mutableStateOf(false) }
    RegionContainer(
        title = "Switches",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Switches")
        StyledSpacedFlowRow {
            StyledSwitch(
                checked = switched.value,
                onCheckedChange = { switched.value = it },
                modifier = Modifier.semantics { contentDescription = "Default switch" },
            )
            StyledText("Checked: ${switched.value}")
        }
        SubTitle("Disabled Switches")
        StyledSpacedFlowRow {
            listOf(false, true).forEach { value ->
                StyledSwitch(
                    checked = value,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.semantics {
                        contentDescription = "Disabled switch: $value"
                    },
                )
            }
        }
        SubTitle("Switches with Customization")
        StyledSpacedFlowRow {
            StyledSwitch(
                checked = customSwitched.value,
                onCheckedChange = { customSwitched.value = it },
                tint = Color.Magenta,
                onTint = Color.White,
                modifier = Modifier.semantics { contentDescription = "Custom switch" },
            )
            StyledText("Custom tint")
        }
    }
}
