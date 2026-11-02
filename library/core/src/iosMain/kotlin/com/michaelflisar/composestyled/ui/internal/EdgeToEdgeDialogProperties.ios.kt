package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
actual fun edgeToEdgeDialogProperties(dismissOnBackPress: Boolean): DialogProperties =
    DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false,
    )
