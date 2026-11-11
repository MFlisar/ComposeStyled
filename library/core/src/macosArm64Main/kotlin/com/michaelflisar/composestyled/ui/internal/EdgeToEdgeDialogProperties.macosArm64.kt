package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
@OptIn(ExperimentalComposeUiApi::class)
actual fun edgeToEdgeDialogProperties(
    dismissOnBackPress: Boolean,
    transparentScrim: Boolean,
): DialogProperties {
    val properties = DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false,
    )
    return if (transparentScrim) {
        DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            usePlatformDefaultWidth = properties.usePlatformDefaultWidth,
            scrimColor = Color.Transparent,
        )
    } else {
        properties
    }
}
