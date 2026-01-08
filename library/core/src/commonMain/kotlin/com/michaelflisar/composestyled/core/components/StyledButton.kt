package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeBuilder
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.wrappers.StyledThemeProperty
import com.michaelflisar.composestyled.core.classes.wrappers.StyledThemeToken
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState
import com.michaelflisar.composestyled.core.tokens.StyledColors

object StyledButton {

    class Config(
        val colorDef: StatefulBaseColorDef,
    )

    internal val Property = StyledThemeProperty<StatefulBaseColorDef>("button")

    internal val ButtonFilledPrimary =
        StyledThemeToken<StatefulBaseColorDef>("button.filled.primary")
    internal val ButtonOutlined = StyledThemeToken<StatefulBaseColorDef>("button.outlined.default")
    internal val ButtonText = StyledThemeToken<StatefulBaseColorDef>("button.text.default")

    sealed class Variant {
        internal class Token internal constructor(
            val token: StyledThemeToken<StatefulBaseColorDef>,
        ) : Variant()

        class Custom(val colorDef: StatefulBaseColorDef) : Variant()

        val FilledPrimary: Variant = Token(StyledThemeToken("button.filled.primary"))
        val Outlined: Variant = Token(StyledThemeToken("button.outlined.default"))
        val Text: Variant = Token(StyledThemeToken("button.text.default"))
    }

    @Composable
    internal fun registerStyle(builder: ThemeBuilder, colors: StyledColors) {
        with(builder) {
            properties[Property.property] = createDefaultKeyMap(colors)
        }
    }

    @Composable
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
            ButtonFilledPrimary.token to buttonFilledPrimary,
            ButtonOutlined.token to buttonOutlined,
            ButtonText.token to buttonText,
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
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val color = when (variant) {
        is StyledButton.Variant.Token -> Theme[StyledButton.Property.property][variant.token.token]
        is StyledButton.Variant.Custom -> variant.colorDef
    }
    val resolveState = rememberStyledResolveState(interactionSource, enabled)
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
