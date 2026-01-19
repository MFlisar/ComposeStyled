package com.michaelflisar.composestyled.theme.fluent2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.theme.StyledColorUtil

object Fluent2Util {

    @Composable
    @ReadOnlyComposable
    fun resolveButtonColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?= null,
    ): StatefulBaseColorDef {
        return when (variant) {
            StyledButton.Variant.Primary -> accentFilled(customization)
            StyledButton.Variant.Secondary -> subtle(customization)
            StyledButton.Variant.Outlined -> outline(customization)
            StyledButton.Variant.Text -> textCommand(customization)
        }
    }

    // Fluent: subtle neutral highlight + stroke emphasis; focus often handled via focus ring
    // (ring is renderer-level; here we emphasize border).
    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    private fun accentFilled(customization: StyledButton.Customization?): StatefulBaseColorDef {
        val c = StyledTheme.colors
        val colors = BaseColor(
            background = customization?.background?.resolve() ?: c.primary,
            foreground = customization?.content?.resolve() ?: c.onPrimary,
            border = customization?.border?.resolve()
        )
        val normal = BaseColorDef(colors)
        return StatefulBaseColorDef(
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

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    private fun subtle(customization: StyledButton.Customization?): StatefulBaseColorDef {
        val c = StyledTheme.colors
        val colors = BaseColor(
            background = customization?.background?.resolve() ?: c.surfaceVariant,
            foreground = customization?.content?.resolve() ?: c.onSurfaceVariant,
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

        return StatefulBaseColorDef(
            normal = normal,
            hovered = hovered,
            focused = focused,
            pressed = pressed,
            error = null
        )
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    private fun outline(customization: StyledButton.Customization?): StatefulBaseColorDef {
        val c = StyledTheme.colors
        val colors = BaseColor(
            background = customization?.background?.resolve() ?: Color.Transparent,
            foreground = customization?.content?.resolve() ?: c.onSurface,
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

        return StatefulBaseColorDef(
            normal = normal,
            hovered = hovered,
            focused = focused,
            pressed = pressed,
            error = null
        )
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    @ReadOnlyComposable
    private fun textCommand(customization: StyledButton.Customization?): StatefulBaseColorDef {
        val c = StyledTheme.colors
        val colors = BaseColor(
            background = customization?.background?.resolve() ?: Color.Transparent,
            foreground = customization?.content?.resolve() ?: c.primary,
            border = customization?.border?.resolve()
        )
        val normal = BaseColorDef(colors)
        return StatefulBaseColorDef(
            normal = normal,
            // Fluent commands often change emphasis more than background
            hovered = StyledColorUtil.dim(normal, foregroundAlpha = 0.92f),
            focused = StyledColorUtil.dim(normal, foregroundAlpha = 0.92f),
            pressed = StyledColorUtil.dim(normal, foregroundAlpha = 0.70f),
            error = null
        )
    }
}