package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

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

@Composable
fun StyledProgressIndicator(
    type: StyledProgressIndicator.Type,
    progress: Float? = null,
    modifier: Modifier = Modifier,
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
fun CircularProgressIndicator(
    progress: Float? = null,
    modifier: Modifier = Modifier,
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
fun LinearProgressIndicator(
    progress: Float? = null,
    modifier: Modifier = Modifier,
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
