package com.michaelflisar.demo.demos.regions

import androidx.compose.runtime.Composable
import com.michaelflisar.demo.custom.StyledCustomComponent
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer

@Composable
internal fun DemoCustom(
    baseId: Int,
    expandState: ExpandState,
) {
    RegionContainer(
        title = "Custom component",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledCustomComponent()
    }
}
