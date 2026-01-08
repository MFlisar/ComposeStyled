package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeBuilder
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState
import com.michaelflisar.composestyled.core.tokens.StyledColors


object StyledButton : BaseStyledComponent {

    internal val Property = ThemeProperty<StatefulBaseColorDef>("button")

    internal val TokenFilledPrimary = ThemeToken<StatefulBaseColorDef>("button.filled.primary")
    internal val TokenOutlined = ThemeToken<StatefulBaseColorDef>("button.outlined.default")
    internal val TokenText = ThemeToken<StatefulBaseColorDef>("button.text.default")

    sealed class Variant {

        companion object {

            val FilledPrimary: Variant = Token(TokenFilledPrimary)
            val Outlined: Variant = Token(TokenOutlined)
            val Text: Variant = Token(TokenText)

            fun custom(colorDef: StatefulBaseColorDef): Variant = Custom(colorDef)
        }

        internal data class Token(
            val token: ThemeToken<StatefulBaseColorDef>,
        ) : Variant()

        internal data class Custom(val colorDef: StatefulBaseColorDef) : Variant()

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
        val buttonFilledPrimary = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = null
            )
        )
        val buttonOutlined = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.primary,
                border = colors.outlineVariant
            )
        )
        val buttonText = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.primary,
                border = null
            )
        )
        return mapOf(
            TokenFilledPrimary to buttonFilledPrimary,
            TokenOutlined to buttonOutlined,
            TokenText to buttonText,
        )
    }
}

// ----------------------
// Defaults
// ----------------------

object StyledButtonDefaults {
    @Composable
    fun contentPadding(
        horizontal: Dp = StyledTheme.paddings.large,
        vertical: Dp = StyledTheme.paddings.small,
    ) = PaddingValues(horizontal = horizontal, vertical = vertical)
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledButton(
    variant: StyledButton.Variant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = StyledTheme.shapes.button,
    contentPadding: PaddingValues = StyledButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val color = when (variant) {
        is StyledButton.Variant.Token -> Theme[StyledButton.Property][variant.token]
        is StyledButton.Variant.Custom -> variant.colorDef
    }
    val resolveState = rememberStyledResolveState(interactionSource, enabled, isError = false /* no error state for buttons */)
    val colors = color.resolve(resolveState)

    LocalStyledComponents.current.Button(
        colors = colors,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}
