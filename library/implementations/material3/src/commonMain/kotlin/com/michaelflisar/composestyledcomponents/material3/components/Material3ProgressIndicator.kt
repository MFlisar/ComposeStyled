package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledProgressIndicator

class Material3ProgressIndicator internal constructor() : StyledProgressIndicator.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3ProgressIndicator() }
    }

    @Composable
    override fun Render(
        type: StyledProgressIndicator.Type,
        progress: Float?,
        modifier: Modifier,
        color: Color,
        trackColor: Color,
        strokeWidth: Dp,
    ) {
        when (type) {
            StyledProgressIndicator.Type.Circular -> {
                if (progress == null) {
                    CircularProgressIndicator(
                        modifier = modifier,
                        color = color,
                        trackColor = trackColor,
                        strokeWidth = strokeWidth,
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = modifier,
                        color = color,
                        trackColor = trackColor,
                        strokeWidth = strokeWidth,
                    )
                }
            }

            StyledProgressIndicator.Type.Linear -> {
                if (progress == null) {
                    LinearProgressIndicator(
                        modifier = modifier.height(strokeWidth),
                        color = color,
                        trackColor = trackColor,
                        strokeCap = StrokeCap.Round,
                    )
                } else {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = modifier.height(strokeWidth),
                        color = color,
                        trackColor = trackColor,
                        strokeCap = StrokeCap.Round,
                    )
                }
            }
        }
    }
}
