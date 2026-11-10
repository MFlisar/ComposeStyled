package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationTabRow
import com.michaelflisar.composestyled.foundation.components.FoundationTab
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledTabRow
import com.michaelflisar.composestyled.ui.components.StyledTab

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatTabRow internal constructor() :
    StyledTabRow.Implementation by FoundationTabRow(
        indicatorAnimationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
    ) {

    companion object {
        @Composable
        fun create() = remember { CupertinoFlatTabRow() }
    }
}

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatTab internal constructor() : StyledTab.Implementation by FoundationTab() {

    companion object {
        @Composable
        fun create() = remember { CupertinoFlatTab() }
    }
}
