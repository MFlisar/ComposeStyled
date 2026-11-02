package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationTabRow
import com.michaelflisar.composestyled.foundation.components.FoundationTab
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledTabRow
import com.michaelflisar.composestyled.ui.components.StyledTab

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatTabRow internal constructor(edgePadding: Dp) :
    StyledTabRow.Implementation by FoundationTabRow(
        scrollableEdgePadding = edgePadding,
        indicatorAnimationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
    ) {

    companion object {
        @Composable
        fun create(): CupertinoFlatTabRow {
            val edgePadding = StyledTheme.padding.large
            return remember(edgePadding) { CupertinoFlatTabRow(edgePadding) }
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatTab internal constructor() : StyledTab.Implementation by FoundationTab() {

    companion object {
        @Composable
        fun create() = remember { CupertinoFlatTab() }
    }
}
