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
import com.michaelflisar.composestyled.ui.components.StyledSwitch
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoSwitches(baseId: Int, expandState: ExpandState) {
    val switched = rememberSaveable { mutableStateOf(true) }
    val fullItemSwitched = rememberSaveable { mutableStateOf(false) }
    val customSwitched = rememberSaveable { mutableStateOf(false) }
    RegionContainer(
        title = "Switches",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Switches")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledSwitch(
                checked = switched.value,
                onCheckedChange = { switched.value = it },
                modifier = Modifier.semantics { contentDescription = "Default switch" },
            )
            StyledSwitch(
                checked = fullItemSwitched.value,
                onCheckedChange = { fullItemSwitched.value = it },
                clickBehavior = StyledToggleBehavior.FullItem,
                label = { StyledText("Full item click") },
            )
            StyledText(
                "Checked: ${switched.value}; full item: ${fullItemSwitched.value}"
            )
        }
        SubTitle("Disabled Switches")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
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
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
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
