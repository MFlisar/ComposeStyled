package com.michaelflisar.composestyled.theme.fluent2.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledCardRenderer
import com.michaelflisar.composestyled.theme.fluent2.Fluent2Util

@OptIn(InternalComposeStyledApi::class)
internal object StyledCardImpl : UnstyledCardRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledCard.Variant,
        emphasis: Emphasis,
        customization: StyledCard.Customization?,
    ): StatefulBaseColorDef {
        val c = StyledTheme.colors

        return when (variant) {

            // Fluent2 Filled: neutrale Fläche, bei Emphasis leicht akzentuiert (sehr subtil)
            StyledCard.Variant.Filled -> {
                val baseBg = customization?.background?.resolve() ?: c.surface
                val bg = if (customization?.background != null) {
                    baseBg
                } else {
                    // Fluent2: eher "surface variations" als starker Tint.
                    // Wenn du tokens wie surfaceContainerX hast, nutze die hier.
                    // Fallback: minimaler Tint + minimaler Lift in dark (über Util)
                    Fluent2Util.emphasizedColor(
                        base = baseBg,
                        surfaceTint = c.primary,
                        emphasis = emphasis,
                        isDark = c.isDarkTheme
                    )
                }

                val fg = customization?.content?.resolve() ?: c.onSurface

                // Fluent: auch bei Filled oft ein sehr subtiler stroke möglich,
                // aber nur wenn Theme das will. Hier: default null (wie bei dir in Cupertino),
                // Outlined regelt den sichtbaren Stroke.
                val b = customization?.border?.resolve()

                StatefulBaseColorDef(normal = BaseColorDef(bg, fg, b))
            }

            // Fluent2 Outlined: transparent/neutral + klarer stroke
            StyledCard.Variant.Outlined -> {
                val bg = customization?.background?.resolve() ?: Color.Transparent
                val fg = customization?.content?.resolve() ?: c.onSurface

                // Fluent: stroke ist präsenter als iOS separator
                val defaultStroke = c.outlineVariant.copy(alpha = if (c.isDarkTheme) 0.70f else 0.85f)
                val b = customization?.border?.resolve() ?: defaultStroke

                StatefulBaseColorDef(normal = BaseColorDef(bg, fg, b))
            }
        }
    }

    @Composable
    override fun resolveElevation(
        variant: StyledCard.Variant,
        emphasis: Emphasis,
        customization: StyledCard.Customization?,
    ): Dp {
        // Fluent2: Shadow existiert, aber sehr subtil; oft eher über stroke/neutral surface.
        // Nur bei hoher emphasis ein bisschen elevation.
        return when (variant) {
            StyledCard.Variant.Outlined -> 0.dp
            StyledCard.Variant.Filled -> when (emphasis) {
                Emphasis.Low -> 0.dp
                Emphasis.Medium -> 1.dp
                Emphasis.High -> 2.dp
            }
        }
    }

    @Composable
    override fun resolveBorderStroke(
        variant: StyledCard.Variant,
        emphasis: Emphasis,
        colors: BaseColor,
        customization: StyledCard.Customization?,
    ): BorderStroke? {
        val strokeColor = colors.border

        return when (variant) {
            StyledCard.Variant.Filled -> {
                // Optional: Fluent "card" hat manchmal trotzdem einen hairline stroke.
                // Wenn du das willst: abhängig von emphasis aktivieren.
                if (strokeColor == null) null
                else when (emphasis) {
                    Emphasis.Low -> BorderStroke(1.dp, strokeColor.copy(alpha = 0.35f))
                    Emphasis.Medium -> BorderStroke(1.dp, strokeColor.copy(alpha = 0.50f))
                    Emphasis.High -> BorderStroke(1.dp, strokeColor.copy(alpha = 0.65f))
                }
            }

            StyledCard.Variant.Outlined -> {
                strokeColor?.let { BorderStroke(1.dp, it) }
            }
        }
    }
}
