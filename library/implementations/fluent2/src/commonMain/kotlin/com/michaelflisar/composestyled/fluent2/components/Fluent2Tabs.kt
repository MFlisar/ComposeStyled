package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationTabRow
import com.michaelflisar.composestyled.foundation.components.FoundationTab
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledTabRow
import com.michaelflisar.composestyled.ui.components.StyledTab

@OptIn(InternalComposeStyledApi::class)
class Fluent2TabRow internal constructor() :
    StyledTabRow.Implementation by FoundationTabRow(
        indicatorAnimationSpec = tween(durationMillis = 83),
    ) {

    companion object {
        @Composable
        fun create() = remember { Fluent2TabRow() }
    }
}

@OptIn(InternalComposeStyledApi::class)
class Fluent2Tab internal constructor() : StyledTab.Implementation by FoundationTab() {

    companion object {
        @Composable
        fun create() = remember { Fluent2Tab() }
    }
}
