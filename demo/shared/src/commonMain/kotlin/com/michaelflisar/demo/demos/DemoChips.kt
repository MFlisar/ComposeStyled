package com.michaelflisar.demo.demos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledChip
import com.michaelflisar.composestyledcomponents.ui.components.StyledChipDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledSelectableChip
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoChips(
    baseId: Int,
    expandState: ExpandState,
) {
    val selected = remember { mutableStateOf(true) }

    RegionContainer(
        title = "Chips",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Clickable")
        StyledSpacedFlowRow {
            StyledChip(
                onClick = {},
                leadingIcon = Icons.Default.Add.toIconComposable(),
            ) {
                StyledText("Chip")
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
        }

        SubTitle("Selectable")
        StyledSpacedFlowRow {
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
        }
    }
}
