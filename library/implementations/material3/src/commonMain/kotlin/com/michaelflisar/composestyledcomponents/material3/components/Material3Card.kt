package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.michaelflisar.composestyledcomponents.ui.components.StyledCard
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3Card internal constructor(
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
        ) = remember(variantMapping, defaults) { Material3Card(variantMapping, defaults) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledCard.Variant) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant ->
            when (variant) {
                StyledCard.Variant.Standard -> Variant.Filled
                StyledCard.Variant.Alternative -> Variant.Outlined
            }
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = CardDefaults.shape,
            contentPadding: PaddingValues = PaddingValues(16.dp),
        ) = StyledCard.Defaults(shape, contentPadding)
    }

    enum class Variant { Filled, Outlined }

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
        val mapped = variantMapping(variant)
        val filledBackground = customization?.colors?.filledBackground?.resolve()
            ?.takeIf { it != Color.Unspecified } ?: Color.Unspecified
        val filledForeground = customization?.colors?.filledForeground?.resolve()
            ?.takeIf { it != Color.Unspecified } ?: Color.Unspecified
        val textForeground = customization?.colors?.textForeground?.resolve()
            ?.takeIf { it != Color.Unspecified } ?: Color.Unspecified
        val customBorder = customization?.border?.resolve()
            ?.takeIf { it != Color.Unspecified }
            ?.let { BorderStroke(1.dp, it) }
        val colors = when (mapped) {
            Variant.Filled -> CardDefaults.cardColors(
                containerColor = filledBackground,
                contentColor = filledForeground,
            )
            Variant.Outlined -> CardDefaults.outlinedCardColors(
                contentColor = textForeground,
            )
        }
        val customElevation = customization?.elevation?.takeIf { it.isSpecified }
        val elevation = if (mapped == Variant.Filled && customElevation != null) {
            CardDefaults.cardElevation(
                defaultElevation = customElevation,
                pressedElevation = customElevation,
                focusedElevation = customElevation,
                hoveredElevation = customElevation,
                draggedElevation = customElevation,
                disabledElevation = customElevation,
            )
        } else when (mapped) {
            Variant.Filled -> CardDefaults.cardElevation()
            Variant.Outlined -> CardDefaults.outlinedCardElevation()
        }
        val cardContent: @Composable ColumnScope.() -> Unit = {
            CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = content)
        }
        // A custom border is supported by Card; retain the mapped colors and elevation.

        if (customBorder != null || mapped == Variant.Filled) {
            if (onClick == null) {
                Card(modifier, shape, colors, elevation, customBorder, cardContent)
            } else {
                Card(onClick, modifier, enabled, shape, colors, elevation, customBorder, interactionSource, cardContent)
            }
        } else {
            val outline = CardDefaults.outlinedCardBorder(enabled = onClick == null || enabled)
            if (onClick == null) {
                OutlinedCard(modifier, shape, colors, elevation, outline, cardContent)
            } else {
                OutlinedCard(onClick, modifier, enabled, shape, colors, elevation, outline, interactionSource, cardContent)
            }
        }
    }
}
