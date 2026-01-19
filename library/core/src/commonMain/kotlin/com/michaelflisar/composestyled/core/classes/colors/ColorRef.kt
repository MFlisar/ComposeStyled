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
sealed interface ColorRef {

    @Composable
    @ReadOnlyComposable
    fun resolve(): Color

    @Immutable
    data class Fixed(
        val color: Color,
        val alpha: Float? = null,
    ) : ColorRef {

        @Composable
        @ReadOnlyComposable
        override fun resolve(): Color = if (alpha != null) color.copy(alpha = alpha) else color

    }

    enum class Token : ColorRef {
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
        ;

        @Composable
        @ReadOnlyComposable
        override fun resolve(): Color {
            return when (this) {
                Primary -> StyledTheme.colors.primary
                OnPrimary -> StyledTheme.colors.onPrimary
                Secondary -> StyledTheme.colors.secondary
                OnSecondary -> StyledTheme.colors.onSecondary
                Background -> StyledTheme.colors.background
                OnBackground -> StyledTheme.colors.onBackground
                Surface -> StyledTheme.colors.surface
                OnSurface -> StyledTheme.colors.onSurface
                Error -> StyledTheme.colors.error
                OnError -> StyledTheme.colors.onError
                Outline -> StyledTheme.colors.outline
                OutlineVariant -> StyledTheme.colors.outlineVariant
            }
        }
    }

    enum class Local : ColorRef {
        Content,
        Container
        ;

        @Composable
        @ReadOnlyComposable
        override fun resolve(): Color {
            return when (this) {
                Content -> LocalContentColor.current
                Container -> LocalBackgroundColor.current
            }
        }
    }
}