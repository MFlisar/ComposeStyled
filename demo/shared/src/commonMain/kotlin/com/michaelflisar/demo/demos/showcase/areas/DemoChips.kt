package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledChip
import com.michaelflisar.composestyled.ui.components.StyledChipDefaults
import com.michaelflisar.composestyled.ui.components.StyledOutlinedChip
import com.michaelflisar.composestyled.ui.components.StyledOutlinedSelectableChip
import com.michaelflisar.composestyled.ui.components.StyledSelectableChip
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoChips(
    baseId: Int,
    expandState: ExpandState,
) {
    val selected = rememberSaveable { mutableStateOf(true) }

    RegionContainer(
        title = "Chips",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Clickable")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledChip(
                onClick = {},
                leadingIcon = Icons.Default.Add.toIconComposable(),
            ) {
                StyledText("Chip")
            }
            StyledChip(
                onClick = {},
                variant = StyledChip.Variant.Filled,
            ) {
                StyledText("Filled chip")
            }
            StyledChip(
                onClick = {},
                leadingIcon = Icons.Default.Add.toIconComposable(),
                customization = StyledChipDefaults.colors(
                    container = StyledTheme.colorScheme.secondarySurface,
                    content = StyledTheme.colorScheme.onSurface,
                    border = StyledTheme.colorScheme.outline,
                    //selectedContainer = StyledTheme.colorScheme.primarySurface,
                    //selectedContent = StyledTheme.colorScheme.onPrimarySurface,
                    //selectedBorder = StyledTheme.colorScheme.primary,
                ),
            ) {
                StyledText("Customized chip")
            }
            StyledOutlinedChip(
                onClick = {},
                leadingIcon = Icons.Default.Add.toIconComposable(),
            ) {
                StyledText("Outlined chip")
            }
        }

        SubTitle("Selectable")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledSelectableChip(
                onClick = {},
                selected = false,
            ) {
                StyledText("Unselected chip")
            }
            StyledSelectableChip(
                onClick = { selected.value = !selected.value },
                selected = selected.value,
                leadingIcon = Icons.Default.Add.toIconComposable(),
            ) {
                StyledText("Selectable chip")
            }
            StyledSelectableChip(
                onClick = { selected.value = !selected.value },
                selected = selected.value,
                variant = StyledChip.Variant.Filled,
            ) {
                StyledText("Filled selectable")
            }
            StyledSelectableChip(
                onClick = {},
                selected = false,
                variant = StyledChip.Variant.Filled,
            ) {
                StyledText("Unselected filled")
            }
            StyledSelectableChip(
                onClick = { selected.value = !selected.value },
                selected = selected.value,
                leadingIcon = Icons.Default.Add.toIconComposable(),
                trailingIcon = Icons.Default.Close.toIconComposable(),
                customization = StyledChipDefaults.colors(
                    //container = StyledTheme.colorScheme.secondarySurface,
                    //content = StyledTheme.colorScheme.onSurface,
                    //border = StyledTheme.colorScheme.outline,
                    selectedContainer = StyledTheme.colorScheme.primarySurface,
                    selectedContent = StyledTheme.colorScheme.onPrimarySurface,
                    selectedBorder = StyledTheme.colorScheme.primary,
                ),
            ) {
                StyledText("Customized selectable")
            }
            StyledOutlinedSelectableChip(
                onClick = { selected.value = !selected.value },
                selected = selected.value,
            ) {
                StyledText("Outlined selectable")
            }
            StyledOutlinedSelectableChip(
                onClick = {},
                selected = false,
            ) {
                StyledText("Unselected outlined")
            }
        }
    }
}
