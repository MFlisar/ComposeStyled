package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.theme.material3.Material3Util
import com.michaelflisar.composestyled.shared.UnstyledButtonTokenDefaultRenderer

@OptIn(InternalComposeStyledApi::class)
internal object StyledButtonImpl : UnstyledButtonTokenDefaultRenderer() {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {

        val colors = StyledTheme.colors

        val buttonFilledPrimary = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = null
            ),
            hovered = BaseColorDef(
                background = Material3Util.hoverFromBackground(colors.primary),
                foreground = colors.onPrimary,
                border = null
            )
        )
        val buttonFilledSecondary = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.seondary,
                foreground = colors.onSecondary,
                border = null
            ),
            hovered = BaseColorDef(
                background = Material3Util.hoverFromBackground(colors.seondary),
                foreground = colors.onSecondary,
                border = null
            )
        )
        val buttonOutlined = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onBackground,
                border = colors.outlineVariant
            ),
            hovered = BaseColorDef(
                background = Material3Util.hoverFromForeground(colors.onBackground),
                foreground = colors.onBackground,
                border = colors.outlineVariant
            ),
        )
        val buttonText = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onBackground,
                border = null
            ),
            hovered = BaseColorDef(
                background = Material3Util.hoverFromForeground(colors.onBackground),
                foreground = colors.onBackground,
                border = null
            )
        )

        StyledButton.registerVariantStyles(
            primary = buttonFilledPrimary,
            secondary = buttonFilledSecondary,
            outlined = buttonOutlined,
            text = buttonText
        )
    }
}