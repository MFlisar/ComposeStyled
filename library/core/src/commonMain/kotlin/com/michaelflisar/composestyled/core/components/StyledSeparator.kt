package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.classes.IVariantId
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.Variant
import com.michaelflisar.composestyled.core.classes.customDataOrNull
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

typealias StyledSeparatorVariant = Variant<StyledSeparator.VariantId, Color>

object StyledSeparator {

    // properties
    private val Property = ThemeProperty<Color>("separator")

    // variant ids
    enum class VariantId(
        override val id: String,
    ) : IVariantId {
        Default("separator.default"),
        Strong("separator.strong"),
    }

    // variants
    object Variants {
        val Default: StyledSeparatorVariant = Variant.Token(VariantId.Default)
        val Strong: StyledSeparatorVariant = Variant.Token(VariantId.Strong)

        fun custom(
            color: Color,
        ) = Variant.Custom(VariantId.Default, color)
    }

    // tokens
    internal val Tokens = TokenMap.create<VariantId, Color>(Property)

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        default: Color,
        strong: Color,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                VariantId.Default to default,
                VariantId.Strong to strong,
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
        request: Request,
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    )

    @Composable
    fun RenderVertical(
        request: Request,
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    )

    data class Request(
        val variant: StyledSeparator.VariantId,
        val customColor: Color?,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledSeparatorDefaults {
    val Thickness: Dp = 1.dp
    val DefaultVariant: StyledSeparatorVariant = StyledSeparator.Variants.Default
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledHorizontalSeparator(
    modifier: Modifier = Modifier,
    variant: StyledSeparatorVariant = StyledSeparatorDefaults.DefaultVariant,
    color: Color = Color.Unspecified,
    thickness: Dp = StyledSeparatorDefaults.Thickness,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
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
                request = StyledSeparatorWrapperRenderer.Request(
                    variant = variant.variantId,
                    customColor = variant.customDataOrNull(),
                ),
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
    variant: StyledSeparatorVariant = StyledSeparatorDefaults.DefaultVariant,
    color: Color = Color.Unspecified,
    thickness: Dp = StyledSeparatorDefaults.Thickness,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
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
                request = StyledSeparatorWrapperRenderer.Request(
                    variant = variant.variantId,
                    customColor = variant.customDataOrNull(),
                ),
                modifier = modifier,
                color = color,
                thickness = thickness,
            )
        }
    }
}
