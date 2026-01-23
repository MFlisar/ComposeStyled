package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
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
        emphasis: Emphasis,
        customization: StyledCard.Customization?,
    ): StatefulBaseColorDef {
        val c = StyledTheme.colors
        return when (variant) {
            StyledCard.Variant.Filled -> {
                val background = customization?.background?.resolve() ?: c.surface
                val bg = Material3Util.emphasizedColor(background, c.primary, emphasis, c.isDarkTheme, boostTintInDark = true)
                val fg = customization?.content?.resolve() ?: c.onSurface
                val b = customization?.border?.resolve()
                StatefulBaseColorDef(
                    normal = BaseColorDef(bg, fg, b),
                )
            }

            StyledCard.Variant.Outlined -> {
                val background = customization?.background?.resolve() ?: Color.Transparent
                val bg = if (background == Color.Transparent) {
                    background
                } else Material3Util.emphasizedColor(background, c.primary, emphasis, c.isDarkTheme, boostTintInDark = false)
                val fg = customization?.content?.resolve() ?: c.onSurface
                val b = customization?.border?.resolve() ?: c.outlineVariant
                StatefulBaseColorDef(
                    normal = BaseColorDef(bg, fg, b),
                )
            }
        }
    }

    @Composable
    override fun resolveElevation(
        variant: StyledCard.Variant,
        emphasis: Emphasis,
        customization: StyledCard.Customization?,
    ): Dp {
        return when (variant) {
            StyledCard.Variant.Filled -> Material3Util.emphasisElevation(emphasis)
            StyledCard.Variant.Outlined -> 0.dp // outlined cards are transparent and have no elevation
        }
    }

    @Composable
    override fun resolveBorderStroke(
        variant: StyledCard.Variant,
        emphasis: Emphasis,
        colors: BaseColor,
        customization: StyledCard.Customization?,
    ): BorderStroke? {
        return when (variant) {
            StyledCard.Variant.Filled -> null
            StyledCard.Variant.Outlined -> {
                colors.border?.let { BorderStroke(1.dp, it) }
            }
        }
    }
}
