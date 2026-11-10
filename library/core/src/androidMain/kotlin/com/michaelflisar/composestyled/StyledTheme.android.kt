package com.michaelflisar.composestyled

import android.os.Build

actual val StyledTheme.isDynamicColorsSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

actual val StyledTheme.isSystemContrastSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
