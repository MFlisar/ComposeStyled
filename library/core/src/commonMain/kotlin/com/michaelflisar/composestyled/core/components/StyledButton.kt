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
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledButton {

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Primary("button.primary"),
        Secondary("button.secondary"),
        Outlined("button.outlined"),
        Text("button.text"),
    }

    fun customize(
        background: ColorRef? = null,
        content: ColorRef? = null,
        border: ColorRef? = null,
    ) = Customization(background, content, border)

    fun customize(
        background: Color? = null,
        content: Color? = null,
        border: Color? = null,
    ) = customize(background?.asColorRef(), content?.asColorRef(), border?.asColorRef())

    @Immutable
    class Customization internal constructor(
        val background: ColorRef?,
        val content: ColorRef?,
        val border: ColorRef?,
    )
}

// ----------------------
// Renderer
// ----------------------

interface StyledButtonVariantProvider {

    @Composable
    fun resolveColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
    ): StatefulBaseColorDef
}

interface StyledButtonTokenRenderer : StyledTokenRenderer {

    val variantProvider: StyledButtonVariantProvider

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

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.control

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
    shape: Shape = StyledButtonDefaults.shape,
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
            val colors = styledComponents.button.variantProvider
                .resolveColors(variant, customization)
                .resolve(state)

            styledComponents.button.Render(
                onClick = onClick,
                modifier = modifier.heightIn(min = StyledButtonDefaults.MinimumHeight),
                enabled = enabled,
                shape = shape,
                backgroundColor = colors.background,
                contentColor = colors.foreground,
                borderColor = colors.border,
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
