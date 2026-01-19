package com.michaelflisar.composestyled.core.classes.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.classes.StyledInteractionState
import com.michaelflisar.composestyled.core.classes.StyledResolveState
import com.michaelflisar.composestyled.core.disabled

@Immutable
data class StatefulBaseColorDef(
    val normal: BaseColorDef,
    val hovered: BaseColorDef? = null,
    val focused: BaseColorDef? = null,
    val pressed: BaseColorDef? = null,
    val error: BaseColorDef? = null,
) {
    fun customise(
        background: ColorRef? = null,
        foreground: ColorRef? = null,
        border: ColorRef? = null,
    ) = StatefulBaseColorDef(
        normal = normal.customise(background, foreground, border),
        hovered = hovered?.customise(background, foreground, border),
        focused = focused?.customise(background, foreground, border),
        pressed = pressed?.customise(background, foreground, border),
        error = error?.customise(background, foreground, border),
    )

    @Composable
    @ReadOnlyComposable
    fun resolve(
        state: StyledResolveState,
    ): BaseColor {
        return resolve(state.interaction, state.enabled)
    }

    @Composable
    @ReadOnlyComposable
    private fun resolve(
        state: StyledInteractionState,
        enabled: Boolean = true,
    ): BaseColor {
        val colorDefinition = when (state) {
            StyledInteractionState.Error -> error ?: normal
            StyledInteractionState.Focused -> focused ?: normal
            StyledInteractionState.Hovered -> hovered ?: normal
            StyledInteractionState.Pressed -> pressed ?: focused ?: hovered ?: normal
            StyledInteractionState.Normal -> normal
        }

        return BaseColor(
            background = colorDefinition.background.resolve()
                .treat(enabled, DisableFactorType.Default),
            foreground = colorDefinition.foreground.resolve()
                .treat(enabled, DisableFactorType.Content),
            border = colorDefinition.border?.resolve()?.treat(enabled, DisableFactorType.Outline),
        )
    }

    @Composable
    @ReadOnlyComposable
    private fun Color.treat(enabled: Boolean, disableFactorType: DisableFactorType) =
        if (enabled) this else this.disabled(disableFactorType)
}