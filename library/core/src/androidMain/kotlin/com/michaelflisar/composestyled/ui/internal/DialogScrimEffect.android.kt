package com.michaelflisar.composestyled.ui.internal

import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
@Composable
actual fun DialogScrimEffect(transparent: Boolean) {
    val view = LocalView.current
    val window = (view.parent as? DialogWindowProvider)?.window
    DisposableEffect(window, transparent) {
        if (window == null || !transparent) {
            onDispose {}
        } else {
            val originalAttributes = window.attributes
            val hadDimBehind = originalAttributes.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND != 0
            val originalDimAmount = originalAttributes.dimAmount
            window.setDimBehind(enabled = false, dimAmount = 0f)

            onDispose {
                window.setDimBehind(
                    enabled = hadDimBehind,
                    dimAmount = originalDimAmount,
                )
            }
        }
    }
}

private fun Window.setDimBehind(enabled: Boolean, dimAmount: Float) {
    val attributes = attributes
    attributes.flags = if (enabled) {
        attributes.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
    } else {
        attributes.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
    }
    attributes.dimAmount = dimAmount
    this.attributes = attributes
}
