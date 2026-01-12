package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledButton {

    // properties
    private val Property = ThemeProperty<StatefulBaseColorDef>("button")

    // tokens
    internal val Tokens = TokenMap.create(Property, Variant.entries.toSet())

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Primary("button.primary"),
        Secondary("button.secondary"),
        Outlined("button.outlined"),
        Text("button.text"),
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        primary: StatefulBaseColorDef,
        secondary: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
        text: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                Variant.Primary to primary,
                Variant.Secondary to secondary,
                Variant.Outlined to outlined,
                Variant.Text to text,
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
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledButtonDefaults {

    val DefaultVariant = StyledButton.Variant.Primary

    val MinimumHeight: Dp
        @Composable @ReadOnlyComposable get() = StyledTheme.sizes.minimumInteractiveSize

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
    variant: StyledButton.Variant = StyledButtonDefaults.DefaultVariant,
    customization: StyledButton.Customization? = null,
    enabled: Boolean = true,
    shape: Shape = StyledTheme.shapes.button,
    contentPadding: PaddingValues = StyledButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    when (val styledComponents = LocalStyledComponents.current) {
        is StyledTokenComponents -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = false,
            )
            val def = StyledButton.Tokens.resolveVariantData(variant).customise(
                background = customization?.background,
                foreground = customization?.content,
                border = customization?.border,
            )
            val resolved = def.resolve(state)

            styledComponents.button.Render(
                onClick = onClick,
                modifier = modifier.heightIn(min = StyledButtonDefaults.MinimumHeight),
                enabled = enabled,
                shape = shape,
                backgroundColor = resolved.background,
                contentColor = resolved.foreground,
                borderColor = resolved.border,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                content = content
            )
        }

        is StyledWrapperComponents -> {
            styledComponents.button.Render(
                variant = variant,
                customization = customization,
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
