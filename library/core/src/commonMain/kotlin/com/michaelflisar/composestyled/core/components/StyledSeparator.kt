package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

object StyledSeparator {

    // properties
    private val Property = ThemeProperty<Color>("separator")

    // tokens
    internal val Tokens = TokenMap.create(Property, Variant.entries.toSet())

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Default("separator.default"),
        Strong("separator.strong"),
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        default: Color,
        strong: Color,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                Variant.Default to default,
                Variant.Strong to strong,
            )
        )
    }
}

// ----------------------
// Renderer
// ----------------------

interface StyledSeparatorTokenRenderer : StyledTokenRenderer {

    @Composable
    fun RenderHorizontal(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    )

    @Composable
    fun RenderVertical(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    )
}

interface StyledSeparatorWrapperRenderer {

    @Composable
    fun RenderHorizontal(
        variant: StyledSeparator.Variant,
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    )

    @Composable
    fun RenderVertical(
        variant: StyledSeparator.Variant,
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledSeparatorDefaults {
    val Thickness: Dp = 1.dp
    val DefaultVariant: StyledSeparator.Variant = StyledSeparator.Variant.Default
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledHorizontalSeparator(
    modifier: Modifier = Modifier,
    variant: StyledSeparator.Variant = StyledSeparatorDefaults.DefaultVariant,
    color: Color = Color.Unspecified,
    thickness: Dp = StyledSeparatorDefaults.Thickness,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenComponents -> {
            val themed = StyledSeparator.Tokens.resolveVariantData(variant)
            val finalColor = if (color != Color.Unspecified) color else themed
            components.separator.RenderHorizontal(
                modifier = modifier,
                color = finalColor,
                thickness = thickness,
            )
        }

        is StyledWrapperComponents -> {
            components.separator.RenderHorizontal(
                variant = variant,
                modifier = modifier,
                color = color,
                thickness = thickness,
            )
        }
    }
}

@Composable
fun StyledVerticalSeparator(
    modifier: Modifier = Modifier,
    variant: StyledSeparator.Variant = StyledSeparatorDefaults.DefaultVariant,
    color: Color = Color.Unspecified,
    thickness: Dp = StyledSeparatorDefaults.Thickness,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenComponents -> {
            val themed = StyledSeparator.Tokens.resolveVariantData(variant)
            val finalColor = if (color != Color.Unspecified) color else themed
            components.separator.RenderVertical(
                modifier = modifier,
                color = finalColor,
                thickness = thickness,
            )
        }

        is StyledWrapperComponents -> {
            components.separator.RenderVertical(
                variant = variant,
                modifier = modifier,
                color = color,
                thickness = thickness,
            )
        }
    }
}
