package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledProgressIndicator
import kotlin.math.min

@InternalComposeStyledApi
class FoundationProgressIndicator : StyledProgressIndicator.Implementation {

    @Composable
    override fun Render(
        type: StyledProgressIndicator.Type,
        progress: Float?,
        modifier: Modifier,
        color: Color,
        trackColor: Color,
        strokeWidth: Dp,
    ) {
        val phase = if (progress == null) {
            rememberInfiniteTransition(label = "Progress indicator").animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "Progress indicator phase",
            )
        } else {
            null
        }
        val semanticsModifier = Modifier.semantics {
            progressBarRangeInfo = progress?.let {
                ProgressBarRangeInfo(it, 0f..1f)
            } ?: ProgressBarRangeInfo.Indeterminate
        }
        when (type) {
            StyledProgressIndicator.Type.Circular -> {
                Canvas(
                    modifier = modifier
                        .then(semanticsModifier)
                        .size(40.dp),
                ) {
                    val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                    val inset = stroke.width / 2f
                    val arcSize = androidx.compose.ui.geometry.Size(
                        width = size.width - stroke.width,
                        height = size.height - stroke.width,
                    )
                    drawArc(
                        color = trackColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke,
                    )
                    drawArc(
                        color = color,
                        startAngle = if (progress == null) (phase?.value ?: 0f) * 360f else -90f,
                        sweepAngle = progress?.let { it * 360f } ?: 90f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke,
                    )
                }
            }

            StyledProgressIndicator.Type.Linear -> {
                Canvas(
                    modifier = modifier
                        .then(semanticsModifier)
                        .height(strokeWidth),
                ) {
                    val radius = min(size.height / 2f, size.width / 2f)
                    drawRoundRect(
                        color = trackColor,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius),
                    )
                    val left: Float
                    val right: Float
                    if (progress != null) {
                        left = 0f
                        right = size.width * progress
                    } else {
                        val segmentWidth = size.width * 0.3f
                        left = (size.width + segmentWidth) * (phase?.value ?: 0f) - segmentWidth
                        right = left + segmentWidth
                    }
                    val clippedLeft = left.coerceAtLeast(0f)
                    val clippedRight = right.coerceAtMost(size.width)
                    if (clippedRight > clippedLeft) {
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(clippedLeft, 0f),
                            size = androidx.compose.ui.geometry.Size(
                                clippedRight - clippedLeft,
                                size.height,
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius),
                        )
                    }
                }
            }
        }
    }
}
