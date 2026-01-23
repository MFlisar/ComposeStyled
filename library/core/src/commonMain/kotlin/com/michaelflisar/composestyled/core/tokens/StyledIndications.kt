package com.michaelflisar.composestyled.core.tokens

import androidx.compose.foundation.Indication
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.isDark

@Immutable
data class StyledIndications(
    val onDark: Indication = IndicationNone,
    val onLight: Indication = IndicationNone,
) {
    fun select(backgroundColor: Color) = if (backgroundColor.isDark) onDark else onLight
    fun select(onDark: Boolean) = if (onDark) this.onDark else onLight
}

internal val LocalStyledIndications = compositionLocalOf { StyledIndications() }

private object IndicationNone : Indication {
    override fun equals(other: Any?): Boolean {
        return other is IndicationNone
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}