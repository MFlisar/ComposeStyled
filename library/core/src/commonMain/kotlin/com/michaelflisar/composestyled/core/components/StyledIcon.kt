package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder

object StyledIcon : BaseStyledComponent {

    // Default-Tint (wenn niemand Variant/Token nutzt)
    internal val Property = ThemeProperty<Color>("icon")

    internal val TokenDefault = ThemeToken<Color>("icon.default")
    internal val TokenMuted = ThemeToken<Color>("icon.muted")
    internal val TokenError = ThemeToken<Color>("icon.error")
    internal val TokenOnPrimary = ThemeToken<Color>("icon.onPrimary")

    sealed class Variant {
        companion object {
            val Default: Variant = Token(TokenDefault)
            val Muted: Variant = Token(TokenMuted)
            val Error: Variant = Token(TokenError)
            val OnPrimary: Variant = Token(TokenOnPrimary)
            fun custom(tint: Color): Variant = Custom(tint)
        }

        internal data class Token(val token: ThemeToken<Color>) : Variant()
        internal data class Custom(val tint: Color) : Variant()
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        default: Color,
        muted: Color,
        error: Color,
        onPrimary: Color,
    ) {
        with(LocalThemeBuilder.current) {
            properties[Property] = mapOf(
                TokenDefault to default,
                TokenMuted to muted,
                TokenError to error,
                TokenOnPrimary to onPrimary,
            )
        }
    }
}

object StyledIconDefaults {
    val size: Dp = 24.dp
}

@Composable
fun StyledIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = StyledIconDefaults.size,
    variant: StyledIcon.Variant = StyledIcon.Variant.Default,
) {
    val themedTint = when (variant) {
        is StyledIcon.Variant.Token -> Theme[StyledIcon.Property][variant.token]
        is StyledIcon.Variant.Custom -> variant.tint
    }

    val finalTint = if (tint != Color.Unspecified) tint else themedTint

    LocalStyledComponents.current.Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = finalTint,
        size = size,
    )
}

@Composable
fun StyledIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = StyledIconDefaults.size,
    variant: StyledIcon.Variant = StyledIcon.Variant.Default,
) {
    val themedTint = when (variant) {
        is StyledIcon.Variant.Token -> Theme[StyledIcon.Property][variant.token]
        is StyledIcon.Variant.Custom -> variant.tint
    }

    val finalTint = if (tint != Color.Unspecified) tint else themedTint

    LocalStyledComponents.current.Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = finalTint,
        size = size,
    )
}
