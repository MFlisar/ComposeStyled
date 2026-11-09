package com.michaelflisar.composestyled.cupertino.liquid.layout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.foundation.layout.FoundationDialog
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledDialog

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidDialog internal constructor(
    override val presentationMapping: PresentationMapping,
) : FoundationDialog() {

    override val defaults: StyledDialog.Defaults
        @Composable get() = StyledDialog.Defaults(
            shape = RoundedCornerShape(22.dp),
            sheetShape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            maxWidth = 320.dp,
            edgeMargin = if (StyledTheme.platform.isDesktopOrWeb()) 24.dp else 16.dp,
        )

    override val verticalAlertActions = true
    override val fullscreenActionsInToolbar = true
    override val centeredTitle = true

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            presentationMapping: PresentationMapping = DefaultPresentationMapping,
        ): CupertinoLiquidDialog = remember(presentationMapping) { CupertinoLiquidDialog(presentationMapping) }

        // -------------
        // 2) mapping
        // -------------

        /** Resolves Auto for actual toolbar actions only; missing icons fall back to text. */
        typealias PresentationMapping = (StyledDialog.ActionSlot) -> StyledDialog.ButtonPresentation

        val DefaultPresentationMapping: PresentationMapping = { StyledDialog.ButtonPresentation.Text }
    }
}
