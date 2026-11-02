package com.michaelflisar.composestyled.fluent2.layout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.layout.FoundationBottomSheet
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledBottomSheet

@OptIn(InternalComposeStyledApi::class)
class Fluent2BottomSheet internal constructor() : FoundationBottomSheet() {
    override val defaults: StyledBottomSheet.Defaults
        @Composable get() = StyledBottomSheet.Defaults(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            edgeMargin = if (StyledTheme.platform.isDesktopOrWeb()) 24.dp else 16.dp,
        )
    override val verticalActions = false
    override val fullscreenActionsInToolbar = false
    override val positiveActionFirst = true
    override val actionSpacing
        @Composable get() = 8.dp
    override val containerColor
        @Composable get() = StyledTheme.colorScheme.surface

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): Fluent2BottomSheet = remember { Fluent2BottomSheet() }
    }
}
