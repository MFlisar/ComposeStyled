package com.michaelflisar.composestyled.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.classes.StyledLocalMissingException
import com.michaelflisar.composestyled.contrastRatio

/** Semantic color roles resolved for one light/dark theme and contrast level. */
@Immutable
data class StyledColorScheme(
    val isDarkTheme: Boolean,

    internal val backgroundDef: Definition,

    // Surface
    internal val surfaceLowestDef: Definition,
    internal val surfaceLowDef: Definition,
    internal val surfaceDef: Definition,
    internal val surfaceHighDef: Definition,
    internal val surfaceHighestDef: Definition,
    internal val surfaceVariantDef: Definition,

    // Inverse Surface
    internal val inverseSurfaceDef: Definition,

    // Brand
    internal val primaryDef: Definition,
    internal val primarySurfaceDef: Definition,
    internal val secondaryDef: Definition,
    internal val secondarySurfaceDef: Definition,
    internal val tertiaryDef: Definition = secondaryDef,
    internal val tertiarySurfaceDef: Definition = secondarySurfaceDef,

    // Lines
    val outline: Color,
    val outlineVariant: Color,

    // Status
    internal val errorDef: Definition,
    internal val errorSurfaceDef: Definition,
    internal val successDef: Definition,
    internal val successSurfaceDef: Definition,
    internal val warningDef: Definition,
    internal val warningSurfaceDef: Definition,

    // Disable handling
    val disabledAlphaFactors: StyledDisabledAlphaFactors,
) {
    /** A semantic color role with its default and supporting foreground colors. */
    @Immutable
    data class Definition(
        val color: Color,
        val onColor: Color,
        val onColorSupporting: Color = supportingColor(color, onColor),
    )

    internal val allDefinitions = listOf(
        backgroundDef,
        surfaceLowestDef,
        surfaceLowDef,
        surfaceDef,
        surfaceHighDef,
        surfaceHighestDef,
        surfaceVariantDef,
        inverseSurfaceDef,
        primaryDef,
        primarySurfaceDef,
        secondaryDef,
        secondarySurfaceDef,
        tertiaryDef,
        tertiarySurfaceDef,
        errorDef,
        errorSurfaceDef,
        successDef,
        successSurfaceDef,
        warningDef,
        warningSurfaceDef
    )

    // ------------------
    // direct colors accessor for convenience
    // ------------------

    val background: Color get() = backgroundDef.color
    val onBackground: Color get() = backgroundDef.onColor
    val onBackgroundSupporting: Color get() = backgroundDef.onColorSupporting

    val surfaceLowest: Color get() = surfaceLowestDef.color
    val onSurfaceLowest: Color get() = surfaceLowestDef.onColor
    val onSurfaceLowestSupporting: Color get() = surfaceLowestDef.onColorSupporting

    val surfaceLow: Color get() = surfaceLowDef.color
    val onSurfaceLow: Color get() = surfaceLowDef.onColor
    val onSurfaceLowSupporting: Color get() = surfaceLowDef.onColorSupporting

    val surface: Color get() = surfaceDef.color
    val onSurface: Color get() = surfaceDef.onColor
    val onSurfaceSupporting: Color get() = surfaceDef.onColorSupporting

    val surfaceHigh: Color get() = surfaceHighDef.color
    val onSurfaceHigh: Color get() = surfaceHighDef.onColor
    val onSurfaceHighSupporting: Color get() = surfaceHighDef.onColorSupporting

    val surfaceHighest: Color get() = surfaceHighestDef.color
    val onSurfaceHighest: Color get() = surfaceHighestDef.onColor
    val onSurfaceHighestSupporting: Color get() = surfaceHighestDef.onColorSupporting

    val surfaceVariant: Color get() = surfaceVariantDef.color
    val onSurfaceVariant: Color get() = surfaceVariantDef.onColor
    val onSurfaceVariantSupporting: Color get() = surfaceVariantDef.onColorSupporting

    val inverseSurface: Color get() = inverseSurfaceDef.color
    val inverseOnSurface: Color get() = inverseSurfaceDef.onColor
    val inverseOnSurfaceSupporting: Color get() = inverseSurfaceDef.onColorSupporting

    val primary: Color get() = primaryDef.color
    val onPrimary: Color get() = primaryDef.onColor
    val onPrimarySupporting: Color get() = primaryDef.onColorSupporting

    val primarySurface: Color get() = primarySurfaceDef.color
    val onPrimarySurface: Color get() = primarySurfaceDef.onColor
    val onPrimarySurfaceSupporting: Color get() = primarySurfaceDef.onColorSupporting

    val secondary: Color get() = secondaryDef.color
    val onSecondary: Color get() = secondaryDef.onColor
    val onSecondarySupporting: Color get() = secondaryDef.onColorSupporting

    val secondarySurface: Color get() = secondarySurfaceDef.color
    val onSecondarySurface: Color get() = secondarySurfaceDef.onColor
    val onSecondarySurfaceSupporting: Color get() = secondarySurfaceDef.onColorSupporting

    val tertiary: Color get() = tertiaryDef.color
    val onTertiary: Color get() = tertiaryDef.onColor
    val onTertiarySupporting: Color get() = tertiaryDef.onColorSupporting

    val tertiarySurface: Color get() = tertiarySurfaceDef.color
    val onTertiarySurface: Color get() = tertiarySurfaceDef.onColor
    val onTertiarySurfaceSupporting: Color get() = tertiarySurfaceDef.onColorSupporting

    val error: Color get() = errorDef.color
    val onError: Color get() = errorDef.onColor
    val onErrorSupporting: Color get() = errorDef.onColorSupporting

    val errorSurface: Color get() = errorSurfaceDef.color
    val onErrorSurface: Color get() = errorSurfaceDef.onColor
    val onErrorSurfaceSupporting: Color get() = errorSurfaceDef.onColorSupporting

    val success: Color get() = successDef.color
    val onSuccess: Color get() = successDef.onColor
    val onSuccessSupporting: Color get() = successDef.onColorSupporting

    val successSurface: Color get() = successSurfaceDef.color
    val onSuccessSurface: Color get() = successSurfaceDef.onColor
    val onSuccessSurfaceSupporting: Color get() = successSurfaceDef.onColorSupporting

    val warning: Color get() = warningDef.color
    val onWarning: Color get() = warningDef.onColor
    val onWarningSupporting: Color get() = warningDef.onColorSupporting

    val warningSurface: Color get() = warningSurfaceDef.color
    val onWarningSurface: Color get() = warningSurfaceDef.onColor
    val onWarningSurfaceSupporting: Color get() = warningSurfaceDef.onColorSupporting
}

private fun supportingColor(containerColor: Color, contentColor: Color): Color {
    val originalContrast = contentColor.contrastRatio(containerColor)
    val targetContrast = maxOf(4.5f, originalContrast * 0.65f)
    if (targetContrast >= originalContrast) return contentColor

    fun blendTowardContainer(amount: Float) = Color(
        red = contentColor.red + (containerColor.red - contentColor.red) * amount,
        green = contentColor.green + (containerColor.green - contentColor.green) * amount,
        blue = contentColor.blue + (containerColor.blue - contentColor.blue) * amount,
        alpha = contentColor.alpha + (containerColor.alpha - contentColor.alpha) * amount,
    )

    var low = 0f
    var high = 1f
    repeat(24) {
        val amount = (low + high) / 2f
        if (blendTowardContainer(amount).contrastRatio(containerColor) >= targetContrast) {
            low = amount
        } else {
            high = amount
        }
    }

    return blendTowardContainer(low)
}


internal val LocalStyledColorScheme =
    staticCompositionLocalOf<StyledColorScheme> { throw StyledLocalMissingException("LocalStyledColorScheme") }
