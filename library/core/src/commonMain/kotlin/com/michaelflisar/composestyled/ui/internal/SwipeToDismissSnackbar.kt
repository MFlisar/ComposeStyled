package com.michaelflisar.composestyled.ui.internal

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import kotlin.math.abs

internal val LocalStyledSnackbarHostSwipeToDismissEnabled = compositionLocalOf { true }

@Composable
@InternalComposeStyledApi
fun Modifier.swipeToDismissSnackbar(
    enabled: Boolean = true,
    onDismiss: () -> Unit,
): Modifier {
    if (!enabled || !LocalStyledSnackbarHostSwipeToDismissEnabled.current) return this

    var offsetX by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var width by remember { mutableIntStateOf(0) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = if (isDragging) snap() else spring(),
        label = "SnackbarSwipeOffset",
    )
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    return this
        .onSizeChanged { width = it.width }
        .graphicsLayer { translationX = animatedOffsetX }
        .draggable(
            state = rememberDraggableState { delta ->
                isDragging = true
                offsetX = (offsetX + delta).coerceIn(-width.toFloat(), width.toFloat())
            },
            orientation = Orientation.Horizontal,
            onDragStopped = {
                isDragging = false
                if (abs(offsetX) >= width * 0.5f) {
                    currentOnDismiss()
                } else {
                    offsetX = 0f
                }
            },
        )
}
