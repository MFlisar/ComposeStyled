package com.michaelflisar.demo.demos

import androidx.compose.runtime.Composable
import com.michaelflisar.demo.StyledCustomComponent
import com.michaelflisar.demo.helper.ExpandState
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
