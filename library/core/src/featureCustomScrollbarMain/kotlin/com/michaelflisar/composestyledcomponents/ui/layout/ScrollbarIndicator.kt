package com.michaelflisar.composestyledcomponents.ui.layout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledContentColor

@Composable
internal actual fun ScrollbarIndicator(
    modifier: Modifier,
    scrollState: ScrollState,
    orientation: Orientation,
) {
    var trackLength by remember { mutableIntStateOf(0) }
    val minThumbSize = with(LocalDensity.current) { 24.dp.toPx() }
    val color = LocalStyledContentColor.current.copy(alpha = 0.5f)
    val draggableState = rememberDraggableState { delta ->
        val maxValue = scrollState.maxValue.toFloat()
        val viewportSize = scrollState.viewportSize.toFloat()
        val trackSize = trackLength.toFloat()
        if (maxValue > 0f && viewportSize > 0f && trackSize > 0f) {
            val thumbSize = scrollbarThumbSize(trackSize, viewportSize, maxValue, minThumbSize)
            val thumbTravel = trackSize - thumbSize
            if (thumbTravel > 0f) {
                scrollState.dispatchRawDelta(delta * maxValue / thumbTravel)
            }
        }
    }

    Canvas(
        modifier = modifier
            .onSizeChanged {
                trackLength = if (orientation == Orientation.Vertical) it.height else it.width
            }
            .draggable(
                orientation = orientation,
                state = draggableState,
            )
    ) {
        val trackSize = if (orientation == Orientation.Vertical) size.height else size.width
        val viewportSize = scrollState.viewportSize.toFloat()
        val maxValue = scrollState.maxValue.toFloat()
        if (trackSize > 0f && viewportSize > 0f && maxValue > 0f) {
            val thumbSize = scrollbarThumbSize(trackSize, viewportSize, maxValue, minThumbSize)
            val thumbTravel = trackSize - thumbSize
            val thumbOffset = thumbTravel * (scrollState.value.toFloat() / maxValue)
            if (orientation == Orientation.Vertical) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(0f, thumbOffset),
                    size = Size(size.width, thumbSize),
                    cornerRadius = CornerRadius(size.width / 2f),
                )
            } else {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(thumbOffset, 0f),
                    size = Size(thumbSize, size.height),
                    cornerRadius = CornerRadius(size.height / 2f),
                )
            }
        }
    }
}

private fun scrollbarThumbSize(
    trackSize: Float,
    viewportSize: Float,
    maxValue: Float,
    minThumbSize: Float,
): Float {
    val contentSize = viewportSize + maxValue
    return (trackSize * viewportSize / contentSize)
        .coerceAtLeast(minThumbSize)
        .coerceAtMost(trackSize)
}
