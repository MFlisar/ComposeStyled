package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledButtonTokenDefaultRenderer

@OptIn(InternalComposeStyledApi::class)
internal object StyledButtonImpl : UnstyledButtonTokenDefaultRenderer() {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {
        val c = StyledTheme.colors

        fun pressedOverlay(onTopOf: Color, alpha: Float) =
            onTopOf.copy(alpha = alpha) // simpel; besser: echte Overlay-Mischung, wenn du willst

        // Primary = tinted text (Standard iOS)
        val primary = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = c.primary,              // iOS: systemBlue / tint
                border = null
            ),
            hovered = BaseColorDef(
                background = pressedOverlay(c.primary, 0.08f), // optional (Pointer)
                foreground = c.primary,
                border = null
            )
            // wenn du später "pressed" einführst: background/foreground einfach per Alpha dimmen
        )

        // Secondary = weaker tinted/label
        val secondary = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = c.seondary,    // iOS: secondaryLabel
                border = null
            ),
            hovered = BaseColorDef(
                background = pressedOverlay(c.seondary, 0.08f),
                foreground = c.seondary,
                border = null
            )
        )

        // Outlined = bordered (optional iOS 15+)
        val outlined = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = c.onBackground,
                border = c.outline              // iOS: separator
            ),
            hovered = BaseColorDef(
                background = pressedOverlay(c.onBackground, 0.06f),
                foreground = c.onBackground,
                border = c.outline
            )
        )

        // Text = plain label (neutral)
        val text = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = c.onBackground,             // iOS: label
                border = null
            ),
            hovered = BaseColorDef(
                background = pressedOverlay(c.onBackground, 0.06f),
                foreground = c.onBackground,
                border = null
            )
        )

        StyledButton.registerVariantStyles(
            primary = primary,
            secondary = secondary,
            outlined = outlined,
            text = text
        )
    }
}
