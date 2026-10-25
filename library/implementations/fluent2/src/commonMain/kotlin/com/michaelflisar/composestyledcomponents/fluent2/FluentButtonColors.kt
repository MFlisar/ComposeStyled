package com.michaelflisar.composestyledcomponents.fluent2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.ColorToken
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.darken
import com.michaelflisar.composestyledcomponents.lighten

@Immutable
internal class FluentButtonColors(
    val background: Color,
    val foreground: Color,
    val border: Color,
) {
    enum class Variant { Primary, Secondary, Subtle, Outline, Transparent, Standard, IconOutline }

    companion object {
        @Composable
        fun resolve(
            variant: Variant,
            enabled: Boolean,
            hovered: Boolean,
            pressed: Boolean,
            colors: CustomColors?,
            border: ColorToken?,
        ): FluentButtonColors {
            val theme = StyledTheme.colorScheme
            val dark = theme.isDarkTheme
            val primary = variant == Variant.Primary
            val secondary = variant == Variant.Secondary
            val outlined = variant == Variant.Outline || variant == Variant.IconOutline
            val filled = primary || secondary
            val neutralForeground = if (dark) Color.White else Color(0xFF242424)
            val neutralSecondaryForeground = if (dark) Color(0xFFD6D6D6) else Color(0xFF424242)
            // Keep overlay RGB constant to avoid flashes when animating from transparent.

            val overlay = if (dark) Color.White else Color.Black
            val idleBackground = overlay.copy(alpha = 0f)

            if (!enabled) {
                return FluentButtonColors(
                    background = if (filled) {
                        if (dark) Color(0xFF141414) else Color(0xFFF0F0F0)
                    } else idleBackground,
                    foreground = if (dark) Color(0xFF5C5C5C) else Color(0xFFBDBDBD),
                    border = if (secondary || outlined) {
                        if (dark) Color(0xFF424242) else Color(0xFFE0E0E0)
                    } else Color.Transparent,
                )
            }

            val filledBackground = colors?.filledBackground?.resolve()?.takeIf { it != Color.Unspecified }
            val filledForeground = colors?.filledForeground?.resolve()?.takeIf { it != Color.Unspecified }
            val textForeground = colors?.textForeground?.resolve()?.takeIf { it != Color.Unspecified }
            val customBorder = border?.resolve()?.takeIf { it != Color.Unspecified }
            val baseBackground = when {
                primary -> filledBackground ?: theme.primary
                secondary -> filledBackground ?: if (dark) Color(0xFF292929) else Color.White
                else -> idleBackground
            }
            val baseForeground = when (variant) {
                Variant.Primary -> filledForeground ?: theme.onPrimary
                Variant.Secondary -> filledForeground ?: neutralForeground
                Variant.Outline -> textForeground ?: neutralForeground
                Variant.Standard, Variant.IconOutline -> textForeground ?: theme.primary
                Variant.Subtle, Variant.Transparent -> textForeground ?: neutralSecondaryForeground
            }
            val baseBorder = if (secondary || outlined) {
                customBorder ?: if (dark) Color(0xFF666666) else Color(0xFFD1D1D1)
            } else Color.Transparent
            val active = pressed || hovered
            val background = when {
                !active -> baseBackground
                primary -> when {
                    pressed -> baseBackground.darken(0.24f)
                    dark -> baseBackground.lighten(0.10f)
                    else -> baseBackground.darken(0.10f)
                }
                secondary -> if (dark) {
                    if (pressed) baseBackground.darken(0.25f) else baseBackground.lighten(0.09f)
                } else baseBackground.darken(if (pressed) 0.12f else 0.04f)
                else -> overlay.copy(
                    alpha = if (pressed) {
                        if (dark) 0.10f else 0.12f
                    } else {
                        if (dark) 0.06f else 0.04f
                    },
                )
            }
            val foreground = when {
                active && variant == Variant.Transparent ->
                    textForeground ?: if (pressed) theme.primary.darken(0.24f) else theme.primary
                active && variant == Variant.Subtle -> textForeground ?: neutralForeground
                else -> baseForeground
            }
            val stroke = if (active && (secondary || outlined) && customBorder == null) {
                if (dark) baseBorder.lighten(0.10f)
                else baseBorder.darken(if (pressed) 0.14f else 0.05f)
            } else baseBorder
            return FluentButtonColors(background, foreground, stroke)
        }
    }
}
