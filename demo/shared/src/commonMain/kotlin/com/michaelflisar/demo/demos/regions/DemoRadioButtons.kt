package com.michaelflisar.demo.demos.regions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledRadioButton
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoRadioButtons(baseId: Int, expandState: ExpandState) {
    val selectedOption = rememberSaveable { mutableStateOf(0) }
    RegionContainer(
        title = "Radio buttons",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Default Radio Buttons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            listOf("First", "Second").forEachIndexed { index, label ->
                StyledRadioButton(
                    selected = selectedOption.value == index,
                    onClick = { selectedOption.value = index },
                    modifier = Modifier.semantics {
                        contentDescription = "$label radio button"
                    },
                    label = { StyledText(label) },
                )
            }
            StyledRadioButton(
                selected = selectedOption.value == 2,
                onClick = { selectedOption.value = 2 },
                modifier = Modifier.semantics {
                    contentDescription = "Radio button without label"
                },
            )
            StyledRadioButton(
                selected = selectedOption.value == 3,
                onClick = { selectedOption.value = 3 },
                clickBehavior = StyledToggleBehavior.FullItem,
                label = { StyledText("Full item click") },
            )
            StyledText("Selected: ${selectedOption.value + 1}")
        }
        SubTitle("Disabled Radio Buttons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            listOf(false, true).forEach { value ->
                StyledRadioButton(
                    selected = value,
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.semantics {
                        contentDescription = "Disabled radio button: $value"
                    },
                    label = { StyledText("Disabled: $value") },
                )
            }
        }
        SubTitle("Radio Button with Customization")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledRadioButton(
                selected = true,
                onClick = {},
                tint = Color.Magenta,
                modifier = Modifier.semantics { contentDescription = "Custom radio button" },
                label = { StyledText("Custom tint") },
            )
        }
    }
}
