package com.michaelflisar.demo.demos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledSurface
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.ExpandState

@Composable
internal fun DemoSurfaces(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Surfaces",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Surfaces")
        StyledSurface(modifier = Modifier.fillMaxWidth()) {
            StyledText("Default surface", Modifier.padding(StyledTheme.padding.content))
        }
        SubTitle("Surfaces with Customization")
        StyledSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = StyledTheme.shapes.medium,
            border = BorderStroke(1.dp, StyledTheme.colorScheme.outline),
        ) {
            StyledText("Outlined surface", Modifier.padding(StyledTheme.padding.content))
        }
        StyledSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = StyledTheme.shapes.medium,
            color = StyledTheme.colorScheme.primarySurface,
            shadowElevation = 4.dp,
        ) {
            StyledText("Elevated colored surface", Modifier.padding(StyledTheme.padding.content))
        }
    }
}
