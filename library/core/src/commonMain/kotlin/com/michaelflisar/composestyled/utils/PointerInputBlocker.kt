package com.michaelflisar.composestyled.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Blocks pointer input from reaching descendant handlers without consuming events, so ancestors
 * can still handle them.
 */
@Composable
fun PointerInputBlocker(
    blocked: Boolean = true,
    content: @Composable () -> Unit,
) {
    if (!blocked) {
        content()
        return
    }

    Box {
        content()
        Box(
            Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
        )
    }
}
