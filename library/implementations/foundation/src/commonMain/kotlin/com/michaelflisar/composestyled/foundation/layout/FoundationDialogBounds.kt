package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
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
    fullscreenProgress: Float? = null,
    onFullscreenTravelChanged: ((Float) -> Unit)? = null,
    content: @Composable (Modifier) -> Unit,
) {
    if (fullscreenProgress != null) {
        ProgressiveDialogBounds(
            maxWidth,
            maxHeightFraction,
            edgeMargin,
            properties,
            onDismissRequest,
            fullscreenProgress,
            onFullscreenTravelChanged,
            content,
        )
        return
    }
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

@Composable
private fun ProgressiveDialogBounds(
    maxWidth: Dp,
    maxHeightFraction: Float,
    edgeMargin: Dp,
    properties: StyledDialog.Properties,
    onDismissRequest: () -> Unit,
    fullscreenProgress: Float,
    onFullscreenTravelChanged: ((Float) -> Unit)?,
    content: @Composable (Modifier) -> Unit,
) {
    val progress = fullscreenProgress.coerceIn(0f, 1f)
    BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val density = LocalDensity.current
        val layoutDirection = LocalLayoutDirection.current
        val safeInsets = WindowInsets.safeDrawing
        val safeStart = with(density) {
            safeInsets.getLeft(this, layoutDirection).toDp()
        }
        val safeTop = with(density) { safeInsets.getTop(this).toDp() }
        val safeEnd = with(density) {
            safeInsets.getRight(this, layoutDirection).toDp()
        }
        val safeBottom = with(density) { safeInsets.getBottom(this).toDp() }
        val imeBottom = with(density) { WindowInsets.ime.getBottom(this).toDp() }
        val boundedBottomInset = maxOf(safeBottom, imeBottom)
        val safeWidth = (maxWidth - safeStart - safeEnd).coerceAtLeast(0.dp)
        val safeHeight = (maxHeight - safeTop - boundedBottomInset).coerceAtLeast(0.dp)
        val boundedInnerHeight = (safeHeight - edgeMargin * 2f).coerceAtLeast(0.dp)
        val boundedWidth = minOf(
            maxWidth,
            (safeWidth - edgeMargin * 2f).coerceAtLeast(0.dp),
        )
        val boundedHeight = minOf(
            boundedInnerHeight,
            safeHeight * maxHeightFraction,
        )
        val fullHeight = (maxHeight - imeBottom).coerceAtLeast(0.dp)
        val contentWidth = interpolateDp(boundedWidth, maxWidth, progress)
        val contentHeight = interpolateDp(boundedHeight, fullHeight, progress)
        val startPadding = interpolateDp(safeStart + edgeMargin, 0.dp, progress)
        val topPadding = interpolateDp(safeTop + edgeMargin, 0.dp, progress)
        val endPadding = interpolateDp(safeEnd + edgeMargin, 0.dp, progress)
        val bottomPadding = interpolateDp(boundedBottomInset + edgeMargin, imeBottom, progress)
        val dragTravel = with(density) {
            (
                safeTop + edgeMargin +
                    (boundedInnerHeight - boundedHeight) / 2f
                ).toPx()
        }.coerceAtLeast(1f)
        SideEffect {
            onFullscreenTravelChanged?.invoke(dragTravel)
        }
        Box(
            Modifier.fillMaxSize().clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = progress < 1f && properties.dismissOnClickOutside,
            ) { onDismissRequest() },
        )
        BoxWithConstraints(
            Modifier.padding(
                start = startPadding,
                top = topPadding,
                end = endPadding,
                bottom = bottomPadding,
            ),
            contentAlignment = Alignment.Center,
        ) {
            content(
                Modifier.width(contentWidth)
                    .heightIn(max = contentHeight),
            )
        }
    }
}

private fun interpolateDp(start: Dp, end: Dp, fraction: Float): Dp =
    start + (end - start) * fraction

internal fun requireModalBounds(maxWidth: Dp, maxHeightFraction: Float, edgeMargin: Dp) {
    require(maxWidth > 0.dp && !maxWidth.value.isNaN()) { "maxWidth must be positive" }
    require(maxHeightFraction > 0f && maxHeightFraction <= 1f) { "maxHeightFraction must be in (0, 1]" }
    require(edgeMargin.value.isFinite() && edgeMargin >= 0.dp) { "edgeMargin must be finite and non-negative" }
}
