package com.michaelflisar.composestyled.core.classes.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.runtime.LocalBackgroundColor
import com.michaelflisar.composestyled.core.runtime.LocalContentColor

fun Color.asColorRef(): ColorRef = ColorRef.Fixed(this)

@Stable
data class ColorRefTransformer(
    val alpha: Float? = null,
    val adjustWithAlpha: Boolean = false,
    val factorDarken: Float? = null
)

@Stable
sealed interface ColorRef {

    @Composable
    @ReadOnlyComposable
    fun resolve(): Color

    @Stable
    data class Fixed(
        val color: Color,
        val alpha: Float? = null
    ) : ColorRef {
        @Composable
        @ReadOnlyComposable
        override fun resolve(): Color = if (alpha != null) color.copy(alpha = alpha) else color
    }

    @Stable
    data class Token(
        val token: ColorToken,
    ) : ColorRef {
        @Composable
        override fun resolve(): Color {
            // Placeholder implementation, replace with actual theme color resolution
            return when (token) {
                ColorToken.Primary -> StyledTheme.colors.primary
                ColorToken.OnPrimary -> StyledTheme.colors.onPrimary
                ColorToken.Secondary -> StyledTheme.colors.secondary
                ColorToken.OnSecondary -> StyledTheme.colors.onSecondary
                ColorToken.Background -> StyledTheme.colors.background
                ColorToken.OnBackground -> StyledTheme.colors.onBackground
                ColorToken.Surface -> StyledTheme.colors.surface
                ColorToken.OnSurface -> StyledTheme.colors.onSurface
                ColorToken.Error -> StyledTheme.colors.error
                ColorToken.OnError -> StyledTheme.colors.onError
                ColorToken.Outline -> StyledTheme.colors.outline
                ColorToken.OutlineVariant -> StyledTheme.colors.outlineVariant
            }
        }
    }

    @Stable
    data class Local(
        val token: ColorLocalToken,
    ) : ColorRef {
        @Composable
        override fun resolve(): Color {
            // Placeholder implementation, replace with actual local color resolution
            return when (token) {
                ColorLocalToken.Content -> LocalContentColor.current
                ColorLocalToken.Container -> LocalBackgroundColor.current
            }
        }
    }
}

enum class ColorToken {
    Primary,
    OnPrimary,
    Secondary,
    OnSecondary,
    Background,
    OnBackground,
    Surface,
    OnSurface,
    Error,
    OnError,
    Outline,
    OutlineVariant
}

enum class ColorLocalToken {
    Content,
    Container
}