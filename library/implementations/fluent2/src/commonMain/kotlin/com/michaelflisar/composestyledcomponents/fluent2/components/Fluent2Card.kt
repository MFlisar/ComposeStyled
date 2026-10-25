package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledCard
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationCard
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Fluent2Card internal constructor(
    val variantMapping: VariantMapping,
    override val defaults: StyledCard.Defaults,
) : StyledCard.Implementation {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
            defaults: StyledCard.Defaults = defaults(),
        ) = remember(variantMapping, defaults) { Fluent2Card(variantMapping, defaults) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledCard.Variant) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant ->
            when (variant) {
                StyledCard.Variant.Standard -> Variant.Standard
                StyledCard.Variant.Alternative -> Variant.Alternative
            }
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = StyledTheme.shapes.medium,
            contentPadding: PaddingValues = PaddingValues(12.dp),
        ) = StyledCard.Defaults(shape, contentPadding)
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
            shadowElevation = if (mappedVariant == Variant.Standard) 2.dp else 0.dp,
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
