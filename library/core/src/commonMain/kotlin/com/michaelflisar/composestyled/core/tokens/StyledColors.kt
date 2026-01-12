package com.michaelflisar.composestyled.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.classes.ThemeMode

/**
 * Definiert die Standardfarben für das Designsystem.
 */
@Immutable
data class StyledColors(
    val isDarkTheme: Boolean,
    val backgroundDef: Definition,
    val surfaceDef: Definition,
    val surfaceVariantDef: Definition,
    val primaryDef: Definition,
    val secondaryDef: Definition,
    val outlineDef: Definition,
    val outlineVariantDef: Definition,
    val errorDef: Definition,
    val successDef: Definition,
    val disableFactors: StyledDisableFactors
) {
    @Immutable
    data class Definition(
        val color: Color,
        val onColor: Color,
    )

    // ------------------
    // direct colors accessor for convenience
    // ------------------

    val background: Color
        get() = backgroundDef.color

    val onBackground: Color
        get() = backgroundDef.onColor

    val surface: Color
        get() = surfaceDef.color

    val onSurface: Color
        get() = surfaceDef.onColor

    val surfaceVariant: Color
        get() = surfaceVariantDef.color

    val onSurfaceVariant: Color
        get() = surfaceVariantDef.onColor

    val primary: Color
        get() = primaryDef.color

    val onPrimary: Color
        get() = primaryDef.onColor

    val seondary: Color
        get() = secondaryDef.color

    val onSecondary: Color
        get() = secondaryDef.onColor

    val outline: Color
        get() = outlineDef.color

    val onOutline: Color
        get() = outlineDef.onColor

    val outlineVariant: Color
        get() = outlineVariantDef.color

    val onOutlineVariant: Color
        get() = outlineVariantDef.onColor

    val error: Color
        get() = errorDef.color

    val onError: Color
        get() = errorDef.onColor

    val success: Color
        get() = successDef.color

    val onSuccess: Color
        get() = successDef.onColor
}

@Immutable
data class StyledDisableFactors(
    val default: Float,
    val outline: Float,
    val content: Float,
) {
    fun get(type: DisableFactorType): Float = when (type) {
        DisableFactorType.Default -> default
        DisableFactorType.Outline -> outline
        DisableFactorType.Content -> content
    }
}

fun lightDisableFactors() = StyledDisableFactors(
    default = 0.38f, // Container / Surface
    outline = 0.38f, // Border / Divider
    content = 0.38f, // Text / Icon
)

fun darkDisableFactors() = StyledDisableFactors(
    default = 0.38f, // Container
    outline = 0.38f, // Border
    content = 0.50f, // Text / Icon stärker sichtbar
)


fun lightStyledColors() = StyledColors(
    isDarkTheme = false,
    // Material3 background + onBackground
    backgroundDef = StyledColors.Definition(
        Color(0xFFFFFBFE),
        Color(0xFF1C1B1F)
    ),

    // Material3 surface + onSurface
    surfaceDef = StyledColors.Definition(
        Color(0xFFFFFBFE),
        Color(0xFF1C1B1F)
    ),
    // Material3 surfaceVariant + onSurfaceVariant
    surfaceVariantDef = StyledColors.Definition(
        Color(0xFFE7E0EC),
        Color(0xFF49454F)
    ),
    // Material3 primary + onPrimary
    primaryDef = StyledColors.Definition(
        Color(0xFF2F6BFF),
        Color(0xFFFFFFFF)
    ),
    // Material3 secondary + onSecondary
    secondaryDef = StyledColors.Definition(
        Color(0xFF625B71),
        Color(0xFFFFFFFF)
    ),
    // Material3 outline + onOutline
    outlineDef = StyledColors.Definition(
        Color(0xFF79747E),
        Color(0xFF1C1B1F)
    ),
    // Material3 outlineVariant
    outlineVariantDef = StyledColors.Definition(
        Color(0xFFCAC4D0),
        Color(0xFF49454F)
    ),
    // Material3 error + onError
    errorDef = StyledColors.Definition(
        Color(0xFFB3261E),
        Color(0xFFFFFFFF)
    ),
    successDef = StyledColors.Definition(
        Color(0xFF2E7D32),
        Color(0xFFFFFFFF)
    ),
    disableFactors = lightDisableFactors()
)

fun darkStyledColors() = StyledColors(
    isDarkTheme = true,
    // Material3 background + onBackground (dark)
    backgroundDef = StyledColors.Definition(
        Color(0xFF1C1B1F),
        Color(0xFFE6E1E5)
    ),
    // Material3 surface + onSurface (dark)
    surfaceDef = StyledColors.Definition(
        Color(0xFF1C1B1F),
        Color(0xFFE6E1E5)
    ),
    // Material3 surfaceVariant + onSurfaceVariant (dark)
    surfaceVariantDef = StyledColors.Definition(
        Color(0xFF49454F),
        Color(0xFFCAC4D0)
    ),
    // Material3 primary + onPrimary (dark)
    primaryDef = StyledColors.Definition(
        Color(0xFF7AA2FF),
        Color(0xFF001A43)
    ),
    // Material3 secondary + onSecondary (dark)
    secondaryDef = StyledColors.Definition(
        Color(0xFFCBC4DB),
        Color(0xFF332D41)
    ),
    // Material3 outline (dark)
    outlineDef = StyledColors.Definition(
        Color(0xFF938F99),
        Color(0xFFE6E1E5)
    ),
    // Material3 outlineVariant (dark)
    outlineVariantDef = StyledColors.Definition(
        Color(0xFF49454F),
        Color(0xFFCAC4D0)
    ),
    // Material3 error + onError (dark)
    errorDef = StyledColors.Definition(
        Color(0xFFF2B8B5),
        Color(0xFF601410)
    ),
    successDef = StyledColors.Definition(
        Color(0xFF81C784),
        Color(0xFF003908)
    ),
    disableFactors = darkDisableFactors()
)

internal val LocalStyledColors = staticCompositionLocalOf { lightStyledColors() }
