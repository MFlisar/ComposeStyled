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
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledButton {

    // variants
    enum class Variant {
        Primary,
        Secondary,
        Outlined,
        Text
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

sealed interface StyledButtonRenderer {

    interface TokenRenderer : StyledButtonRenderer {

        @Composable
        fun resolveColors(
            variant: StyledButton.Variant,
            customization: StyledButton.Customization?,
        ): StatefulBaseColorDef

        @Composable
        fun render(
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

    interface WrapperRenderer : StyledButtonRenderer {

        @Composable
        fun render(
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

}

// ----------------------
// Defaults
// ----------------------

object StyledButtonDefaults {

    val variant = StyledButton.Variant.Primary

    val minimumHeight: Dp
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
    variant: StyledButton.Variant = StyledButtonDefaults.variant,
    customization: StyledButton.Customization? = null,
    enabled: Boolean = true,
    shape: Shape = StyledButtonDefaults.shape,
    contentPadding: PaddingValues = StyledButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    when (val renderer = LocalStyledComponents.current.button) {
        is StyledButtonRenderer.TokenRenderer -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = false,
            )
            val colors = renderer
                .resolveColors(variant, customization)
                .resolve(state)

            renderer.render(
                onClick = onClick,
                modifier = modifier.heightIn(min = StyledButtonDefaults.minimumHeight),
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

        is StyledButtonRenderer.WrapperRenderer -> {
            renderer.render(
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