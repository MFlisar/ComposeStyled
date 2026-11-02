package com.michaelflisar.composestyled.runtime

import androidx.compose.runtime.staticCompositionLocalOf

/** Marker for contextual component roles resolved by a theme renderer. */
interface StyledContext {
    /** Context used when no more specific component context is provided. */
    object Default : StyledContext
}

@InternalComposeStyledApi
val LocalStyledContext = staticCompositionLocalOf<StyledContext> { StyledContext.Default }
