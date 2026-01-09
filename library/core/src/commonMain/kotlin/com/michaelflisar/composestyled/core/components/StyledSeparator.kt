package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder

object StyledSeparator : BaseStyledComponent {

    internal val Property = ThemeProperty<Color>("separator")

    internal val TokenDefault = ThemeToken<Color>("separator.default")
    internal val TokenStrong = ThemeToken<Color>("separator.strong")

    sealed class Variant {
        companion object {
            val Default: Variant = Token(TokenDefault)
            val Strong: Variant = Token(TokenStrong)

            fun custom(color: Color): Variant = Custom(color)
        }

        internal data class Token(val token: ThemeToken<Color>) : Variant()
        internal data class Custom(val color: Color) : Variant()
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        default: Color,
        strong: Color,
    ) {
        with(LocalThemeBuilder.current) {
            properties[Property] = mapOf(
                TokenDefault to default,
                TokenStrong to strong,
            )
        }
    }
}

/** Defaults für [StyledSeparator]. */
object StyledSeparatorDefaults {
    val thickness: Dp = 1.dp
}

@Composable
fun StyledHorizontalSeparator(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    thickness: Dp = StyledSeparatorDefaults.thickness,
    variant: StyledSeparator.Variant = StyledSeparator.Variant.Default,
) {
    val themedColor = when (variant) {
        is StyledSeparator.Variant.Token -> Theme[StyledSeparator.Property][variant.token]!!
        is StyledSeparator.Variant.Custom -> variant.color
    }

    val finalColor = when {
        color != Color.Unspecified -> color
        else -> themedColor
    }

    LocalStyledComponents.current.HorizontalSeparator(
        modifier = modifier,
        color = finalColor,
        thickness = thickness,
    )
}

@Composable
fun StyledVerticalSeparator(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    thickness: Dp = StyledSeparatorDefaults.thickness,
    variant: StyledSeparator.Variant = StyledSeparator.Variant.Default,
) {
    val themedColor = when (variant) {
        is StyledSeparator.Variant.Token -> Theme[StyledSeparator.Property][variant.token]!!
        is StyledSeparator.Variant.Custom -> variant.color
    }

    val finalColor = when {
        color != Color.Unspecified -> color
        else -> themedColor
    }

    LocalStyledComponents.current.VerticalSeparator(
        modifier = modifier,
        color = finalColor,
        thickness = thickness,
    )
}
