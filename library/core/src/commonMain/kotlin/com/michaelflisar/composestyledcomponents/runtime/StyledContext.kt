package com.michaelflisar.composestyledcomponents.runtime

import androidx.compose.runtime.staticCompositionLocalOf

interface StyledContext {
    object Default : StyledContext
}

@InternalComposeStyledApi
val LocalStyledContext = staticCompositionLocalOf<StyledContext> { StyledContext.Default }
