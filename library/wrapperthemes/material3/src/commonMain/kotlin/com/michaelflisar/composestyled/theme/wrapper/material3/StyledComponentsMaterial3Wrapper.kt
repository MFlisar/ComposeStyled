package com.michaelflisar.composestyled.theme.wrapper.material3

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledSeparatorImpl
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.wrapper.material3.components.StyledTextImpl

val StyledComponentsMaterial3Wrapper = StyledWrapperComponents(
    root = { content ->
        val colors = StyledTheme.colors
        val shapes = StyledTheme.shapes
        val typography = StyledTheme.typography
        val cs = colors.toMaterial3ColorScheme(colors.isDarkTheme)
        val t = typography.toMaterial3Typography()
        val s = shapes.toMaterial3Shapes()

        MaterialTheme(
            colorScheme = cs,
            typography = t,
            shapes = s,
        ) {
            // Keep core locals aligned with MaterialTheme by default
            ProvideStyledLocals(
                contentColor = cs.onBackground,
                backgroundColor = cs.background,
                content = content,
                applyTransparentBackgroundColor = true // here we save the background color even if it's transparent
            )
        }
    },
    surface = StyledSurfaceImpl,
    button = StyledButtonImpl,
    card = StyledCardImpl,
    checkbox = StyledCheckboxImpl,
    icon = StyledIconImpl,
    separator = StyledSeparatorImpl,
    text = StyledTextImpl,
    textField = StyledTextFieldImpl,
)


private fun StyledColors.toMaterial3ColorScheme(
    isDarkTheme: Boolean,
) = if (isDarkTheme) {
    darkColorScheme(
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        primary = primary,
        onPrimary = onPrimary,
        secondary = seondary,
        onSecondary = onSecondary,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError,
    )
} else {
    lightColorScheme(
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        primary = primary,
        onPrimary = onPrimary,
        secondary = seondary,
        onSecondary = onSecondary,
        outline = outline,
        outlineVariant = outlineVariant,
        error = error,
        onError = onError,
    )
}

private fun StyledTypography.toMaterial3Typography(): Typography =
    Typography(
        bodyLarge = this.bodyLarge,
        bodyMedium = this.bodyMedium,
        bodySmall = this.bodySmall,
        titleLarge = this.titleLarge,
        titleMedium = this.titleMedium,
        titleSmall = this.titleSmall,
        labelLarge = this.labelLarge,
        labelMedium = this.labelMedium,
        labelSmall = this.labelSmall,
    )

private fun StyledShapes.toMaterial3Shapes(): Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(this.small * .8f),
        small = RoundedCornerShape(this.small),
        medium = RoundedCornerShape(this.medium),
        large = RoundedCornerShape(this.large),
        extraLarge = RoundedCornerShape(this.large * 1.2f)
    )