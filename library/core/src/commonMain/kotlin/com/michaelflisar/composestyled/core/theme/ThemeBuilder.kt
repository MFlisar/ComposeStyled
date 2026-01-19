package com.michaelflisar.composestyled.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.michaelflisar.composestyled.core.isLight
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.core.tokens.StyledDisableFactors
import com.michaelflisar.composestyled.core.tokens.darkDisableFactors
import com.michaelflisar.composestyled.core.tokens.lightDisableFactors

private fun blend(a: Color, b: Color, t: Float) = lerp(a, b, t)

private fun pickOn(bg: Color, fallback: Color? = null): Color {
    return if (bg.alpha < 0.01f && fallback != null)
        fallback
    else
        (if (bg.isLight) Color(0xFF111111) else Color(0xFFFFFFFF))
}

// "Surface" colors for intents: universal tonal backgrounds
private fun intentSurface(intent: Color, isDark: Boolean, surfaceVariant: Color): Color =
    blend(surfaceVariant, intent, if (isDark) 0.18f else 0.10f)

object ThemeBuilder {

    @Immutable
    data class Seeds(

        // brand/status
        val primary: Color,
        val secondary: Color,
        val success: Color,
        val warning: Color,
        val error: Color,

        // neutrals
        val background: Color,
        val onBackground: Color? = null,

        // optionally override computed neutrals if you want exact palettes
        val surface: Color? = null,
        val surfaceVariant: Color? = null,
        val outline: Color? = null,
        val outlineVariant: Color? = null,

        // optional: your disable factors
        val disableFactors: StyledDisableFactors? = null,
    )

    fun buildStyledColors(
        isDark: Boolean,
        seeds: Seeds,
    ): StyledColors {

        val bg = seeds.background
        val onBg = seeds.onBackground ?: pickOn(bg)

        val surface = seeds.surface ?: if (isDark)
            blend(bg, Color.White, 0.06f)
        else
            blend(bg, Color.Black, 0.02f)

        val onSurface = pickOn(surface, fallback = onBg)

        val outline = seeds.outline ?: blend(onSurface, surface, if (isDark) 0.55f else 0.70f)
        val outlineVariant =
            seeds.outlineVariant ?: blend(outline, surface, if (isDark) 0.65f else 0.75f)

        val surfaceVariant =
            seeds.surfaceVariant ?: blend(surface, outlineVariant, if (isDark) 0.35f else 0.25f)
        val onSurfaceVariant = pickOn(surfaceVariant, fallback = onSurface)

        // onIntentSurface: keep it stable & readable (neutral onSurface is safest cross-platform)
        val primarySurface = intentSurface(seeds.primary, isDark, surfaceVariant)
        val secondarySurface = intentSurface(seeds.secondary, isDark, surfaceVariant)
        val errorSurface = intentSurface(seeds.error, isDark, surfaceVariant)
        val successSurface = intentSurface(seeds.success, isDark, surfaceVariant)
        val warningSurface = intentSurface(seeds.warning, isDark, surfaceVariant)

        val disable = seeds.disableFactors ?: if (isDark)
            darkDisableFactors()
        else
            lightDisableFactors()

        return StyledColors(
            isDarkTheme = isDark,
            backgroundDef = StyledColors.Definition(bg, onBg),
            surfaceDef = StyledColors.Definition(surface, onSurface),
            surfaceVariantDef = StyledColors.Definition(surfaceVariant, onSurfaceVariant),

            primaryDef = StyledColors.Definition(seeds.primary, pickOn(seeds.primary)),
            primarySurfaceDef = StyledColors.Definition(primarySurface, onSurface),

            secondaryDef = StyledColors.Definition(seeds.secondary, pickOn(seeds.secondary)),
            secondarySurfaceDef = StyledColors.Definition(secondarySurface, onSurface),

            outlineDef = StyledColors.Definition(outline, onSurface),
            outlineVariantDef = StyledColors.Definition(outlineVariant, onSurfaceVariant),

            errorDef = StyledColors.Definition(seeds.error, pickOn(seeds.error)),
            errorSurfaceDef = StyledColors.Definition(errorSurface, onSurface),

            successDef = StyledColors.Definition(seeds.success, pickOn(seeds.success)),
            successSurfaceDef = StyledColors.Definition(successSurface, onSurface),

            warningDef = StyledColors.Definition(seeds.warning, pickOn(seeds.warning)),
            warningSurfaceDef = StyledColors.Definition(warningSurface, onSurface),

            disableFactors = disable
        )
    }
}
