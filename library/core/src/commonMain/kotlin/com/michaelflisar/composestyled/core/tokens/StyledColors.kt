package com.michaelflisar.composestyled.core.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.michaelflisar.composestyled.core.classes.ColorDef
import com.michaelflisar.composestyled.core.classes.DisableFactorType

/**
 * Definiert die Standardfarben für das Designsystem.
 */
@Immutable
data class StyledColors(
    val backgroundDef: ColorDef,
    val surfaceDef: ColorDef,
    val surfaceVariantDef: ColorDef,
    val primaryDef: ColorDef,
    val outlineDef: ColorDef,
    val outlineVariantDef: ColorDef,
    val inputDef: ColorDef,
    val disableFactors: StyledDisableFactors = StyledDisableFactors(),
) {
    // ------------------
    // direct colors accessor for convenience
    // ------------------

    val background: Color
        @Composable @ReadOnlyComposable get() = backgroundDef.color

    val onBackground: Color
        @Composable @ReadOnlyComposable get() = backgroundDef.onColor

    val surface: Color
        @Composable @ReadOnlyComposable get() = surfaceDef.color

    val onSurface: Color
        @Composable @ReadOnlyComposable get() = surfaceDef.onColor

    val surfaceVariant: Color
        @Composable @ReadOnlyComposable get() = surfaceVariantDef.color

    val onSurfaceVariant: Color
        @Composable @ReadOnlyComposable get() = surfaceVariantDef.onColor

    val primary: Color
        @Composable @ReadOnlyComposable get() = primaryDef.color

    val onPrimary: Color
        @Composable @ReadOnlyComposable get() = primaryDef.onColor

    val outline: Color
        @Composable @ReadOnlyComposable get() = outlineDef.color

    val onOutline: Color
        @Composable @ReadOnlyComposable get() = outlineDef.onColor

    val outlineVariant: Color
        @Composable @ReadOnlyComposable get() = outlineVariantDef.color

    val onOutlineVariant: Color
        @Composable @ReadOnlyComposable get() = outlineVariantDef.onColor

    val input: Color
        @Composable @ReadOnlyComposable get() = inputDef.color

    val onInput: Color
        @Composable @ReadOnlyComposable get() = inputDef.onColor

    // ------------------
    // access with enabled/disabled state
    // ------------------

    @Composable
    @ReadOnlyComposable
    fun background(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = backgroundDef.color(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun onBackground(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = backgroundDef.onColor(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun surface(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = surfaceDef.color(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun onSurface(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = surfaceDef.onColor(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun surfaceVariant(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = surfaceVariantDef.color(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun onSurfaceVariant(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = surfaceVariantDef.onColor(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun primary(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = primaryDef.color(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun onPrimary(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = primaryDef.onColor(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun outline(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = outlineDef.color(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun onOutline(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = outlineDef.onColor(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun outlineVariant(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = outlineVariantDef.color(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun onOutlineVariant(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = outlineVariantDef.onColor(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun input(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = inputDef.color(enabled, disableFactorType)

    @Composable
    @ReadOnlyComposable
    fun onInput(
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = inputDef.onColor(enabled, disableFactorType)

    // ------------------
    // helper function
    // ------------------

    @Composable
    @ReadOnlyComposable
    fun onColorFor(
        color: Color,
        enabled: Boolean = true,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ) = when (color) {
        backgroundDef.color -> onBackground(enabled, disableFactorType)
        surfaceDef.color -> onSurface(enabled, disableFactorType)
        surfaceVariantDef.color -> onSurfaceVariant(enabled, disableFactorType)
        primaryDef.color -> onPrimary(enabled, disableFactorType)
        outlineDef.color -> onOutline(enabled, disableFactorType)
        outlineVariantDef.color -> onOutlineVariant(enabled, disableFactorType)
        inputDef.color -> onInput(enabled, disableFactorType)
        else -> if (color.luminance() < .5f) Color.White else Color.Black
    }

    @Composable
    @ReadOnlyComposable
    fun onDisabledColorFor(
        disabledColor: Color,
        disableFactorType: DisableFactorType = DisableFactorType.Default,
    ): Color {
        val factor = LocalStyledColors.current.disableFactors.let {
            listOf(
                it.default,
                it.outline,
                it.icon,
                it.text
            )
        }.distinct()
        val contains = { color: ColorDef ->
            factor.map { color.color.copy(alpha = it) }.contains(disabledColor)
        }
        return when {
            contains(backgroundDef) -> onBackground(false, disableFactorType)
            contains(surfaceDef) -> onSurface(false, disableFactorType)
            contains(surfaceVariantDef) -> onSurfaceVariant(false, disableFactorType)
            contains(primaryDef) -> onPrimary(false, disableFactorType)
            contains(outlineDef) -> onOutline(false, disableFactorType)
            contains(outlineVariantDef) -> onOutlineVariant(false, disableFactorType)
            contains(inputDef) -> onInput(false, disableFactorType)
            else -> {
                val alpha = LocalStyledColors.current.disableFactors.get(disableFactorType)
                if (disabledColor.luminance() < .5f) Color.White.copy(alpha = alpha) else Color.Black.copy(
                    alpha = alpha
                )
            }
        }
    }

}

@Immutable
data class StyledDisableFactors(
    val default: Float = .38f,
    val outline: Float = .12f,
    val icon: Float = .12f,
    val text: Float = .12f,
) {
    fun get(type: DisableFactorType): Float = when (type) {
        DisableFactorType.Default -> default
        DisableFactorType.Outline -> outline
        DisableFactorType.Icon -> icon
        DisableFactorType.Text -> text
    }
}

fun lightStyledColors() = StyledColors(
    // Material3 background + onBackground
    backgroundDef = ColorDef(
        Color(0xFFFFFBFE),
        Color(0xFF1C1B1F)
    ),

    // Material3 surface + onSurface
    surfaceDef = ColorDef(Color(0xFFFFFBFE), Color(0xFF1C1B1F)),
    // Material3 surfaceVariant + onSurfaceVariant
    surfaceVariantDef = ColorDef(
        Color(0xFFFFFFFF),
        Color(0xFF1C1B1F)
    ),
    // Material3 primary + onPrimary
    primaryDef = ColorDef(Color(0xFF2F6BFF), Color(0xFFFFFFFF)),
    // Material3 outline + onOutline
    outlineDef = ColorDef(
        Color(0xFF79747E),
        Color(0xFFCAC4D0)
    ),
    // Material3 outlineVariant
    outlineVariantDef = ColorDef(
        Color(0xFFCAC4D0),
        Color(0xFF79747E)
    ),
    // input colors
    inputDef = ColorDef(
        Color(0xFFF5F5F5),
        Color(0xFF1C1B1F)
    )
)

fun darkStyledColors() = StyledColors(
    // Material3 background + onBackground (dark)
    backgroundDef = ColorDef(
        Color(0xFF1C1B1F),
        Color(0xFFE6E1E5)
    ),
    // Material3 surface + onSurface (dark)
    surfaceDef = ColorDef(
        Color(0xFF1C1B1F),
        Color(0xFFE6E1E5)
    ),
    // Material3 surfaceVariant + onSurfaceVariant (dark)
    surfaceVariantDef = ColorDef(
        Color(0xFF23232B),
        Color(0xFFE6E1E5)
    ),
    // Material3 primary + onPrimary (dark)
    primaryDef = ColorDef(
        Color(0xFF7AA2FF),
        Color(0xFF001A43)
    ),
    // Material3 outline (dark)
    outlineDef = ColorDef(
        Color(0xFF938F99),
        Color(0xFF44424F)
    ),
    // Material3 outlineVariant (dark)
    outlineVariantDef = ColorDef(
        Color(0xFF44424F),
        Color(0xFF938F99)
    ),
    // input colors (dark)
    inputDef = ColorDef(
        Color(0xFF2C2C2E),
        Color(0xFFE6E1E5)
    )
)

internal val LocalStyledColors = staticCompositionLocalOf { lightStyledColors() }
