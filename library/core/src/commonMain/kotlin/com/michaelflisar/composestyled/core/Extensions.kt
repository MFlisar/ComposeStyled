package com.michaelflisar.composestyled.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

val Color.isDark
    get() = luminance() < 0.5

val Color.focusColor
    get() = if (isDark) Color.White else Color.White