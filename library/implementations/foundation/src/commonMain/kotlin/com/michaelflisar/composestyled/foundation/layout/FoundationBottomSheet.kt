package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.layout.StyledBottomSheet
import com.michaelflisar.composestyled.ui.layout.StyledBottomSheetDefaults
import com.michaelflisar.composestyled.ui.layout.StyledDialog

@InternalComposeStyledApi
abstract class FoundationBottomSheet : StyledBottomSheet.Implementation {
    protected abstract val verticalActions: Boolean
    protected open val fullscreenActionsInToolbar: Boolean = true
    protected open val centeredTitle: Boolean = false
    protected open val positiveActionFirst: Boolean = false
    protected open val roleMapping: (StyledButton.Role) -> StyledDialog.ButtonPresentation =
        { StyledDialog.ButtonPresentation.Text }
    protected open val actionSpacing: Dp
        @Composable get() = StyledTheme.spacing.small
    protected open val actionRowSpacing: Dp
        @Composable get() = actionSpacing
    protected open val stackPositiveActionFirst: Boolean = false
    /** Gap between scrolled sheet content and actions in a fixed footer. */
    protected open val footerTopPadding: Dp
        @Composable get() = StyledTheme.spacing.large
    protected abstract val containerColor: Color
        @Composable get

    @Composable
    final override fun Render(
        onDismissRequest: () -> Unit,
        onDismissRequestWithoutAnimation: () -> Unit,
        modifier: Modifier,
        properties: StyledDialog.Properties,
        style: StyledDialog.Style.BottomSheet,
        title: (@Composable () -> Unit)?,
        buttons: StyledDialog.Buttons,
        visible: Boolean,
        isVisible: () -> Boolean,
        content: @Composable ColumnScope.(PaddingValues) -> Unit,
    ) {
        val defaults = StyledBottomSheetDefaults.defaults
        val width = style.maxWidth ?: defaults.maxWidth
        val height = style.maxHeightFraction ?: defaults.maxHeightFraction
        val margin = style.edgeMargin ?: defaults.edgeMargin
        requireModalBounds(width, height, margin)
        FoundationDialogSheet(
            onDismissRequest, onDismissRequestWithoutAnimation,
            modifier, style.shape ?: defaults.shape, properties, style.options,
            containerColor, title, buttons, visible, isVisible, content, modal = false,
            verticalActions = verticalActions, positiveActionFirst = positiveActionFirst,
            fullscreenActionsInToolbar = fullscreenActionsInToolbar,
            centeredTitle = centeredTitle,
            roleMapping = roleMapping,
            actionSpacing = actionSpacing, actionRowSpacing = actionRowSpacing,
            stackPositiveActionFirst = stackPositiveActionFirst,
            footerTopPadding = footerTopPadding,
            maxWidth = width, maxHeightFraction = height, edgeMargin = margin,
        )
    }
}
