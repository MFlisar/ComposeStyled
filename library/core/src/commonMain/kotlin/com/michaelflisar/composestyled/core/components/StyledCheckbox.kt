package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeBuilder
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState
import com.michaelflisar.composestyled.core.tokens.StyledColors

object StyledCheckbox : BaseStyledComponent {

    internal val Property = ThemeProperty<StatefulBaseColorDef>("checkbox")

    internal val TokenUnchecked = ThemeToken<StatefulBaseColorDef>("checkbox.unchecked")
    internal val TokenChecked = ThemeToken<StatefulBaseColorDef>("checkbox.checked")

    sealed class Variant {

        companion object {
            val Default: Variant = Token(unchecked = TokenUnchecked, checked = TokenChecked)
            fun custom(
                unchecked: StatefulBaseColorDef,
                checked: StatefulBaseColorDef,
            ): Variant = Custom(unchecked = unchecked, checked = checked)
        }

        internal data class Token(
            val unchecked: ThemeToken<StatefulBaseColorDef>,
            val checked: ThemeToken<StatefulBaseColorDef>,
        ) : Variant()

        internal data class Custom(
            val unchecked: StatefulBaseColorDef,
            val checked: StatefulBaseColorDef,
        ) : Variant()
    }

    override fun registerStyle(
        builder: ThemeBuilder,
        colors: StyledColors,
    ) {
        with(builder) {
            properties[Property] = createDefaultKeyMap(colors)
        }
    }

    private fun createDefaultKeyMap(colors: StyledColors): Map<ThemeToken<StatefulBaseColorDef>, StatefulBaseColorDef> {
        val unchecked = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.outline
            ),
            focused = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.primary
            ),
            error = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.error
            )
        )

        val checked = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = colors.primary
            ),
            focused = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = colors.primary
            ),
            error = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = colors.error
            )
        )

        return mapOf(
            TokenUnchecked to unchecked,
            TokenChecked to checked,
        )
    }
}

@Composable
fun StyledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    variant: StyledCheckbox.Variant = StyledCheckbox.Variant.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colorDef = when (variant) {
        is StyledCheckbox.Variant.Token -> if (checked) Theme[StyledCheckbox.Property][variant.checked] else Theme[StyledCheckbox.Property][variant.unchecked]
        is StyledCheckbox.Variant.Custom -> if (checked) variant.checked else variant.unchecked
    }

    val resolveState = rememberStyledResolveState(
        interactionSource = interactionSource,
        enabled = enabled,
        isError = isError,
    )
    val colors = colorDef.resolve(resolveState)

    LocalStyledComponents.current.Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = colors,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}
