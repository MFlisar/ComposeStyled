package com.michaelflisar.composestyled.theme.cupertino

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.theme.StyledColorUtil

object CupertinoUtil {

    @Composable
    @ReadOnlyComposable
    fun resolveButtonColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization? = null,
    ): StatefulBaseColorDef {
        val c = StyledTheme.colors
        return when (variant) {

            // iOS primary: tinted text
            StyledButton.Variant.Prominent -> plain(c.primary, customization)

            // iOS secondary: label-like / less emphasis
            StyledButton.Variant.Normal -> plain(c.onSurfaceVariant, customization)

            // bordered style
            StyledButton.Variant.Outlined -> bordered(c.primary, c.outlineVariant, customization)

            // same as primary (plain tinted)
            StyledButton.Variant.Text -> plain(c.primary, customization)
        }
    }

    // iOS-like: states mainly through foreground dimming, not heavy bg layers
    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    private fun plain(
        foreground: Color,
        customization: StyledButton.Customization?,
    ): StatefulBaseColorDef {
        val colors = BaseColor(
            background = Color.Transparent,
            foreground = customization?.tint?.resolve() ?: foreground,
            border = customization?.border?.resolve()
        )
        val normal = BaseColorDef(colors)
        return StatefulBaseColorDef(
            normal = normal,
            hovered = StyledColorUtil.dim(normal, foregroundAlpha = 0.90f),
            focused = StyledColorUtil.dim(normal, foregroundAlpha = 0.90f),
            pressed = StyledColorUtil.dim(normal, foregroundAlpha = 0.65f),
            error = null
        )
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    private fun bordered(
        foreground: Color,
        border: Color,
        customization: StyledButton.Customization?,
    ): StatefulBaseColorDef {
        val colors = BaseColor(
            background = Color.Transparent,
            foreground = customization?.tint?.resolve() ?: foreground,
            border = customization?.border?.resolve() ?: border
        )
        val normal = BaseColorDef(colors)
        val bg = normal.background.resolve()
        val hovered = highlight(
            normal,
            StyledColorUtil.pick(bg, light = 0.02f, dark = 0.04f)
        )

        val focused = highlight(
            normal,
            StyledColorUtil.pick(bg, light = 0.03f, dark = 0.05f)
        )

        val pressed = StyledColorUtil.dim(
            highlight(
                normal,
                StyledColorUtil.pick(bg, light = 0.05f, dark = 0.08f)
            ),
            foregroundAlpha = 0.70f
        )

        return StatefulBaseColorDef(
            normal = normal,
            hovered = hovered,
            focused = focused,
            pressed = pressed,
            error = null
        )
    }

    // very subtle neutral highlight behind the content (optional, but feels iOS-y on bordered)
    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    private fun highlight(
        base: BaseColorDef,
        alpha: Float,
    ): BaseColorDef {
        val c = StyledTheme.colors
        val highlighted = StyledColorUtil.overlay(
            base = base,
            overlayColor = c.onSurface, // neutral highlight
            alpha = alpha,
            applyToBackground = true,
            applyToForeground = false,
            applyToBorder = false
        )
        // keep the dimming for pressed
        return highlighted
    }

    @Composable
    fun emphasizedColor(
        base: Color,
        surfaceTint: Color,
        emphasis: Emphasis,
        isDark: Boolean,
    ): Color {
        // Sehr subtile Werte, iOS-like
        return if (!isDark) {
            val a = when (emphasis) {
                Emphasis.Low -> 0.02f
                Emphasis.Medium -> 0.03f
                Emphasis.High -> 0.04f
            }
            surfaceTint.copy(alpha = a).compositeOver(base)
        } else {
            val lift = when (emphasis) {
                Emphasis.Low -> 0.02f
                Emphasis.Medium -> 0.035f
                Emphasis.High -> 0.05f
            }
            val lifted = androidx.compose.ui.graphics.lerp(base, Color.White, lift)
            // minimal tint, sonst wird’s „androidy“
            surfaceTint.copy(alpha = 0.015f).compositeOver(lifted)
        }
    }
}