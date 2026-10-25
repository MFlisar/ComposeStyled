package com.michaelflisar.composestyledcomponents.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme

fun Color.asColorToken(): ColorToken = ColorToken.Fixed(this)

@Stable
sealed interface ColorToken {

    @Composable
    @ReadOnlyComposable
    fun resolve(): Color

    @Immutable
    data class Fixed(
        val color: Color,
        val alpha: Float? = null,
    ) : ColorToken {

        @Composable
        @ReadOnlyComposable
        override fun resolve(): Color = if (alpha != null) color.copy(alpha = alpha) else color

    }

    enum class Theme : ColorToken {
        Primary,
        OnPrimary,
        PrimarySurface,
        OnPrimarySurface,
        Secondary,
        OnSecondary,
        SecondarySurface,
        OnSecondarySurface,
        Background,
        OnBackground,
        Surface,
        OnSurface,
        Error,
        OnError,
        ErrorSurface,
        OnErrorSurface,
        Outline,
        OutlineVariant,
        LocalContentColor
        ;

        @Composable
        @ReadOnlyComposable
        override fun resolve(): Color {
            return when (this) {
                Primary -> StyledTheme.colorScheme.primary
                OnPrimary -> StyledTheme.colorScheme.onPrimary
                PrimarySurface -> StyledTheme.colorScheme.primarySurface
                OnPrimarySurface -> StyledTheme.colorScheme.onPrimarySurface
                Secondary -> StyledTheme.colorScheme.secondary
                OnSecondary -> StyledTheme.colorScheme.onSecondary
                SecondarySurface -> StyledTheme.colorScheme.secondarySurface
                OnSecondarySurface -> StyledTheme.colorScheme.onSecondarySurface
                Background -> StyledTheme.colorScheme.background
                OnBackground -> StyledTheme.colorScheme.onBackground
                Surface -> StyledTheme.colorScheme.surface
                OnSurface -> StyledTheme.colorScheme.onSurface
                Error -> StyledTheme.colorScheme.error
                OnError -> StyledTheme.colorScheme.onError
                ErrorSurface -> StyledTheme.colorScheme.errorSurface
                OnErrorSurface -> StyledTheme.colorScheme.onErrorSurface
                Outline -> StyledTheme.colorScheme.outline
                OutlineVariant -> StyledTheme.colorScheme.outlineVariant
                LocalContentColor -> StyledTheme.currentContentColor
            }
        }
    }
}