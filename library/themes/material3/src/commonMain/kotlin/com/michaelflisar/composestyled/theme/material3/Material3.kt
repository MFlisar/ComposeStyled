package com.michaelflisar.composestyled.theme.material3

import androidx.compose.ui.graphics.Color

internal object Material3 {

    fun hoverFromForeground(color: Color) = color.copy(color.alpha * .12f)
    fun hoverFromBackground(color: Color) = color.copy(color.alpha * .88f)
}