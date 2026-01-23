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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Density
import com.michaelflisar.composestyled.core.classes.Size
import com.michaelflisar.composestyled.core.classes.StyledResolveState
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledButton {

    // variants
    enum class Variant {
        Prominent,
        Normal,
        Outlined,
        Text
    }

    fun customize(
        tint: ColorRef? = null,
        onTint: ColorRef? = null,
        border: ColorRef? = null,
    ) = Customization(tint, onTint, border)

    fun customize(
        tint: Color? = null,
        onTint: Color? = null,
        border: Color? = null,
    ) = customize(tint?.asColorRef(), onTint?.asColorRef(), border?.asColorRef())

    @Immutable
    class Customization internal constructor(
        val tint: ColorRef?,
        val onTint: ColorRef?,
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
        fun resolveBorderWidth(
            variant: StyledButton.Variant,
            customization: StyledButton.Customization?,
            state: StyledResolveState,
        ): Dp?

        @Composable
        fun render(
            onClick: () -> Unit,
            modifier: Modifier,
            textStyle: TextStyle,
            enabled: Boolean,
            shape: Shape,
            backgroundColor: Color,
            contentColor: Color,
            borderColor: Color?,
            borderWidth: Dp?,
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

    val variant = StyledButton.Variant.Prominent

    @Composable
    @ReadOnlyComposable
    fun minimumHeight(
        size: Size = Size.Normal,
    ): Dp {
        return size.minimumInteractiveSize()
    }

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.control

    @Composable
    fun contentPadding(
        density: Density,
    ): PaddingValues {
        return PaddingValues(
            horizontal = density.dp(StyledTheme.paddings.large),
            vertical = density.dp(StyledTheme.paddings.medium),
        )
    }
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: StyledButton.Variant = StyledButtonDefaults.variant,
    size: Size = Size.Normal,
    density: Density = Density.Normal,
    customization: StyledButton.Customization? = null,
    enabled: Boolean = true,
    shape: Shape = StyledButtonDefaults.shape,
    contentPadding: PaddingValues = StyledButtonDefaults.contentPadding(density),
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

            val borderWidth = renderer.resolveBorderWidth(variant, customization, state)

            val textStyle = when (size) {
                Size.Small -> StyledTheme.typography.labelSmall
                Size.Normal -> StyledTheme.typography.labelMedium
                Size.Large -> StyledTheme.typography.labelLarge
            }

            renderer.render(
                onClick = onClick,
                modifier = modifier.heightIn(min = StyledButtonDefaults.minimumHeight(size)),
                textStyle = textStyle,
                enabled = enabled,
                shape = shape,
                backgroundColor = colors.background,
                contentColor = colors.foreground,
                borderColor = colors.border,
                borderWidth = borderWidth,
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