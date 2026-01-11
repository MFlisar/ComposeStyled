package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.IVariantId
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.Variant
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.customDataOrNull
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

typealias StyledCardVariant = Variant<StyledCard.VariantId, StatefulBaseColorDef>

object StyledCard {

    // properties
    private val Property = ThemeProperty<StatefulBaseColorDef>("card")

    // variant ids
    enum class VariantId(
        override val id: String,
    ) : IVariantId {
        Filled("card.filled.default"),
        Outlined("card.outlined.default"),
    }

    // variants
    object Variants {
        val Filled: StyledCardVariant = Variant.Token(VariantId.Filled)
        val Outlined: StyledCardVariant = Variant.Token(VariantId.Outlined)
        fun custom(
            variant: VariantId,
            colorDef: StatefulBaseColorDef,
        ) = Variant.Custom(variant, colorDef)
    }

    // tokens
    internal val Tokens = TokenMap.create<VariantId, StatefulBaseColorDef>(Property)

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        filled: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                VariantId.Filled to filled,
                VariantId.Outlined to outlined,
            )
        )
    }
}

// ----------------------
// Renderer
// ----------------------

interface StyledCardTokenRenderer : StyledTokenRenderer {

    @Composable
    fun Render(
        modifier: Modifier,
        shape: Shape,
        emphasis: Emphasis,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    )

}

interface StyledCardWrapperRenderer {

    @Composable
    fun Render(
        request: Request,
        modifier: Modifier,
        emphasis: Emphasis,
        shape: Shape,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    )

    data class Request(
        val id: StyledCard.VariantId,
        val customColors: StatefulBaseColorDef?,
    )

}

// ----------------------
// Defaults
// ----------------------

object StyledCardDefaults {

    val DefaultVariant = StyledCard.Variants.Filled

    val DefaultEmphasis = Emphasis.Low

    @Composable
    fun contentPadding(): PaddingValues = PaddingValues(0.dp)

    val BorderWidth: Dp = Dp.Hairline
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledCard(
    modifier: Modifier = Modifier,
    variant: StyledCardVariant = StyledCardDefaults.DefaultVariant,
    shape: Shape = StyledTheme.shapes.card,
    emphasis: Emphasis = StyledCardDefaults.DefaultEmphasis,
    enabled: Boolean = true,
    contentPadding: PaddingValues = StyledCardDefaults.contentPadding(),
    content: @Composable ColumnScope.() -> Unit,
) {
    when (val styledComponents = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
            val state = rememberStyledResolveState(
                interactionSource = null,
                enabled = enabled,
                isError = false,
            )
            val def = StyledCard.Tokens.resolveVariantData(variant)
            val resolved = def.resolve(state)
            val border = resolved.border?.let { BorderStroke(StyledCardDefaults.BorderWidth, it) }
            styledComponents.card.Render(
                modifier = modifier,
                shape = shape,
                emphasis = emphasis,
                backgroundColor = resolved.background,
                contentColor = resolved.foreground,
                border = border,
                contentPadding = contentPadding,
                content = content,
            )
        }

        is StyledWrapperComponents -> {
            styledComponents.card.Render(
                request = StyledCardWrapperRenderer.Request(
                    id = variant.variantId,
                    customColors = variant.customDataOrNull()
                ),
                modifier = modifier,
                emphasis = emphasis,
                shape = shape,
                contentPadding = contentPadding,
                content = content,
            )
        }
    }
}