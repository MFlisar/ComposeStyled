package com.michaelflisar.demo.demos.regions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.components.StyledCheckbox
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.composestyled.utils.ExpandState

@Composable
internal fun DemoCheckboxes(baseId: Int, expandState: ExpandState) {
    val checked = rememberSaveable { mutableStateOf(false) }
    val unlabeledChecked = rememberSaveable { mutableStateOf(false) }
    val controlOnlyChecked = rememberSaveable { mutableStateOf(false) }
    val customChecked = rememberSaveable { mutableStateOf(true) }
    RegionContainer(
        title = "Checkboxes",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Checkboxes")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledCheckbox(
                checked = checked,
                modifier = Modifier.semantics { contentDescription = "Default checkbox" },
                label = { StyledText("Full item click (default)") },
            )
            StyledCheckbox(
                checked = unlabeledChecked,
                modifier = Modifier.semantics { contentDescription = "Checkbox without label" },
            )
            StyledCheckbox(
                checked = controlOnlyChecked,
                clickBehavior = StyledToggleBehavior.ControlOnly,
                label = { StyledText("Control only") },
            )
            StyledText("Checked: ${checked.value}; control only: ${controlOnlyChecked.value}")
        }
        SubTitle("Disabled Checkboxes")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            listOf(false, true).forEach { value ->
                StyledCheckbox(
                    checked = value,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.semantics {
                        contentDescription = "Disabled checkbox: $value"
                    },
                    label = { StyledText("Disabled: $value") },
                )
            }
        }
        SubTitle("Checkboxes with Customization")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledCheckbox(
                checked = customChecked,
                tint = Color.Magenta,
                onTint = Color.White,
                modifier = Modifier.semantics { contentDescription = "Custom checkbox" },
                label = { StyledText("Custom tint") },
            )
        }
    }
}
