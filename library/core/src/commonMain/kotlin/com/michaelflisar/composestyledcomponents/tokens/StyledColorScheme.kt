package com.michaelflisar.composestyledcomponents.tokens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.classes.StyledLocalMissingException

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


fun lightStyledColors() = StyledColorScheme(
    isDarkTheme = false,
    backgroundDef = StyledColorScheme.Definition(Color(0xFFFFFBFE), Color(0xFF1C1B1F)),
    surfaceDef = StyledColorScheme.Definition(Color(0xFFFFFBFE), Color(0xFF1C1B1F)),
    surfaceVariantDef = StyledColorScheme.Definition(Color(0xFFE7E0EC), Color(0xFF49454F)),

    primaryDef = StyledColorScheme.Definition(Color(0xFF2F6BFF), Color(0xFFFFFFFF)),
    primarySurfaceDef = StyledColorScheme.Definition(Color(0xFFDCE3FF), Color(0xFF00154C)),

    secondaryDef = StyledColorScheme.Definition(Color(0xFF455D7A), Color(0xFFFFFFFF)),
    secondarySurfaceDef = StyledColorScheme.Definition(Color(0xFFDCE8F7), Color(0xFF142238)),

    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),

    errorDef = StyledColorScheme.Definition(Color(0xFFB3261E), Color(0xFFFFFFFF)),
    errorSurfaceDef = StyledColorScheme.Definition(Color(0xFFF9DEDC), Color(0xFF410E0B)),

    successDef = StyledColorScheme.Definition(Color(0xFF2E7D32), Color(0xFFFFFFFF)),
    successSurfaceDef = StyledColorScheme.Definition(Color(0xFFC8E6C9), Color(0xFF003908)),

    warningDef = StyledColorScheme.Definition(Color(0xFFB26A00), Color(0xFFFFFFFF)),
    warningSurfaceDef = StyledColorScheme.Definition(Color(0xFFFFE0B2), Color(0xFF402A00)),

    disableFactors = lightDisableFactors()
)

fun darkStyledColors() = StyledColorScheme(
    isDarkTheme = true,
    backgroundDef = StyledColorScheme.Definition(Color(0xFF1C1B1F), Color(0xFFE6E1E5)),
    surfaceDef = StyledColorScheme.Definition(Color(0xFF1C1B1F), Color(0xFFE6E1E5)),
    surfaceVariantDef = StyledColorScheme.Definition(Color(0xFF49454F), Color(0xFFCAC4D0)),

    primaryDef = StyledColorScheme.Definition(Color(0xFF7AA2FF), Color(0xFF001A43)),
    primarySurfaceDef = StyledColorScheme.Definition(Color(0xFF253C59), Color(0xFFCFE0FF)),

    secondaryDef = StyledColorScheme.Definition(Color(0xFFB7C9E4), Color(0xFF26364D)),
    secondarySurfaceDef = StyledColorScheme.Definition(Color(0xFF354A65), Color(0xFFDCE8F7)),

    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),

    errorDef = StyledColorScheme.Definition(Color(0xFFF2B8B5), Color(0xFF601410)),
    errorSurfaceDef = StyledColorScheme.Definition(Color(0xFF8C1D18), Color(0xFFF9DEDC)),

    successDef = StyledColorScheme.Definition(Color(0xFF81C784), Color(0xFF003908)),
    successSurfaceDef = StyledColorScheme.Definition(Color(0xFF145A22), Color(0xFFC8E6C9)),

    warningDef = StyledColorScheme.Definition(Color(0xFFFFC470), Color(0xFF402A00)),
    warningSurfaceDef = StyledColorScheme.Definition(Color(0xFF5A3A00), Color(0xFFFFE0B2)),

    disableFactors = darkDisableFactors()
)

@Composable
fun autoStyledColors(
    dark: Boolean = isSystemInDarkTheme(),
) = if (dark) darkStyledColors() else lightStyledColors()

internal val LocalStyledColorScheme =
    staticCompositionLocalOf<StyledColorScheme> { throw StyledLocalMissingException("LocalStyledColorScheme") }
