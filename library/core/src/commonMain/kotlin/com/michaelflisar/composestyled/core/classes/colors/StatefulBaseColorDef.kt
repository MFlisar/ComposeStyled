package com.michaelflisar.composestyled.core.classes.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.classes.StyledInteractionState
import com.michaelflisar.composestyled.core.classes.StyledResolveState
import com.michaelflisar.composestyled.core.treat

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
    fun customise(
        background: Color? = null,
        foreground: Color? = null,
        border: Color? = null,
    ) = StatefulBaseColorDef(
        normal = normal.customise(background, foreground, border),
        hovered = hovered?.customise(background, foreground, border),
        focused = focused?.customise(background, foreground, border),
        pressed = pressed?.customise(background, foreground, border),
        error = error?.customise(background, foreground, border),
    )


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
        return BaseColor(
            background = base.background.treat(state, DisableFactorType.Default),
            foreground = base.foreground.treat(state, DisableFactorType.Content),
            border = base.border?.treat(state, DisableFactorType.Outline)
        )
    }

    @Composable
    @ReadOnlyComposable
    internal fun resolve(
        state: StyledInteractionState,
    ): BaseColor {
        val colorRef = when (state) {
            StyledInteractionState.Error -> error ?: normal
            StyledInteractionState.Focused -> focused ?: normal
            StyledInteractionState.Hovered -> hovered ?: normal
            StyledInteractionState.Pressed -> pressed ?: focused ?: hovered ?: normal
            StyledInteractionState.Normal -> normal
        }
        return BaseColor(
            background = colorRef.background,
            foreground = colorRef.foreground,
            border = colorRef.border
        )
    }
}