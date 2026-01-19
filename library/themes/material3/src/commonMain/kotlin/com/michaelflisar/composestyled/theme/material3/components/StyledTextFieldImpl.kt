package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledTextField
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledTextFieldRenderer

@OptIn(InternalComposeStyledApi::class)
internal object StyledTextFieldImpl : UnstyledTextFieldRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledTextField.Variant,
        customization: StyledTextField.Customization?,
    ): StatefulBaseColorDef {
        val background = customization?.background?.resolve()
        val foreground = customization?.content?.resolve()
        val border = customization?.border?.resolve()
        val colors = StyledTheme.colors
        return remember(variant, background, foreground, border, colors) {
            when (variant) {
                StyledTextField.Variant.Filled -> {
                    val bg = background ?: colors.surface
                    val fg = foreground ?: colors.onSurface
                    val b = border
                    StatefulBaseColorDef(
                        normal = BaseColorDef(bg, fg, b),
                        focused = BaseColorDef(bg, fg, border ?: colors.primary),
                        error = BaseColorDef(bg, fg, border ?: colors.error),
                    )
                }

                StyledTextField.Variant.Outlined -> {
                    val bg = background ?: Color.Transparent
                    val fg = foreground ?: colors.onSurface
                    val b = border ?: colors.outlineVariant
                    StatefulBaseColorDef(
                        normal = BaseColorDef(bg, fg, b),
                        focused = BaseColorDef(bg, fg, border ?: colors.primary),
                        error = BaseColorDef(bg, fg, border ?: colors.error),
                    )
                }
            }
        }
    }
}
