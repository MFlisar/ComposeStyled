package com.michaelflisar.composestyled.theme.fluent2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.theme.StyledColorUtil

object Fluent2Util {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    fun resolveButtonColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?= null,
    ): StatefulBaseColorDef {
        val c = StyledTheme.colors
        return when (variant) {
            StyledButton.Variant.Prominent -> {
                val colors = BaseColor(
                    background = customization?.tint?.resolve() ?: c.primary,
                    foreground = customization?.onTint?.resolve() ?: c.onPrimary,
                    border = customization?.border?.resolve()
                )
                val normal = BaseColorDef(colors)
                StatefulBaseColorDef(
                    normal = normal,
                    hovered = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground, // highlight from on-primary
                        alpha = StyledColorUtil.pick(colors.background, light = 0.06f, dark = 0.08f),
                        applyToBackground = true
                    ),
                    focused = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.08f, dark = 0.10f),
                        applyToBackground = true
                    ),
                    pressed = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.10f, dark = 0.14f),
                        applyToBackground = true
                    ),
                    error = null
                )
            }
            StyledButton.Variant.Normal -> {
                val colors = BaseColor(
                    background = customization?.tint?.resolve() ?: c.surfaceVariant,
                    foreground = customization?.onTint?.resolve() ?: c.onSurfaceVariant,
                    border = customization?.border?.resolve() ?: c.outlineVariant
                )
                val normal = BaseColorDef(colors)

                val hovered = StyledColorUtil.overlay(
                    base = normal,
                    overlayColor = c.onSurface, // neutral highlight
                    alpha = StyledColorUtil.pick(colors.background, light = 0.03f, dark = 0.05f),
                    applyToBackground = true
                ).let {
                    // stroke a bit stronger on hover
                    StyledColorUtil.withBorder(it, c.outline.asColorRef())
                }

                val focused = StyledColorUtil.overlay(
                    base = normal,
                    overlayColor = c.onSurface,
                    alpha = StyledColorUtil.pick(colors.background, light = 0.04f, dark = 0.06f),
                    applyToBackground = true
                ).let {
                    // focus border: accent hint (ring would be renderer-level, but this helps)
                    StyledColorUtil.withBorder(it, c.primary.asColorRef())
                }

                val pressed = StyledColorUtil.overlay(
                    base = normal,
                    overlayColor = c.onSurface,
                    alpha = StyledColorUtil.pick(colors.background, light = 0.06f, dark = 0.10f),
                    applyToBackground = true
                ).let {
                    // pressed: slightly dim content too
                    StyledColorUtil.dim(it, foregroundAlpha = 0.85f)
                }

                StatefulBaseColorDef(
                    normal = normal,
                    hovered = hovered,
                    focused = focused,
                    pressed = pressed,
                    error = null
                )
            }
            StyledButton.Variant.Outlined -> {
                val colors = BaseColor(
                    background = Color.Transparent,
                    foreground = customization?.tint?.resolve() ?: c.onSurface,
                    border = customization?.border?.resolve() ?: c.outline
                )
                val normal = BaseColorDef(colors)

                val hovered = StyledColorUtil.overlay(
                    base = normal,
                    overlayColor = c.onSurface,
                    alpha = StyledColorUtil.pick(colors.background, light = 0.03f, dark = 0.06f),
                    applyToBackground = true
                ).let { StyledColorUtil.withBorder(it, c.outlineVariant.asColorRef()) }

                val focused = StyledColorUtil.overlay(
                    base = normal,
                    overlayColor = c.onSurface,
                    alpha = StyledColorUtil.pick(colors.background, light = 0.04f, dark = 0.07f),
                    applyToBackground = true
                ).let { StyledColorUtil.withBorder(it, c.primary.asColorRef()) }

                val pressed = StyledColorUtil.overlay(
                    base = normal,
                    overlayColor = c.onSurface,
                    alpha = StyledColorUtil.pick(colors.background, light = 0.06f, dark = 0.10f),
                    applyToBackground = true
                ).let { StyledColorUtil.dim(it, foregroundAlpha = 0.85f) }

                StatefulBaseColorDef(
                    normal = normal,
                    hovered = hovered,
                    focused = focused,
                    pressed = pressed,
                    error = null
                )
            }
            StyledButton.Variant.Text -> {
                val colors = BaseColor(
                    background = Color.Transparent,
                    foreground = customization?.tint?.resolve() ?: c.primary,
                    border = customization?.border?.resolve()
                )
                val normal = BaseColorDef(colors)
                StatefulBaseColorDef(
                    normal = normal,
                    // Fluent commands often change emphasis more than background
                    hovered = StyledColorUtil.dim(normal, foregroundAlpha = 0.92f),
                    focused = StyledColorUtil.dim(normal, foregroundAlpha = 0.92f),
                    pressed = StyledColorUtil.dim(normal, foregroundAlpha = 0.70f),
                    error = null
                )
            }
        }
    }

    /**
     * Fluent2-style emphasis:
     * - weniger Farbe als Material3
     * - mehr neutrale Surface-Variation
     * - Dark: klarer Helligkeitsshift, kaum Tint
     */
    fun emphasizedColor(
        base: Color,
        surfaceTint: Color,
        emphasis: Emphasis,
        isDark: Boolean,
    ): Color {
        return if (!isDark) {
            // Light: sehr subtiler Tint, eher "surface variation"
            val alpha = when (emphasis) {
                Emphasis.Low -> 0.03f
                Emphasis.Medium -> 0.06f
                Emphasis.High -> 0.09f
            }
            surfaceTint.copy(alpha = alpha).compositeOver(base)
        } else {
            // Dark: sichtbarer Lift, sehr wenig Farbe
            val lift = when (emphasis) {
                Emphasis.Low -> 0.05f
                Emphasis.Medium -> 0.08f
                Emphasis.High -> 0.11f
            }

            val overlayAlpha = when (emphasis) {
                Emphasis.Low -> 0.02f
                Emphasis.Medium -> 0.03f
                Emphasis.High -> 0.035f
            }

            val lerpFactor = when (emphasis) {
                Emphasis.Low -> 0.02f
                Emphasis.Medium -> 0.03f
                Emphasis.High -> 0.04f
            }

            val lifted = lerp(base, Color.White, lift)
            val toned = surfaceTint.copy(alpha = overlayAlpha).compositeOver(lifted)
            lerp(toned, surfaceTint, lerpFactor)
        }
    }
}