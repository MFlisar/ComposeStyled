package com.michaelflisar.composestyled.themes

import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.contrastRatio
import com.michaelflisar.composestyled.onColorByContrastRatio
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.StyledColorScheme
import com.michaelflisar.composestyled.tokens.darkDisableFactors
import com.michaelflisar.composestyled.tokens.lightDisableFactors

@InternalComposeStyledApi
fun createStyledThemeColors(
    themeId: String,
    id: String,
    name: String,
    primaryLight: Color,
    primaryDark: Color,
    secondaryLight: Color,
    secondaryDark: Color,
    tertiaryLight: Color,
    tertiaryDark: Color,
): StyledThemeColors {
    val monochromeLight = AccentColors(
        primary = primaryLight,
        secondary = blend(primaryLight, Color.Black, 0.18f),
        tertiary = blend(primaryLight, Color.Black, 0.34f),
    )
    val monochromeDark = AccentColors(
        primary = primaryDark,
        secondary = blend(primaryDark, Color.White, 0.16f),
        tertiary = blend(primaryDark, Color.White, 0.32f),
    )
    val colorfulLight = AccentColors(primaryLight, secondaryLight, tertiaryLight)
    val colorfulDark = AccentColors(primaryDark, secondaryDark, tertiaryDark)

    return StyledThemeColors(
        themeId = themeId,
        schemeId = id,
        name = name,
        monochrome = schemes(monochromeLight, monochromeDark),
        colorful = schemes(colorfulLight, colorfulDark),
    )
}

private data class AccentColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
)

private fun schemes(
    light: AccentColors,
    dark: AccentColors,
) = StyledThemeColorSchemes(
    lightNormal = scheme(false, StyledContrastLevel.Normal, light),
    lightMedium = scheme(false, StyledContrastLevel.Medium, light),
    lightHigh = scheme(false, StyledContrastLevel.High, light),
    darkNormal = scheme(true, StyledContrastLevel.Normal, dark),
    darkMedium = scheme(true, StyledContrastLevel.Medium, dark),
    darkHigh = scheme(true, StyledContrastLevel.High, dark),
)

private fun scheme(
    isDark: Boolean,
    contrast: StyledContrastLevel,
    accents: AccentColors,
): StyledColorScheme {
    val surface = if (isDark) Color(0xFF1B2028) else Color.White
    val background = blend(surface, accents.primary, if (isDark) 0.035f else 0.025f)
    val surfaceVariant = blend(surface, accents.primary, if (isDark) 0.12f else 0.075f)
    val primarySurface = blend(surface, accents.primary, if (isDark) 0.34f else 0.14f)
    val secondarySurface = blend(surface, accents.secondary, if (isDark) 0.30f else 0.13f)
    val tertiarySurface = blend(surface, accents.tertiary, if (isDark) 0.30f else 0.13f)
    val error = tintStatus(Color(0xFFB3261E), accents.primary)
    val errorSurface = blend(surface, error, if (isDark) 0.36f else 0.13f)
    val success = tintStatus(Color(0xFF176B36), accents.primary)
    val successSurface = blend(surface, success, if (isDark) 0.32f else 0.13f)
    val warning = tintStatus(Color(0xFF825500), accents.primary)
    val warningSurface = blend(surface, warning, if (isDark) 0.32f else 0.13f)

    val backgroundDef = definition(background, contrast)
    val surfaceDef = definition(surface, contrast)
    val surfaceVariantDef = definition(surfaceVariant, contrast)
    val primaryDef = definition(accents.primary, contrast)
    val primarySurfaceDef = definition(primarySurface, contrast)
    val secondaryDef = definition(accents.secondary, contrast)
    val secondarySurfaceDef = definition(secondarySurface, contrast)
    val tertiaryDef = definition(accents.tertiary, contrast)
    val tertiarySurfaceDef = definition(tertiarySurface, contrast)
    val errorDef = definition(error, contrast)
    val errorSurfaceDef = definition(errorSurface, contrast)
    val successDef = definition(success, contrast)
    val successSurfaceDef = definition(successSurface, contrast)
    val warningDef = definition(warning, contrast)
    val warningSurfaceDef = definition(warningSurface, contrast)
    val outline = if (isDark) Color(0xFF938F99) else Color(0xFF79747E)
    val outlineVariant = if (isDark) Color(0xFF49454F) else Color(0xFFCAC4D0)

    return StyledColorScheme(
        isDarkTheme = isDark,
        backgroundDef = backgroundDef,
        surfaceDef = surfaceDef,
        surfaceVariantDef = surfaceVariantDef,
        primaryDef = primaryDef,
        primarySurfaceDef = primarySurfaceDef,
        secondaryDef = secondaryDef,
        secondarySurfaceDef = secondarySurfaceDef,
        tertiaryDef = tertiaryDef,
        tertiarySurfaceDef = tertiarySurfaceDef,
        outline = outline,
        outlineVariant = outlineVariant,
        errorDef = errorDef,
        errorSurfaceDef = errorSurfaceDef,
        successDef = successDef,
        successSurfaceDef = successSurfaceDef,
        warningDef = warningDef,
        warningSurfaceDef = warningSurfaceDef,
        disableFactors = if (isDark) darkDisableFactors() else lightDisableFactors(),
    )
}

private fun definition(
    originalColor: Color,
    contrast: StyledContrastLevel,
): StyledColorScheme.Definition {
    val blackContrast = originalColor.contrastRatio(Color.Black)
    val whiteContrast = originalColor.contrastRatio(Color.White)
    val onColor = originalColor.onColorByContrastRatio()
    val target = contrast.targetContrastRatio + 0.05f

    if (maxOf(blackContrast, whiteContrast) >= target) {
        return StyledColorScheme.Definition(originalColor, onColor)
    }

    val opposite = if (onColor == Color.Black) Color.White else Color.Black
    var low = 0f
    var high = 1f
    repeat(24) {
        val amount = (low + high) / 2f
        if (blend(originalColor, opposite, amount).contrastRatio(onColor) >= target) {
            high = amount
        } else {
            low = amount
        }
    }
    return StyledColorScheme.Definition(blend(originalColor, opposite, high), onColor)
}

private fun tintStatus(status: Color, primary: Color) = blend(status, primary, 0.06f)

private fun blend(color: Color, target: Color, amount: Float) = Color(
    red = color.red + (target.red - color.red) * amount,
    green = color.green + (target.green - color.green) * amount,
    blue = color.blue + (target.blue - color.blue) * amount,
    alpha = color.alpha,
)
