package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledSurface {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSurface")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(val shape: Shape)

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun Render(
            modifier: Modifier,
            shape: Shape,
            color: Color,
            contentColor: Color,
            border: BorderStroke?,
            shadowElevation: Dp,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledSurfaceDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledSurface.Key].defaults.shape
}

// -------------------------
// public API
// -------------------------

/** Non-interactive surface. Does not add content padding. */
@Composable
fun StyledSurface(
    modifier: Modifier = Modifier,
    shape: Shape = StyledSurfaceDefaults.shape,
    color: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(color),
    border: BorderStroke? = null,
    shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledSurface.Key].Render(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        shadowElevation = shadowElevation,
        content = content,
    )
}
