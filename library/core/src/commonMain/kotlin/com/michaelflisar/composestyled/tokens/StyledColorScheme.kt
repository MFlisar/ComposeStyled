package com.michaelflisar.composestyled.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.classes.StyledLocalMissingException

@Immutable
data class StyledColorScheme(
    val isDarkTheme: Boolean,

    internal val backgroundDef: Definition,
    internal val surfaceDef: Definition,

    // "Neutral Variant" (z.B. für chips / cards / inputs)
    internal val surfaceVariantDef: Definition,

    // Brand
    internal val primaryDef: Definition,
    internal val primarySurfaceDef: Definition,
    internal val secondaryDef: Definition,
    internal val secondarySurfaceDef: Definition,

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
    val disableFactors: StyledDisableFactors,
    internal val tertiaryDef: Definition = secondaryDef,
    internal val tertiarySurfaceDef: Definition = secondarySurfaceDef,
) {
    @Immutable
    data class Definition(
        val color: Color,
        val onColor: Color,
    )

    internal val allDefinitions = listOf(
        backgroundDef,
        surfaceDef,
        surfaceVariantDef,
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

    val surface: Color get() = surfaceDef.color
    val onSurface: Color get() = surfaceDef.onColor

    val surfaceVariant: Color get() = surfaceVariantDef.color
    val onSurfaceVariant: Color get() = surfaceVariantDef.onColor

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


internal val LocalStyledColorScheme =
    staticCompositionLocalOf<StyledColorScheme> { throw StyledLocalMissingException("LocalStyledColorScheme") }
