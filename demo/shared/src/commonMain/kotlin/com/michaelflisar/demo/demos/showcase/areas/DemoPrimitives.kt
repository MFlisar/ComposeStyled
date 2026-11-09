package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.components.StyledCheckbox
import com.michaelflisar.composestyled.ui.components.StyledSlider
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalSpacer
import com.michaelflisar.composestyled.ui.primitives.StyledInputAction
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledVerticalSpacer
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoPrimitives(
    baseId: Int,
    expandState: ExpandState,
    showMessage: (String) -> Unit,
) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val progress = rememberSaveable { mutableStateOf(0f) }
    val rtl = rememberSaveable { mutableStateOf(false) }

    RegionContainer(
        title = "Text, spacing and input actions",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Styled text")
        StyledText("Text inherits the current theme style and content color.")
        StyledText(
            text = "Text can also set explicit emphasis and color.",
            color = StyledTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )

        SubTitle("Spacers")
        StyledText("Content before a horizontal spacer")
        StyledHorizontalSpacer(size = 12.dp)
        StyledText("Content after a horizontal spacer")
        StyledRow(itemSpacing = StyledTheme.spacing.medium, verticalAlignment = Alignment.CenterVertically) {
            StyledText("Before vertical spacer")
            StyledVerticalSpacer(size = 24.dp)
            StyledText("After vertical spacer")
        }

        SubTitle("Input actions and expand icon")
        StyledInputAction(
            icon = Icons.Default.Add.toIconComposable(),
            onClick = { showMessage("Input action clicked") },
            contentDescription = "Add",
        )
        StyledRow(itemSpacing = StyledTheme.spacing.medium, verticalAlignment = Alignment.CenterVertically) {
            StyledText("Toggle details")
            StyledInputAction(
                onClick = { expanded.value = !expanded.value },
            ) {
                StyledExpandIcon(expanded = expanded.value)
            }
        }
        if (expanded.value) {
            StyledText("The expand icon reflects the current state.")
        }

        SubTitle("Expand directions")
        StyledCheckbox(checked = rtl, label = { StyledText("Right-to-left icon directions") })
        CompositionLocalProvider(
            LocalLayoutDirection provides if (rtl.value) LayoutDirection.Rtl else LayoutDirection.Ltr,
        ) {
            StyledRow(itemSpacing = StyledTheme.spacing.medium, verticalAlignment = Alignment.CenterVertically) {
                for (direction in StyledExpandIcon.ExpandDirection.entries) {
                    StyledText(direction.name)
                    StyledExpandIcon(
                        expanded = expanded.value,
                        expandDirection = direction,
                    )
                }
            }
        }

        SubTitle("Replace icons")
        StyledRow(itemSpacing = StyledTheme.spacing.medium, verticalAlignment = Alignment.CenterVertically) {
            StyledText("Immediate")
            StyledExpandIcon(
                expanded = expanded.value,
                iconMode = StyledExpandIcon.IconMode.Replacing(
                    collapsedIcon = Icons.Default.Add.toIconComposable(),
                    expandedIcon = Icons.Default.Remove.toIconComposable(),
                ),
            )
            StyledText("Crossfade")
            StyledExpandIcon(
                expanded = expanded.value,
                iconMode = StyledExpandIcon.IconMode.Replacing(
                    collapsedIcon = Icons.Default.Add.toIconComposable(),
                    expandedIcon = Icons.Default.Remove.toIconComposable(),
                    crossfade = true,
                ),
            )
        }

        SubTitle("Direct rotation progress")
        StyledText("Progress: ${(progress.value * 100).toInt()}%")
        StyledSlider(value = progress, valueRange = 0f..1f)
        StyledRow(itemSpacing = StyledTheme.spacing.medium, verticalAlignment = Alignment.CenterVertically) {
            StyledText("Counterclockwise")
            StyledExpandIcon(expansionProgress = progress.value)
            StyledText("Clockwise")
            StyledExpandIcon(
                expansionProgress = progress.value,
                rotationDirection = StyledExpandIcon.RotationDirection.Clockwise,
            )
        }
    }
}
