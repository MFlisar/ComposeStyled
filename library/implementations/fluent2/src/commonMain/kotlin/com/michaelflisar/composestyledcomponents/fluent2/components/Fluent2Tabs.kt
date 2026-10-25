package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationTabRow
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationTab
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledTabRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledTab

@OptIn(InternalComposeStyledApi::class)
class Fluent2TabRow internal constructor(edgePadding: Dp) :
    StyledTabRow.Implementation by FoundationTabRow(
        scrollableEdgePadding = edgePadding,
        indicatorAnimationSpec = tween(durationMillis = 83),
    ) {

    companion object {
        @Composable
        fun create(): Fluent2TabRow {
            val edgePadding = 0.dp
            return remember(edgePadding) { Fluent2TabRow(edgePadding) }
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
class Fluent2Tab internal constructor() : StyledTab.Implementation by FoundationTab() {

    companion object {
        @Composable
        fun create() = remember { Fluent2Tab() }
    }
}
