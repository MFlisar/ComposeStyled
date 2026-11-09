package com.michaelflisar.composestyled.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor

/** Converts a fixed [Color] into a token usable by theme-aware customizations. */
fun Color.asColorToken(): ColorToken = ColorToken.Fixed(this)

/** Converts a specified [Color] into a token, or returns `null` for [Color.Unspecified]. */
fun Color.asColorTokenOrNull(): ColorToken? =
    takeIf { it != Color.Unspecified }?.asColorToken()

/** A color resolved either from a fixed value or from the active [StyledTheme]. */
@Stable
sealed interface ColorToken {

    /** Resolves this token in the current composition. */
    @Composable
    @ReadOnlyComposable
    fun resolve(): Color

    /** A fixed color, optionally resolved with a different alpha. */
    @Immutable
    data class Fixed(
        val color: Color,
        val alpha: Float? = null,
    ) : ColorToken {

        /** Returns [color], applying [alpha] when it is not null. */
        @Composable
        @ReadOnlyComposable
        override fun resolve(): Color = if (alpha != null) color.copy(alpha = alpha) else color

    }

    /** Semantic colors read from the active theme when [resolve] is called. */
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
                LocalContentColor -> LocalStyledContentColor.current
            }
        }
    }
}