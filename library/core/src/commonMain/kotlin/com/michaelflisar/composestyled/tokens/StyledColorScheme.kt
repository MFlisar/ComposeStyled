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

    val surfaceLowest: Color get() = surfaceLowestDef.color
    val onSurfaceLowest: Color get() = surfaceLowestDef.onColor

    val surfaceLow: Color get() = surfaceLowDef.color
    val onSurfaceLow: Color get() = surfaceLowDef.onColor

    val surface: Color get() = surfaceDef.color
    val onSurface: Color get() = surfaceDef.onColor

    val surfaceHigh: Color get() = surfaceHighDef.color
    val onSurfaceHigh: Color get() = surfaceHighDef.onColor

    val surfaceHighest: Color get() = surfaceHighestDef.color
    val onSurfaceHighest: Color get() = surfaceHighestDef.onColor

    val surfaceVariant: Color get() = surfaceVariantDef.color
    val onSurfaceVariant: Color get() = surfaceVariantDef.onColor

    val inverseSurface: Color get() = inverseSurfaceDef.color
    val inverseOnSurface: Color get() = inverseSurfaceDef.onColor

    val primary: Color get() = primaryDef.color
    val onPrimary: Color get() = primaryDef.onColor

    val primarySurface: Color get() = primarySurfaceDef.color
    val onPrimarySurface: Color get() = primarySurfaceDef.onColor

    val secondary: Color get() = secondaryDef.color
    val onSecondary: Color get() = secondaryDef.onColor

    val secondarySurface: Color get() = secondarySurfaceDef.color
    val onSecondarySurface: Color get() = secondarySurfaceDef.onColor

    val tertiary: Color get() = tertiaryDef.color
    val onTertiary: Color get() = tertiaryDef.onColor

    val tertiarySurface: Color get() = tertiarySurfaceDef.color
    val onTertiarySurface: Color get() = tertiarySurfaceDef.onColor

    val error: Color get() = errorDef.color
    val onError: Color get() = errorDef.onColor

    val errorSurface: Color get() = errorSurfaceDef.color
    val onErrorSurface: Color get() = errorSurfaceDef.onColor

    val success: Color get() = successDef.color
    val onSuccess: Color get() = successDef.onColor

    val successSurface: Color get() = successSurfaceDef.color
    val onSuccessSurface: Color get() = successSurfaceDef.onColor

    val warning: Color get() = warningDef.color
    val onWarning: Color get() = warningDef.onColor

    val warningSurface: Color get() = warningSurfaceDef.color
    val onWarningSurface: Color get() = warningSurfaceDef.onColor
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
