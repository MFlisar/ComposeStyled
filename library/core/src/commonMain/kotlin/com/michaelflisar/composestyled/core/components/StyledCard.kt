package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledCard {

    // properties
    private val Property = ThemeProperty<StatefulBaseColorDef>("card")

    // tokens
    internal val Tokens = TokenMap.create(Property, Variant.entries.toSet())

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Filled("card.filled.default"),
        Outlined("card.outlined.default"),
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        filled: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                Variant.Filled to filled,
                Variant.Outlined to outlined,
            )
        )
    }

    fun customize(
        background: Color? = null,
        content: Color? = null,
        border: Color? = null,
    ) = Customization(background, content, border)

    @Immutable
    class Customization internal constructor(
        val background: Color?,
        val content: Color?,
        val border: Color?,
    )
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
        variant: StyledCard.Variant,
        customization: StyledCard.Customization?,
        modifier: Modifier,
        emphasis: Emphasis,
        shape: Shape,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledCardDefaults {

    val DefaultVariant = StyledCard.Variant.Filled

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
    variant: StyledCard.Variant = StyledCardDefaults.DefaultVariant,
    customization: StyledCard.Customization? = null,
    shape: Shape = StyledTheme.shapes.card,
    emphasis: Emphasis = StyledCardDefaults.DefaultEmphasis,
    enabled: Boolean = true,
    contentPadding: PaddingValues = StyledCardDefaults.contentPadding(),
    content: @Composable ColumnScope.() -> Unit,
) {
    when (val styledComponents = LocalStyledComponents.current) {
        is StyledTokenComponents -> {
            val state = rememberStyledResolveState(
                interactionSource = null,
                enabled = enabled,
                isError = false,
            )
            val def = StyledCard.Tokens.resolveVariantData(variant).customise(
                background = customization?.background,
                foreground = customization?.content,
                border = customization?.border,
            )
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
                variant = variant,
                customization = customization,
                modifier = modifier,
                emphasis = emphasis,
                shape = shape,
                contentPadding = contentPadding,
                content = content,
            )
        }
    }
}
