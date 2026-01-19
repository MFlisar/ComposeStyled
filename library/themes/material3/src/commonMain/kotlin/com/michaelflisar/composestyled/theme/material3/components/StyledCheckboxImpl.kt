package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledCheckbox
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledCheckboxRenderer

@OptIn(InternalComposeStyledApi::class)
internal object StyledCheckboxImpl : UnstyledCheckboxRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledCheckbox.Variant,
        checked: Boolean,
        customization: StyledCheckbox.Customization?,
    ): StatefulBaseColorDef {

        val colorChecked = customization?.checked?.resolve()
        val colorUnchecked = customization?.unchecked?.resolve()
        val colors = StyledTheme.colors
        return remember(variant, checked, colorChecked, colorUnchecked, colors, checked) {
            if (checked) {
                val bg = colors.primary
                val fg = colors.onPrimary
                val b = colors.primary
                StatefulBaseColorDef(
                    normal = BaseColorDef(bg, fg, b),
                    focused = BaseColorDef(bg, fg, b),
                    error = BaseColorDef(bg, fg, colors.error)
                )
            } else {
                val bg = Color.Transparent
                val fg = colors.onSurface
                StatefulBaseColorDef(
                    normal = BaseColorDef(bg, fg, colors.outline),
                    focused = BaseColorDef(bg, fg, colors.primary),
                    error = BaseColorDef(bg, fg, colors.error)
                )
            }
        }
    }
}
