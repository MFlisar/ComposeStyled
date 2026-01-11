package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
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

typealias StyledButtonVariant = Variant<StyledButton.VariantId, StatefulBaseColorDef>

object StyledButton {

    // properties
    private val Property = ThemeProperty<StatefulBaseColorDef>("button")

    // variant ids
    enum class VariantId(
        override val id: String,
    ) : IVariantId {
        FilledPrimary("button.filled.primary"),
        Outlined("button.outlined.default"),
        Text("button.text.default"),
    }

    // variants
    object Variants {
        val FilledPrimary: StyledButtonVariant = Variant.Token(VariantId.FilledPrimary)
        val Outlined: StyledButtonVariant = Variant.Token(VariantId.Outlined)
        val Text: StyledButtonVariant = Variant.Token(VariantId.Text)
        fun custom(
            variantId: VariantId,
            colorDef: StatefulBaseColorDef,
        ) = Variant.Custom(variantId, colorDef)
    }

    // tokens
    internal val Tokens = TokenMap.create<VariantId, StatefulBaseColorDef>(Property)

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        primary: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
        text: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                VariantId.FilledPrimary to primary,
                VariantId.Outlined to outlined,
                VariantId.Text to text,
            )
        )
    }
}

// ----------------------
// Renderer
// ----------------------

interface StyledButtonTokenRenderer : StyledTokenRenderer {

    @Composable
    fun Render(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        borderColor: Color?,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    )
}

interface StyledButtonWrapperRenderer {

    @Composable
    fun Render(
        request: Request,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    )

    data class Request(
        val variant: StyledButton.VariantId,
        val customColors: StatefulBaseColorDef?,
    )

}

// ----------------------
// Defaults
// ----------------------

object StyledButtonDefaults {

    val DefaultVariant = StyledButton.Variants.FilledPrimary

    @Composable
    fun contentPadding(
        horizontal: Dp = StyledTheme.paddings.large,
        vertical: Dp = StyledTheme.paddings.small,
    ) = PaddingValues(horizontal = horizontal, vertical = vertical)

}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: StyledButtonVariant = StyledButtonDefaults.DefaultVariant,
    enabled: Boolean = true,
    shape: Shape = StyledTheme.shapes.button,
    contentPadding: PaddingValues = StyledButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    when (val styledComponents = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = false,
            )
            val def = StyledButton.Tokens.resolveVariantData(variant)
            val resolved = def.resolve(state)
            styledComponents.button.Render(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                backgroundColor = resolved.background,
                contentColor = resolved.foreground,
                borderColor = resolved.border ?: Color.Unspecified,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                content = content
            )
        }

        is StyledWrapperComponents -> {
            styledComponents.button.Render(
                request = StyledButtonWrapperRenderer.Request(
                    variant = variant.variantId,
                    customColors = variant.customDataOrNull()
                ),
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                content = content
            )
        }
    }
}
