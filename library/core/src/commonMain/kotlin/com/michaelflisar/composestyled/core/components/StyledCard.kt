package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledCard {

    // variants - TODO: namen neutraler machen?
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
            customization: StyledCard.Customization?,
        ): StatefulBaseColorDef

        @Composable
        fun render(
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

    interface WrapperRenderer : StyledCardRenderer {

        @Composable
        fun render(
            variant: StyledCard.Variant,
            customization: StyledCard.Customization?,
            modifier: Modifier,
            emphasis: Emphasis,
            shape: Shape,
            contentPadding: PaddingValues,
            content: @Composable ColumnScope.() -> Unit,
        )
    }
}

// ----------------------
// Defaults
// ----------------------

object StyledCardDefaults {

    val variant = StyledCard.Variant.Filled

    val DefaultEmphasis = Emphasis.Low

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.container

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
    variant: StyledCard.Variant = StyledCardDefaults.variant,
    customization: StyledCard.Customization? = null,
    shape: Shape = StyledCardDefaults.shape,
    emphasis: Emphasis = StyledCardDefaults.DefaultEmphasis,
    enabled: Boolean = true,
    contentPadding: PaddingValues = StyledCardDefaults.contentPadding(),
    content: @Composable ColumnScope.() -> Unit,
) {
    when (val renderer = LocalStyledComponents.current.card) {
        is StyledCardRenderer.TokenRenderer -> {
            val state = rememberStyledResolveState(
                interactionSource = null,
                enabled = enabled,
                isError = false,
            )
            val colors = renderer
                .resolveColors(variant, customization)
                .resolve(state)
            val border = colors.border?.let { BorderStroke(StyledCardDefaults.BorderWidth, it) }
            renderer.render(
                modifier = modifier,
                shape = shape,
                emphasis = emphasis,
                backgroundColor = colors.background,
                contentColor = colors.foreground,
                border = border,
                contentPadding = contentPadding,
                content = content,
            )
        }

        is StyledCardRenderer.WrapperRenderer -> {
            renderer.render(
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
