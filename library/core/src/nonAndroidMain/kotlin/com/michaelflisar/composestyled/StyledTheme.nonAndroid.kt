package com.michaelflisar.composestyled

actual val StyledTheme.isDynamicColorsSupported: Boolean
    get() = false

actual val StyledTheme.isSystemContrastSupported: Boolean
    get() = false
