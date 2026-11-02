package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
expect fun edgeToEdgeDialogProperties(dismissOnBackPress: Boolean): DialogProperties
