package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledCardRenderer
import com.michaelflisar.composestyled.theme.material3.Material3Util

@OptIn(InternalComposeStyledApi::class)
internal object StyledCardImpl : UnstyledCardRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledCard.Variant,
        customization: StyledCard.Customization?,
    ): StatefulBaseColorDef {
        val background = customization?.background?.resolve()
        val foreground = customization?.content?.resolve()
        val border = customization?.border?.resolve()
        val colors = StyledTheme.colors
        return remember(variant, background, foreground, border, colors) {
            when (variant) {
                StyledCard.Variant.Filled -> {
                    val bg = background ?: colors.surface
                    val fg = foreground ?: colors.onSurface
                    val b = border
                    StatefulBaseColorDef(
                        normal = BaseColorDef(bg, fg, b),
                    )
                }

                StyledCard.Variant.Outlined -> {
                    val bg = background ?: Color.Transparent
                    val fg = foreground ?: colors.onSurface
                    val b = border ?: colors.outlineVariant
                    StatefulBaseColorDef(
                        normal = BaseColorDef(bg, fg, b),
                    )
                }
            }
        }
    }

    @Composable
    override fun calcBackgroundColor(backgroundColor: Color, emphasis: Emphasis): Color {
        val surface = StyledTheme.colors.surface
        val bg by remember(backgroundColor, surface, emphasis) {
            derivedStateOf {
                Material3Util.emphasizedColor(backgroundColor, surface, emphasis)
            }
        }
        return bg
    }

    @Composable
    override fun calcElevation(emphasis: Emphasis): Dp {
        return Material3Util.emphasisElevation(emphasis)
    }
}
