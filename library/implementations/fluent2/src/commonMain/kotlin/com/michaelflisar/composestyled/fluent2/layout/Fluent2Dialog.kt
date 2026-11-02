package com.michaelflisar.composestyled.fluent2.layout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.foundation.layout.FoundationDialog
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledDialog

@OptIn(InternalComposeStyledApi::class)
class Fluent2Dialog internal constructor() : FoundationDialog() {

    override val defaults: StyledDialog.Defaults
        @Composable get() = StyledDialog.Defaults(
            shape = RoundedCornerShape(8.dp),
            sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            maxWidth = 560.dp,
            edgeMargin = if (StyledTheme.platform.isDesktopOrWeb()) 24.dp else 16.dp,
        )

    override val verticalAlertActions = false
    override val fullscreenActionsInToolbar = false
    override val positiveActionFirst = true
    override val alertPadding
        @Composable get() = 24.dp
    override val alertTitleSpacing
        @Composable get() = 8.dp
    override val alertActionSpacing
        @Composable get() = 8.dp
    override val actionSpacing
        @Composable get() = 8.dp

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): Fluent2Dialog = remember { Fluent2Dialog() }
    }
}
