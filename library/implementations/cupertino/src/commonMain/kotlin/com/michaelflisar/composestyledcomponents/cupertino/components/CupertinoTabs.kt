package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationTabRow
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationTab
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledTabRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledTab

@OptIn(InternalComposeStyledApi::class)
class CupertinoTabRow internal constructor(edgePadding: Dp) :
    StyledTabRow.Implementation by FoundationTabRow(
        scrollableEdgePadding = edgePadding,
        indicatorAnimationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
    ) {

    companion object {
        @Composable
        fun create(): CupertinoTabRow {
            val edgePadding = StyledTheme.padding.large
            return remember(edgePadding) { CupertinoTabRow(edgePadding) }
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
class CupertinoTab internal constructor() : StyledTab.Implementation by FoundationTab() {

    companion object {
        @Composable
        fun create() = remember { CupertinoTab() }
    }
}
