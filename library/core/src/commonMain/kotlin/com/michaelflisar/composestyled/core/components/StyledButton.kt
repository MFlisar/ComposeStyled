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
import com.michaelflisar.composestyled.core.tokens.StyledColors

object StyledButton {

    internal val Property = StyledThemeProperty<StatefulBaseColorDef>("button")

    internal val ButtonFilledPrimary =
        StyledThemeToken<StatefulBaseColorDef>("button.filled.primary")
    internal val ButtonOutlined = StyledThemeToken<StatefulBaseColorDef>("button.outlined.default")
    internal val ButtonText = StyledThemeToken<StatefulBaseColorDef>("button.text.default")

    sealed class Variant {

        abstract val token: StyledThemeToken<StatefulBaseColorDef>?

        object FilledPrimary : Variant() {
            override val token = ButtonFilledPrimary
        }

        object Outlined : Variant() {
            override val token = ButtonOutlined
        }

        object Text : Variant() {
            override val token = ButtonText
        }

        class Custom(
            val colorDef: StatefulBaseColorDef,
        ) : Variant() {
            override val token = null
        }
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
                foreground = colors.onBackground,
                border = colors.outlineVariant
            )
        )
        val buttonText = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onBackground,
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
fun Test(
    variant: StyledButton.Variant,
) {
    val color = variant.token?.let { token ->
        Theme[StyledButton.Property.property][token.token]
    } ?: (variant as StyledButton.Variant.Custom).colorDef

    // TODO
    //

}

@Composable
fun StyledButton(
    variant: StyledButton.Variant,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    shape: Shape,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource?,
    content: @Composable RowScope.() -> Unit,
) {
    LocalStyledComponents.current.Button(
        variant = variant,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}
