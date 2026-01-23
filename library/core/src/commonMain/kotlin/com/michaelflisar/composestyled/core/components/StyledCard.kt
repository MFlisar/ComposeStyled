package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledCard {

    enum class Variant {
        Filled,
        Outlined
    }

    fun customize(
        background: ColorRef? = null,
        content: ColorRef? = null,
        border: ColorRef? = null,
    ) = Customization(background, content, border)

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

sealed interface StyledCardRenderer {

    interface TokenRenderer : StyledCardRenderer {

        @Composable
        fun resolveColors(
            variant: StyledCard.Variant,
            emphasis: Emphasis,
            customization: StyledCard.Customization?,
        ): StatefulBaseColorDef

        @Composable
        fun resolveElevation(
            variant: StyledCard.Variant,
            emphasis: Emphasis,
            customization: StyledCard.Customization?,
        ): Dp

        @Composable
        fun resolveBorderStroke(
            variant: StyledCard.Variant,
            emphasis: Emphasis,
            colors: BaseColor,
            customization: StyledCard.Customization?,
        ): BorderStroke?

        @Composable
        fun render(
            modifier: Modifier,
            onClick: (() -> Unit)?,
            shape: Shape,
            backgroundColor: Color,
            contentColor: Color,
            borderStroke: BorderStroke?,
            elevation: Dp,
            contentPadding: PaddingValues,
            interactionSource: MutableInteractionSource,
            content: @Composable ColumnScope.() -> Unit,
        )
    }

    interface WrapperRenderer : StyledCardRenderer {

        @Composable
        fun render(
            variant: StyledCard.Variant,
            emphasis: Emphasis,
            customization: StyledCard.Customization?,
            modifier: Modifier,
            onClick: (() -> Unit)?,
            shape: Shape,
            contentPadding: PaddingValues,
            interactionSource: MutableInteractionSource,
            content: @Composable ColumnScope.() -> Unit,
        )
    }
}

// ----------------------
// Defaults
// ----------------------

object StyledCardDefaults {

    val variant = StyledCard.Variant.Filled

    val emphasis = Emphasis.Low

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.container

    @Composable
    fun contentPadding(
        horizontal: Dp = StyledTheme.paddings.medium,
        vertical: Dp = StyledTheme.paddings.medium,
    ) = PaddingValues(horizontal = horizontal, vertical = vertical)

}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    variant: StyledCard.Variant = StyledCardDefaults.variant,
    emphasis: Emphasis = StyledCardDefaults.emphasis,
    customization: StyledCard.Customization? = null,
    shape: Shape = StyledCardDefaults.shape,
    contentPadding: PaddingValues = StyledCardDefaults.contentPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    when (val renderer = LocalStyledComponents.current.card) {
        is StyledCardRenderer.TokenRenderer -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = true,
                isError = false,
            )

            val colors = renderer
                .resolveColors(variant, emphasis, customization)
                .resolve(state)

            val resolvedElevation = renderer.resolveElevation(variant, emphasis, customization)
            val borderStroke =
                renderer.resolveBorderStroke(variant, emphasis, colors, customization)

            renderer.render(
                modifier = modifier,
                onClick = onClick,
                shape = shape,
                backgroundColor = colors.background,
                contentColor = colors.foreground,
                borderStroke = borderStroke,
                elevation = resolvedElevation,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                content = content
            )
        }

        is StyledCardRenderer.WrapperRenderer -> {
            renderer.render(
                variant = variant,
                emphasis = emphasis,
                customization = customization,
                modifier = modifier,
                onClick = onClick,
                shape = shape,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                content = content
            )
        }
    }
}