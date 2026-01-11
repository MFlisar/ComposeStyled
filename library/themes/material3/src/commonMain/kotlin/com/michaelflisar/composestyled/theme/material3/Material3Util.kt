package com.michaelflisar.composestyled.theme.material3

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.classes.Emphasis

internal object Material3Util {

    fun hoverFromForeground(color: Color) = color.copy(color.alpha * .12f)
    fun hoverFromBackground(color: Color) = color.copy(color.alpha * .88f)

    fun emphasizedColor(
        base: Color,
        surfaceTint: Color,
        emphasis: Emphasis
    ): Color {
        val alpha = when (emphasis) {
            Emphasis.Low -> 0.05f
            Emphasis.Medium -> 0.08f
            Emphasis.High -> 0.12f
        }
        return surfaceTint.copy(alpha = alpha).compositeOver(base)
    }

    fun emphasisElevation(emphasis: Emphasis): Dp {
        return when (emphasis) {
            Emphasis.Low -> 0.dp
            Emphasis.Medium -> 4.dp
            Emphasis.High -> 8.dp
        }
    }

}