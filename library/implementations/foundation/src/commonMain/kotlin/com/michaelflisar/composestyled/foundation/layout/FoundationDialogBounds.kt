package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledDialog

/** Measures against the dialog window, never against the launching composable. */
@InternalComposeStyledApi
@Composable
fun FoundationDialogBounds(
    maxWidth: Dp,
    maxHeightFraction: Float,
    edgeMargin: Dp,
    properties: StyledDialog.Properties,
    onDismissRequest: () -> Unit,
    fullscreen: Boolean = false,
    content: @Composable (Modifier) -> Unit,
) {
    BoxWithConstraints(
        Modifier.fillMaxSize()
            .then(if (fullscreen) Modifier else Modifier.safeDrawingPadding())
            .imePadding(),
        contentAlignment = Alignment.Center,
    ) viewport@{
        Box(
            Modifier.fillMaxSize().clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !fullscreen && properties.dismissOnClickOutside,
            ) { onDismissRequest() },
        )
        BoxWithConstraints(
            Modifier.padding(if (fullscreen) 0.dp else edgeMargin),
            contentAlignment = Alignment.Center,
        ) {
            content(
                if (fullscreen) Modifier.fillMaxSize() else {
                    Modifier.width(minOf(maxWidth, this.maxWidth))
                        .heightIn(max = minOf(maxHeight, this@viewport.maxHeight * maxHeightFraction))
                },
            )
        }
    }
}

internal fun requireModalBounds(maxWidth: Dp, maxHeightFraction: Float, edgeMargin: Dp) {
    require(maxWidth > 0.dp && !maxWidth.value.isNaN()) { "maxWidth must be positive" }
    require(maxHeightFraction > 0f && maxHeightFraction <= 1f) { "maxHeightFraction must be in (0, 1]" }
    require(edgeMargin.value.isFinite() && edgeMargin >= 0.dp) { "edgeMargin must be finite and non-negative" }
}
