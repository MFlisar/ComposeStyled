package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledRangeSlider
import com.michaelflisar.composestyled.ui.components.StyledSlider
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@InternalComposeStyledApi
class FoundationSlider : StyledSlider.Implementation {

    @Composable
    override fun Render(
        value: Float,
        onValueChange: (Float) -> Unit,
        onValueChangeFinished: (() -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        valueRange: ClosedFloatingPointRange<Float>,
        steps: Int,
        interactionSource: MutableInteractionSource?,
        activeColor: Color,
        inactiveColor: Color,
    ) {
        val currentValue by rememberUpdatedState(value)
        val currentOnValueChange by rememberUpdatedState(onValueChange)
        val currentOnValueChangeFinished by rememberUpdatedState(onValueChangeFinished)
        var width by remember { mutableFloatStateOf(0f) }
        val layoutDirection = LocalLayoutDirection.current
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val density = LocalDensity.current
        val thumbRadiusPx = with(density) { 10.dp.toPx() }
        val source = interactionSource ?: remember { MutableInteractionSource() }
        var dragPosition by remember { mutableFloatStateOf(value) }
        var dragOutput by remember { mutableFloatStateOf(value) }
        val dragState = rememberDraggableState { delta ->
            val usableWidth = width - thumbRadiusPx * 2f
            if (usableWidth > 0f) {
                dragPosition += delta / usableWidth *
                    (valueRange.endInclusive - valueRange.start)
                val value = snap(
                    dragPosition,
                    valueRange,
                    steps,
                )
                if (value != dragOutput) {
                    dragOutput = value
                    currentOnValueChange(value)
                }
            }
        }
        val dragged by source.collectIsDraggedAsState()
        val thumbColor = if (enabled) activeColor else activeColor.copy(alpha = 0.38f)
        val selectedTrackColor = if (enabled) activeColor else activeColor.copy(alpha = 0.38f)
        val trackModifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .onSizeChanged { width = it.width.toFloat() }
            .draggable(
                state = dragState,
                orientation = Orientation.Horizontal,
                enabled = enabled,
                interactionSource = source,
                reverseDirection = layoutDirection == LayoutDirection.Rtl,
                onDragStarted = {
                    dragPosition = currentValue
                    dragOutput = currentValue
                },
                onDragStopped = { currentOnValueChangeFinished?.invoke() },
            )
            .pointerInput(enabled, valueRange, steps) {
                detectTapGestures { position ->
                    if (enabled) {
                        val usableWidth = size.width.toFloat() - thumbRadiusPx * 2f
                        if (usableWidth > 0f) {
                            val fraction = ((position.x - thumbRadiusPx) / usableWidth)
                                .coerceIn(0f, 1f)
                            currentOnValueChange(
                                snap(
                                    valueRange.start +
                                        (if (isRtl) 1f - fraction else fraction) *
                                        (valueRange.endInclusive - valueRange.start),
                                    valueRange,
                                    steps,
                                )
                            )
                            currentOnValueChangeFinished?.invoke()
                        }
                    }
                }
            }
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(currentValue, valueRange, steps)
                if (!enabled) disabled()
                setProgress { requested ->
                    if (enabled) {
                        currentOnValueChange(snap(requested, valueRange, steps))
                        currentOnValueChangeFinished?.invoke()
                        true
                    } else {
                        false
                    }
                }
            }

        Canvas(modifier.then(trackModifier)) {
            val radius = 10.dp.toPx()
            val centerY = size.height / 2f
            val start = Offset(radius, centerY)
            val end = Offset(size.width - radius, centerY)
            val fraction = fraction(value, valueRange).let { if (isRtl) 1f - it else it }
            val thumbX = start.x + (end.x - start.x) * fraction
            drawLine(inactiveColor, start, end, strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
            drawLine(
                selectedTrackColor,
                Offset(if (isRtl) thumbX else start.x, centerY),
                Offset(if (isRtl) end.x else thumbX, centerY),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawStepMarks(
                steps = steps,
                startX = start.x,
                endX = end.x,
                centerY = centerY,
                radius = 1.5.dp.toPx(),
                activeColor = selectedTrackColor,
                inactiveColor = inactiveColor,
                selectedStart = if (isRtl) fraction else 0f,
                selectedEnd = if (isRtl) 1f else fraction,
            )
            drawCircle(
                color = thumbColor,
                radius = if (dragged) 11.dp.toPx() else radius,
                center = Offset(thumbX, centerY),
            )
        }
    }
}

@InternalComposeStyledApi
class FoundationRangeSlider : StyledRangeSlider.Implementation {

    @Composable
    override fun Render(
        value: ClosedFloatingPointRange<Float>,
        onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
        onValueChangeFinished: (() -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        valueRange: ClosedFloatingPointRange<Float>,
        steps: Int,
        interactionSource: MutableInteractionSource?,
        activeColor: Color,
        inactiveColor: Color,
    ) {
        val currentValue by rememberUpdatedState(value)
        val currentOnValueChange by rememberUpdatedState(onValueChange)
        val currentOnValueChangeFinished by rememberUpdatedState(onValueChangeFinished)
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val dragged by source.collectIsDraggedAsState()
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        val scope = rememberCoroutineScope()
        var dragInteraction by remember { mutableStateOf<DragInteraction.Start?>(null) }
        val thumbColor = if (enabled) activeColor else activeColor.copy(alpha = 0.38f)
        val selectedTrackColor = if (enabled) activeColor else activeColor.copy(alpha = 0.38f)

        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .widthIn(min = 48.dp)
                .pointerInput(enabled, valueRange, steps) {
                    var draggingStart = true
                    detectDragGestures(
                        onDragStart = { position ->
                            if (!enabled) return@detectDragGestures
                            val current = currentValue
                            draggingStart =
                                kotlin.math.abs(
                                    position.x - xFor(
                                        current.start,
                                        valueRange,
                                        size.width.toFloat(),
                                        10.dp.toPx(),
                                        isRtl,
                                    )
                                ) <= kotlin.math.abs(
                                    position.x - xFor(
                                        current.endInclusive,
                                        valueRange,
                                        size.width.toFloat(),
                                        10.dp.toPx(),
                                        isRtl,
                                    )
                                )
                            val start = DragInteraction.Start()
                            dragInteraction = start
                            scope.launch { source.emit(start) }
                            updateRangeValue(
                                position.x,
                                size.width.toFloat(),
                                10.dp.toPx(),
                                isRtl,
                                draggingStart,
                                currentValue,
                                valueRange,
                                steps,
                                currentOnValueChange,
                            )
                        },
                        onDrag = { change, _ ->
                            if (!enabled) return@detectDragGestures
                            change.consume()
                            updateRangeValue(
                                change.position.x,
                                size.width.toFloat(),
                                10.dp.toPx(),
                                isRtl,
                                draggingStart,
                                currentValue,
                                valueRange,
                                steps,
                                currentOnValueChange,
                            )
                        },
                        onDragEnd = {
                            dragInteraction?.let { start ->
                                scope.launch { source.emit(DragInteraction.Stop(start)) }
                                currentOnValueChangeFinished?.invoke()
                            }
                            dragInteraction = null
                        },
                        onDragCancel = {
                            dragInteraction?.let { start ->
                                scope.launch { source.emit(DragInteraction.Cancel(start)) }
                                currentOnValueChangeFinished?.invoke()
                            }
                            dragInteraction = null
                        },
                    )
                }
                .pointerInput(enabled, valueRange, steps) {
                    detectTapGestures { position ->
                        if (enabled) {
                            val current = currentValue
                            val start = xFor(current.start, valueRange, size.width.toFloat(), 10.dp.toPx(), isRtl)
                            val end = xFor(current.endInclusive, valueRange, size.width.toFloat(), 10.dp.toPx(), isRtl)
                            val moveStart = kotlin.math.abs(position.x - start) <=
                                kotlin.math.abs(position.x - end)
                            updateRangeValue(
                                position.x,
                                size.width.toFloat(),
                                10.dp.toPx(),
                                isRtl,
                                moveStart,
                                current,
                                valueRange,
                                steps,
                                currentOnValueChange,
                            )
                            currentOnValueChangeFinished?.invoke()
                        }
                    }
                },
        ) {
            val startFraction = fraction(value.start, valueRange)
                .let { if (isRtl) 1f - it else it }
            val endFraction = fraction(value.endInclusive, valueRange)
                .let { if (isRtl) 1f - it else it }
            Canvas(Modifier.fillMaxSize()) {
                val radius = 10.dp.toPx()
                val centerY = size.height / 2f
                val xStart = xFor(value.start, valueRange, size.width, radius, isRtl)
                val xEnd = xFor(value.endInclusive, valueRange, size.width, radius, isRtl)
                drawLine(
                    inactiveColor,
                    Offset(radius, centerY),
                    Offset(size.width - radius, centerY),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawLine(
                    selectedTrackColor,
                    Offset(xStart, centerY),
                    Offset(xEnd, centerY),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                val valueStartFraction = fraction(value.start, valueRange)
                    .let { if (isRtl) 1f - it else it }
                val valueEndFraction = fraction(value.endInclusive, valueRange)
                    .let { if (isRtl) 1f - it else it }
                drawStepMarks(
                    steps = steps,
                    startX = radius,
                    endX = size.width - radius,
                    centerY = centerY,
                    radius = 1.5.dp.toPx(),
                    activeColor = selectedTrackColor,
                    inactiveColor = inactiveColor,
                    selectedStart = minOf(valueStartFraction, valueEndFraction),
                    selectedEnd = maxOf(valueStartFraction, valueEndFraction),
                )
                drawCircle(
                    thumbColor,
                    radius = if (dragged) 11.dp.toPx() else radius,
                    center = Offset(xStart, centerY),
                )
                drawCircle(
                    thumbColor,
                    radius = if (dragged) 11.dp.toPx() else radius,
                    center = Offset(xEnd, centerY),
                )
            }
            val thumbSize = 48.dp
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            ((maxWidth - 20.dp) * startFraction + 10.dp - thumbSize / 2)
                                .roundToPx(),
                            0,
                        )
                    }
                    .size(thumbSize)
                    .align(Alignment.CenterStart)
                    .semantics {
                        progressBarRangeInfo =
                            ProgressBarRangeInfo(value.start, valueRange, steps)
                        if (!enabled) disabled()
                        setProgress { requested ->
                            if (enabled) {
                                currentOnValueChange(
                                    snap(requested, valueRange, steps)
                                        .coerceAtMost(currentValue.endInclusive)..
                                        currentValue.endInclusive
                                )
                                currentOnValueChangeFinished?.invoke()
                                true
                            } else {
                                false
                            }
                        }
                    },
            )
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            ((maxWidth - 20.dp) * endFraction + 10.dp - thumbSize / 2)
                                .roundToPx(),
                            0,
                        )
                    }
                    .size(thumbSize)
                    .align(Alignment.CenterStart)
                    .semantics {
                        progressBarRangeInfo =
                            ProgressBarRangeInfo(value.endInclusive, valueRange, steps)
                        if (!enabled) disabled()
                        setProgress { requested ->
                            if (enabled) {
                                currentOnValueChange(
                                    currentValue.start..snap(requested, valueRange, steps)
                                        .coerceAtLeast(currentValue.start)
                                )
                                currentOnValueChangeFinished?.invoke()
                                true
                            } else {
                                false
                            }
                        }
                    },
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStepMarks(
    steps: Int,
    startX: Float,
    endX: Float,
    centerY: Float,
    radius: Float,
    activeColor: Color,
    inactiveColor: Color,
    selectedStart: Float,
    selectedEnd: Float,
) {
    if (steps <= 0) return
    for (step in 1..steps) {
        val fraction = step.toFloat() / (steps + 1)
        drawCircle(
            color = if (fraction in selectedStart..selectedEnd) activeColor else inactiveColor,
            radius = radius,
            center = Offset(startX + (endX - startX) * fraction, centerY),
        )
    }
}

private fun fraction(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
): Float = if (range.endInclusive == range.start) {
    0f
} else {
    ((value - range.start) / (range.endInclusive - range.start)).coerceIn(0f, 1f)
}

private fun xFor(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    width: Float,
    radius: Float,
    isRtl: Boolean,
): Float {
    val valueFraction = fraction(value, range).let { if (isRtl) 1f - it else it }
    return radius + (width - 2f * radius) * valueFraction
}

private fun updateRangeValue(
    position: Float,
    width: Float,
    radius: Float,
    isRtl: Boolean,
    draggingStart: Boolean,
    current: ClosedFloatingPointRange<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    val usableWidth = width - 2f * radius
    if (usableWidth <= 0f) return
    val positionFraction = ((position - radius) / usableWidth).coerceIn(0f, 1f)
    val value = snap(
        valueRange.start +
            (if (isRtl) 1f - positionFraction else positionFraction) *
            (valueRange.endInclusive - valueRange.start),
        valueRange,
        steps,
    )
    if (draggingStart) {
        onValueChange(value.coerceAtMost(current.endInclusive)..current.endInclusive)
    } else {
        onValueChange(current.start..value.coerceAtLeast(current.start))
    }
}

private fun snap(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
): Float {
    val clamped = value.coerceIn(range)
    if (steps <= 0 || range.endInclusive <= range.start) return clamped
    val stepSize = (range.endInclusive - range.start) / (steps + 1)
    return (range.start + ((clamped - range.start) / stepSize).roundToInt() * stepSize)
        .coerceIn(range)
}
