package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledCard
import com.michaelflisar.composestyled.foundation.components.FoundationCard
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

class Fluent2Card internal constructor(
    val variantMapping: VariantMapping,
) : StyledCard.Implementation {

    override val defaults: StyledCard.Defaults
        @Composable get() = StyledCard.Defaults(
            shape = StyledTheme.shapes.medium,
            contentPadding = PaddingValues(12.dp),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ) = remember(variantMapping) { Fluent2Card(variantMapping) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledCard.Variant) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant ->
            when (variant) {
                StyledCard.Variant.Auto,
                StyledCard.Variant.Filled -> Variant.Standard
                StyledCard.Variant.Outlined -> Variant.Alternative
            }
        }
    }

    enum class Variant { Standard, Alternative }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        variant: StyledCard.Variant,
        modifier: Modifier,
        shape: Shape,
        customization: StyledCard.Customization?,
        onClick: (() -> Unit)?,
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        content: @Composable () -> Unit,
    ) {
        val mappedVariant = variantMapping(variant)
        val colors = StyledTheme.colorScheme
        val customBackground = if (mappedVariant == Variant.Standard) {
            customization?.colors?.filledBackground?.resolve()
                ?.takeIf { it != Color.Unspecified }
        } else {
            null
        }
        val background = customBackground ?: colors.surface
        val customContent = if (mappedVariant == Variant.Standard) {
            customization?.colors?.filledForeground?.resolve()
        } else {
            customization?.colors?.textForeground?.resolve()
        }?.takeIf { it != Color.Unspecified }
        val customBorder = customization?.border?.resolve()?.takeIf { it != Color.Unspecified }
        FoundationCard(
            modifier = modifier,
            shape = shape,
            containerColor = background,
            contentColor = customContent ?: StyledTheme.contentColorFor(background),
            shadowElevation = customization?.elevation
                ?: if (mappedVariant == Variant.Standard) 2.dp else 0.dp,
            border = when {
                customBorder != null -> BorderStroke(1.dp, customBorder)
                mappedVariant == Variant.Alternative -> BorderStroke(1.dp, colors.outline)
                else -> BorderStroke(1.dp, colors.outlineVariant)
            },
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            content = content,
        )
    }
}
