package com.michaelflisar.composestyled.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.classes.DisableFactorType

/**
 * Definiert die Standardfarben für das Designsystem.
 */
@Immutable
data class StyledColors(
    val backgroundDef: Definition,
    val surfaceDef: Definition,
    val surfaceVariantDef: Definition,
    val primaryDef: Definition,
    val outlineDef: Definition,
    val outlineVariantDef: Definition,
    val errorDef: Definition,
    val disableFactors: StyledDisableFactors = StyledDisableFactors(),
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
}

@Immutable
data class StyledDisableFactors(
    val default: Float = .38f,
    val outline: Float = .12f,
    val content: Float = .12f,
) {
    fun get(type: DisableFactorType): Float = when (type) {
        DisableFactorType.Default -> default
        DisableFactorType.Outline -> outline
        DisableFactorType.Content -> content
    }
}

fun lightStyledColors() = StyledColors(
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
    )
)

fun darkStyledColors() = StyledColors(
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
    )
)

internal val LocalStyledColors = staticCompositionLocalOf { lightStyledColors() }
