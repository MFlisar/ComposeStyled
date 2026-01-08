package com.michaelflisar.composestyled.core.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.tokens.LocalStyledColors

@Immutable
data class ColorDef(
    val color: Color,
    val onColor: Color
) {
    @Composable
    @ReadOnlyComposable
    fun color(
        enabled: Boolean,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ): Color =
        if (enabled) color else color.copy(
            alpha = LocalStyledColors.current.disableFactors.get(
                disableFactorType
            )
        )

    @Composable
    @ReadOnlyComposable
    fun onColor(
        enabled: Boolean,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ): Color =
        if (enabled) onColor else onColor.copy(
            alpha = LocalStyledColors.current.disableFactors.get(
                disableFactorType
            )
        )
}