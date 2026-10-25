package com.michaelflisar.demo.demos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.ui.components.StyledAlternativePicker
import com.michaelflisar.composestyledcomponents.ui.components.StyledPicker
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoPicker(
    baseId: Int,
    expandState: ExpandState,
    showMessage: (String) -> Unit,
) {
    RegionContainer(
        title = "Picker",
        id = baseId + 1,
        expanded = expandState,
    ) {
        val modifier = Modifier

        StyledSpacedColumn {
            SubTitle("Read-only value with a clickable end icon")
            StyledPicker(
                modifier = modifier,
                value = "Picker",
                onStartPicker = {
                    showMessage("Picker clicked")
                },
                icon = Icons.Default.FolderOpen.toIconComposable(),
                iconContentDescription = "Choose option",
                label = "Label"
            )
            StyledPicker(
                modifier = modifier,
                value = "Tap anywhere",
                onStartPicker = {
                    showMessage("Whole picker field clicked")
                },
                icon = Icons.Default.FolderOpen.toIconComposable(),
                label = "Whole field clickable",
                clickOnIconOnly = false,
            )
            StyledAlternativePicker(
                modifier = modifier,
                value = "Alternative style",
                onStartPicker = {
                    showMessage("Picker clicked, current selection: Alternative style")
                },
                icon = Icons.Default.FolderOpen.toIconComposable(),
            )
            StyledPicker(
                modifier = modifier,
                value = "Disabled picker",
                onStartPicker = {
                    showMessage("Disabled picker clicked")
                },
                icon = Icons.Default.FolderOpen.toIconComposable(),
                enabled = false,
            )
        }
    }
}
