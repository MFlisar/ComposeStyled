package com.michaelflisar.composestyled.theme.cupertino.components

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
import com.michaelflisar.composestyled.theme.cupertino.CupertinoUtil

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

            // Cupertino "Filled": wie grouped cell background
            StyledCard.Variant.Filled -> {
                val baseBg = customization?.background?.resolve() ?: c.surface
                val bg =
                    if (customization?.background != null) baseBg else CupertinoUtil.emphasizedColor(
                        baseBg,
                        c.primary,
                        emphasis,
                        c.isDarkTheme
                    )

                val fg = customization?.content?.resolve() ?: c.onSurface
                val b = customization?.border?.resolve() // standard: kein border bei filled
                StatefulBaseColorDef(normal = BaseColorDef(bg, fg, b))
            }

            // Cupertino "Outlined": transparent oder nahezu transparent + separator line
            StyledCard.Variant.Outlined -> {
                val bg = customization?.background?.resolve() ?: Color.Transparent
                val fg = customization?.content?.resolve() ?: c.onSurface

                // iOS: separator ist sehr subtil; outlineVariant ist oft zu stark.
                val defaultSeparator = c.outlineVariant.copy(alpha = 0.55f)
                val b = customization?.border?.resolve() ?: defaultSeparator

                StatefulBaseColorDef(normal = BaseColorDef(bg, fg, b))
            }
        }
    }

    @Composable
    override fun resolveElevation(
        variant: StyledCard.Variant,
        emphasis: Emphasis,
        customization: StyledCard.Customization?,
    ): Dp = 0.dp

    @Composable
    override fun resolveBorderStroke(
        variant: StyledCard.Variant,
        emphasis: Emphasis,
        colors: BaseColor,
        customization: StyledCard.Customization?,
    ): BorderStroke? {
        return when (variant) {
            StyledCard.Variant.Filled -> {
                // iOS: meist kein expliziter stroke bei filled/grouped cards
                null
            }

            StyledCard.Variant.Outlined -> {
                colors.border?.let {
                    // iOS separator wirkt dünn; 1dp ist ok, wenn alpha reduziert ist (s.o.)
                    BorderStroke(1.dp, it)
                }
            }
        }
    }
}
