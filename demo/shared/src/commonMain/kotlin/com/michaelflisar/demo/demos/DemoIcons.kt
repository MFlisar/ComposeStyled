package com.michaelflisar.demo.demos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.ExpandState

@Composable
internal fun DemoIcons(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Icons",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Icons")
        StyledSpacedFlowRow {
            StyledIcon(icon = Icons.Default.Check.toIconComposable(), contentDescription = "Check")
            StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = "Add")
        }
        SubTitle("Icons with Customization")
        StyledSpacedFlowRow {
            StyledIcon(icon = Icons.Default.Check.toIconComposable(), tint = Color.Magenta, size = 32.dp, contentDescription = "Custom check")
            StyledIcon(icon = Icons.Default.Add.toIconComposable(), tint = Color.Blue, size = 48.dp, contentDescription = "Custom add")
        }
    }
}
