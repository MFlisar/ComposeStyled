package com.michaelflisar.composestyled.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey

object StyledProgressIndicator {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledProgressIndicator")

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Type {
        Circular,
        Linear,
    }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            type: Type,
            progress: Float?,
            modifier: Modifier,
            color: Color,
            trackColor: Color,
            strokeWidth: Dp,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** Shows linear or circular progress; a null [progress] displays indeterminate progress. */
@Composable
fun StyledProgressIndicator(
    type: StyledProgressIndicator.Type,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = StyledTheme.colorScheme.primary,
    trackColor: Color = color.copy(alpha = 0.24f),
    strokeWidth: Dp = 4.dp,
) {
    LocalStyledRegistry.current[StyledProgressIndicator.Key].Render(
        type = type,
        progress = progress?.coerceIn(0f, 1f),
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        strokeWidth = strokeWidth,
    )
}

@Composable
fun StyledCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = StyledTheme.colorScheme.primary,
    trackColor: Color = color.copy(alpha = 0.24f),
    strokeWidth: Dp = 4.dp,
) = StyledProgressIndicator(
    type = StyledProgressIndicator.Type.Circular,
    progress = progress,
    modifier = modifier,
    color = color,
    trackColor = trackColor,
    strokeWidth = strokeWidth,
)

@Composable
fun StyledLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = StyledTheme.colorScheme.primary,
    trackColor: Color = color.copy(alpha = 0.24f),
    strokeWidth: Dp = 4.dp,
) = StyledProgressIndicator(
    type = StyledProgressIndicator.Type.Linear,
    progress = progress,
    modifier = modifier,
    color = color,
    trackColor = trackColor,
    strokeWidth = strokeWidth,
)
