package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
@Suppress("UNUSED_PARAMETER")
actual fun edgeToEdgeDialogProperties(
    dismissOnBackPress: Boolean,
    transparentScrim: Boolean,
): DialogProperties =
    DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false,
    )
