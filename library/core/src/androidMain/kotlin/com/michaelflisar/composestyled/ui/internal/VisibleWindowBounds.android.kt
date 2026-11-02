package com.michaelflisar.composestyled.ui.internal

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntRect

@Composable
internal actual fun rememberVisibleWindowBoundsOnScreen(): IntRect? {
    val view = LocalView.current
    val visibleBoundsOnScreen = remember(view) { mutableStateOf<IntRect?>(null) }
    DisposableEffect(view) {
        val rootView = view.rootView
        val observer = rootView.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val visibleFrame = Rect()
            rootView.getWindowVisibleDisplayFrame(visibleFrame)
            visibleBoundsOnScreen.value =
                if (visibleFrame.isEmpty) {
                    null
                } else {
                    IntRect(
                        left = visibleFrame.left,
                        top = visibleFrame.top,
                        right = visibleFrame.right,
                        bottom = visibleFrame.bottom,
                    )
                }
        }
        observer.addOnGlobalLayoutListener(listener)
        listener.onGlobalLayout()
        onDispose {
            if (observer.isAlive) {
                observer.removeOnGlobalLayoutListener(listener)
            }
        }
    }
    return visibleBoundsOnScreen.value
}
