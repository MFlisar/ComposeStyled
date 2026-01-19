package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents

object StyledSeparator {

    // variants
    enum class Variant {
        Default
    }
}

// ----------------------
// Renderer
// ----------------------

sealed interface StyledSeparatorRenderer {

    interface TokenRenderer : StyledSeparatorRenderer {

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

    interface WrapperRenderer : StyledSeparatorRenderer {

        @Composable
        fun RenderHorizontal(
            variant: StyledSeparator.Variant,
            modifier: Modifier,
            color: Color,
            thickness: Dp,
        )

        @Composable
        fun RenderVertical(
            variant: StyledSeparator.Variant,
            modifier: Modifier,
            color: Color,
            thickness: Dp,
        )
    }

}

// ----------------------
// Defaults
// ----------------------

object StyledSeparatorDefaults {
    val thickness: Dp = 1.dp
    val variant: StyledSeparator.Variant = StyledSeparator.Variant.Default
    val color: Color
        @Composable @ReadOnlyComposable
        get() = StyledTheme.colors.outline
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledHorizontalSeparator(
    modifier: Modifier = Modifier,
    variant: StyledSeparator.Variant = StyledSeparatorDefaults.variant,
    color: Color = StyledSeparatorDefaults.color,
    thickness: Dp = StyledSeparatorDefaults.thickness,
) {
    when (val renderer = LocalStyledComponents.current.separator) {
        is StyledSeparatorRenderer.TokenRenderer -> {
            renderer.RenderHorizontal(
                modifier = modifier,
                color = color,
                thickness = thickness,
            )
        }

        is StyledSeparatorRenderer.WrapperRenderer -> {
            renderer.RenderHorizontal(
                variant = variant,
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
    variant: StyledSeparator.Variant = StyledSeparatorDefaults.variant,
    color: Color = StyledSeparatorDefaults.color,
    thickness: Dp = StyledSeparatorDefaults.thickness,
) {
    when (val renderer = LocalStyledComponents.current.separator) {
        is StyledSeparatorRenderer.TokenRenderer -> {
            renderer.RenderVertical(
                modifier = modifier,
                color = color,
                thickness = thickness,
            )
        }

        is StyledSeparatorRenderer.WrapperRenderer -> {
            renderer.RenderVertical(
                variant = variant,
                modifier = modifier,
                color = color,
                thickness = thickness,
            )
        }
    }
}
