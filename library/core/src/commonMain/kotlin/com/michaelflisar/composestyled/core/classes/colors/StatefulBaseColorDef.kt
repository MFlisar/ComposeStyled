package com.michaelflisar.composestyled.core.classes.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.classes.StyledInteractionState
import com.michaelflisar.composestyled.core.classes.StyledResolveState

// Verantwortung: kombiniert BaseColorDef über Interaktionszustände,
// keine Enabled-Variante (Disabled = Treatment)
@Immutable
data class StatefulBaseColorDef(
    val normal: BaseColorDef,
    val hovered: BaseColorDef? = null,
    val focused: BaseColorDef? = null,
    val pressed: BaseColorDef? = null,
    val error: BaseColorDef? = null,
) {
    // Verantwortung: wählt Zustand nach Priorität, wendet Disabled-Treatment an, liefert finale Farben
    @Composable
    @ReadOnlyComposable
    fun resolve(
        state: StyledResolveState,
    ): BaseColor {
        val base = when (state.interaction) {
            StyledInteractionState.Error -> error ?: normal
            StyledInteractionState.Focused -> focused ?: normal
            StyledInteractionState.Hovered -> hovered ?: normal
            StyledInteractionState.Pressed -> pressed ?: focused ?: hovered ?: normal
            StyledInteractionState.Normal -> normal
        }
        val disableFactors = StyledTheme.colors.disableFactors
        fun Color.treat(disableFactorType: DisableFactorType) = if (state.enabled) this else copy(alpha = alpha * disableFactors.get(disableFactorType))
        return BaseColor(
            background = base.background.treat(DisableFactorType.Default),
            foreground = base.foreground.treat(DisableFactorType.Content),
            border = base.border?.treat(DisableFactorType.Outline)
        )
    }
}