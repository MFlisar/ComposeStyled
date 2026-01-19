package com.michaelflisar.composestyled.theme.material3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.theme.StyledColorUtil

object Material3Util {

    // ----------------
    // buttons
    // ----------------

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    fun resolveButtonColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization? = null
    ): StatefulBaseColorDef {
        val c = StyledTheme.colors
        return when (variant) {

            // Filled primary: state layer uses ON color over container (M3 state layer)
            StyledButton.Variant.Primary -> {
                val colors = BaseColor(
                    background = customization?.background?.resolve() ?: c.primary,
                    foreground = customization?.content?.resolve() ?: c.onPrimary,
                    border = customization?.border?.resolve()
                )
                val normal = BaseColorDef(colors)
                StatefulBaseColorDef(
                    normal = normal,
                    hovered = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.08f, dark = 0.10f),
                        applyToBackground = true
                    ),
                    focused = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.10f, dark = 0.12f),
                        applyToBackground = true
                    ),
                    pressed = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.12f, dark = 0.16f),
                        applyToBackground = true
                    ),
                    error = null
                )
            }

            // Tonal (secondary): state layer should be derived from foreground (onSecondarySurface)
            StyledButton.Variant.Secondary -> {
                val colors = BaseColor(
                    background = customization?.background?.resolve() ?: c.secondarySurface,
                    foreground = customization?.content?.resolve() ?: c.onSecondarySurface,
                    border = customization?.border?.resolve()
                )
                val normal = BaseColorDef(colors)
                StatefulBaseColorDef(
                    normal = normal,
                    hovered = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground, // ✅ M3: state layer uses on-color
                        alpha = StyledColorUtil.pick(colors.background, light = 0.08f, dark = 0.10f),
                        applyToBackground = true
                    ),
                    focused = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.10f, dark = 0.12f),
                        applyToBackground = true
                    ),
                    pressed = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.12f, dark = 0.16f),
                        applyToBackground = true
                    ),
                    error = null
                )
            }

            // Outlined: M3 uses state layer on container (even though container is transparent).
            // To ensure visibility, overlay comes from *foreground* (primary) like your old approach,
            // AND we slightly boost the border for hovered/focused.
            StyledButton.Variant.Outlined -> {
                val colors = BaseColor(
                    background = customization?.background?.resolve() ?: Color.Transparent,
                    foreground = customization?.content?.resolve() ?: c.primary,
                    border = customization?.border?.resolve() ?: c.outline
                )
                val normal = BaseColorDef(colors)

                StatefulBaseColorDef(
                    normal = normal,
                    hovered = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground, // ✅ old behavior, visible
                        alpha = StyledColorUtil.pick(colors.background, light = 0.06f, dark = 0.10f),
                        applyToBackground = true,
                        applyToForeground = false,
                        applyToBorder = false
                    ).let {
                        // subtle border emphasis on hover
                        StyledColorUtil.withBorder(it, c.outlineVariant.asColorRef())
                    },
                    focused = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.08f, dark = 0.12f),
                        applyToBackground = true
                    ).let {
                        // focused border goes to primary (M3-ish emphasis)
                        StyledColorUtil.withBorder(it, c.primary.asColorRef())
                    },
                    pressed = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.10f, dark = 0.16f),
                        applyToBackground = true
                    ),
                    error = null
                )
            }

            // Text: same as outlined but without border; visibility in dark guaranteed by colors.foreground-derived overlay
            StyledButton.Variant.Text -> {
                val colors = BaseColor(
                    background = customization?.background?.resolve() ?: Color.Transparent,
                    foreground = customization?.content?.resolve() ?: c.primary,
                    border = customization?.border?.resolve()
                )
                val normal = BaseColorDef(colors)
                StatefulBaseColorDef(
                    normal = normal,
                    hovered = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.06f, dark = 0.10f),
                        applyToBackground = true
                    ),
                    focused = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.08f, dark = 0.12f),
                        applyToBackground = true
                    ),
                    pressed = StyledColorUtil.overlay(
                        base = normal,
                        overlayColor = colors.foreground,
                        alpha = StyledColorUtil.pick(colors.background, light = 0.10f, dark = 0.16f),
                        applyToBackground = true
                    ),
                    error = null
                )
            }
        }
    }

    // ----------------
    // functions (old)
    // ----------------

    fun emphasizedColor(
        base: Color,
        surfaceTint: Color,
        emphasis: Emphasis,
    ): Color {
        val alpha = when (emphasis) {
            Emphasis.Low -> 0.05f
            Emphasis.Medium -> 0.08f
            Emphasis.High -> 0.12f
        }
        return surfaceTint.copy(alpha = alpha).compositeOver(base)
    }

    fun emphasisElevation(emphasis: Emphasis): Dp {
        return when (emphasis) {
            Emphasis.Low -> 0.dp
            Emphasis.Medium -> 4.dp
            Emphasis.High -> 8.dp
        }
    }
}