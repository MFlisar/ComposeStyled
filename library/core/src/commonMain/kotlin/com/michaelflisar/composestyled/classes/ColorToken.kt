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
        Tertiary,
        OnTertiary,
        Background,
        OnBackground,
        SurfaceLowest,
        OnSurfaceLowest,
        SurfaceLow,
        OnSurfaceLow,
        Surface,
        OnSurface,
        SurfaceHigh,
        OnSurfaceHigh,
        SurfaceHighest,
        OnSurfaceHighest,
        SurfaceVariant,
        OnSurfaceVariant,
        InverseSurface,
        InverseOnSurface,
        TertiarySurface,
        OnTertiarySurface,
        Error,
        OnError,
        ErrorSurface,
        OnErrorSurface,
        Success,
        OnSuccess,
        SuccessSurface,
        OnSuccessSurface,
        Warning,
        OnWarning,
        WarningSurface,
        OnWarningSurface,
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
                Tertiary -> StyledTheme.colorScheme.tertiary
                OnTertiary -> StyledTheme.colorScheme.onTertiary
                Background -> StyledTheme.colorScheme.background
                OnBackground -> StyledTheme.colorScheme.onBackground
                SurfaceLowest -> StyledTheme.colorScheme.surfaceLowest
                OnSurfaceLowest -> StyledTheme.colorScheme.onSurfaceLowest
                SurfaceLow -> StyledTheme.colorScheme.surfaceLow
                OnSurfaceLow -> StyledTheme.colorScheme.onSurfaceLow
                Surface -> StyledTheme.colorScheme.surface
                OnSurface -> StyledTheme.colorScheme.onSurface
                SurfaceHigh -> StyledTheme.colorScheme.surfaceHigh
                OnSurfaceHigh -> StyledTheme.colorScheme.onSurfaceHigh
                SurfaceHighest -> StyledTheme.colorScheme.surfaceHighest
                OnSurfaceHighest -> StyledTheme.colorScheme.onSurfaceHighest
                SurfaceVariant -> StyledTheme.colorScheme.surfaceVariant
                OnSurfaceVariant -> StyledTheme.colorScheme.onSurfaceVariant
                InverseSurface -> StyledTheme.colorScheme.inverseSurface
                InverseOnSurface -> StyledTheme.colorScheme.inverseOnSurface
                TertiarySurface -> StyledTheme.colorScheme.tertiarySurface
                OnTertiarySurface -> StyledTheme.colorScheme.onTertiarySurface
                Error -> StyledTheme.colorScheme.error
                OnError -> StyledTheme.colorScheme.onError
                ErrorSurface -> StyledTheme.colorScheme.errorSurface
                OnErrorSurface -> StyledTheme.colorScheme.onErrorSurface
                Success -> StyledTheme.colorScheme.success
                OnSuccess -> StyledTheme.colorScheme.onSuccess
                SuccessSurface -> StyledTheme.colorScheme.successSurface
                OnSuccessSurface -> StyledTheme.colorScheme.onSuccessSurface
                Warning -> StyledTheme.colorScheme.warning
                OnWarning -> StyledTheme.colorScheme.onWarning
                WarningSurface -> StyledTheme.colorScheme.warningSurface
                OnWarningSurface -> StyledTheme.colorScheme.onWarningSurface
                Outline -> StyledTheme.colorScheme.outline
                OutlineVariant -> StyledTheme.colorScheme.outlineVariant
                LocalContentColor -> LocalStyledContentColor.current
            }
        }
    }
}