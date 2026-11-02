package com.michaelflisar.composestyled.runtime

import androidx.compose.runtime.staticCompositionLocalOf

interface StyledContext {
    object Default : StyledContext
}

@InternalComposeStyledApi
val LocalStyledContext = staticCompositionLocalOf<StyledContext> { StyledContext.Default }
