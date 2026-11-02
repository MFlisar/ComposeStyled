package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.internal.SliderScale
import com.michaelflisar.composestyled.ui.internal.rangeLike
import com.michaelflisar.composestyled.ui.internal.updateRangeState
import com.michaelflisar.composestyled.utils.number.NumberUtil

object StyledRangeSlider {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledRangeSlider")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
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
        )
    }
}

// -------------------------
// public API
// -------------------------

@Composable
fun <T> StyledRangeSlider(
    value: MutableState<out ClosedRange<T>>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedRange<T> = NumberUtil.range(NumberUtil.zero(value.value.start), NumberUtil.one(value.value.start)),
    stepSize: T? = null,
    interactionSource: MutableInteractionSource? = null,
    activeColor: Color = StyledTheme.colorScheme.primary,
    inactiveColor: Color = activeColor.copy(alpha = 0.24f),
    onValueChangeFinished: (() -> Unit)? = null,
    minimumWindowSize: T? = null,
    maximumWindowSize: T? = null,
) where T : Number, T : Comparable<T> {
    rangeLike(value.value, value.value.start, value.value.endInclusive)
    StyledRangeSlider(
        value = value.value,
        onValueChange = { updateRangeState(value, it.start, it.endInclusive) },
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        stepSize = stepSize,
        interactionSource = interactionSource,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        minimumWindowSize = minimumWindowSize,
        maximumWindowSize = maximumWindowSize,
    )
}

/**
 * [minimumWindowSize] and [maximumWindowSize] constrain the selected range's size, not its values.
 * When either limit is reached, moving one endpoint moves the other to preserve that limit.
 */
@Composable
fun <T> StyledRangeSlider(
    value: ClosedRange<T>,
    onValueChange: (ClosedRange<T>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedRange<T> = NumberUtil.range(NumberUtil.zero(value.start), NumberUtil.one(value.start)),
    stepSize: T? = null,
    interactionSource: MutableInteractionSource? = null,
    activeColor: Color = StyledTheme.colorScheme.primary,
    inactiveColor: Color = activeColor.copy(alpha = 0.24f),
    onValueChangeFinished: (() -> Unit)? = null,
    minimumWindowSize: T? = null,
    maximumWindowSize: T? = null,
) where T : Number, T : Comparable<T> {
    val scale = SliderScale(valueRange, stepSize)
    require(value.start <= value.endInclusive) {
        "value must have a start value no greater than its end value."
    }
    val availableSize = scale.rendererRange.endInclusive.toDouble()
    val minimumSize = scale.window(minimumWindowSize, 0.0)
    val maximumSize = scale.window(maximumWindowSize, availableSize).coerceAtMost(availableSize)
    require(minimumSize <= availableSize) {
        "minimumWindowSize must not be greater than the size of valueRange."
    }
    require(
        minimumSize <= maximumSize
    ) {
        "minimumWindowSize must not be greater than maximumWindowSize."
    }
    val current = scale.position(value.start)..scale.position(value.endInclusive)
    val clampedValue = constrainInitialRange(current, 0.0..availableSize, minimumSize, maximumSize)
    LocalStyledRegistry.current[StyledRangeSlider.Key].Render(
        value = clampedValue.start.toFloat()..clampedValue.endInclusive.toFloat(),
        onValueChange = {
            val constrained = constrainRangeChange(
                current = clampedValue,
                requested = scale.snap(it.start.toDouble())..scale.snap(it.endInclusive.toDouble()),
                valueRange = 0.0..availableSize,
                minimum = minimumSize,
                maximum = maximumSize,
            )
            onValueChange(NumberUtil.range(scale.value(constrained.start), scale.value(constrained.endInclusive)))
        },
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        enabled = enabled,
        valueRange = scale.rendererRange,
        steps = scale.steps,
        interactionSource = interactionSource,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
    )
}

// -------------------------
// private functions
// -------------------------

private fun constrainInitialRange(
    value: ClosedFloatingPointRange<Double>,
    valueRange: ClosedFloatingPointRange<Double>,
    minimum: Double,
    maximum: Double,
): ClosedFloatingPointRange<Double> {
    val start = value.start.coerceIn(
        valueRange.start,
        valueRange.endInclusive - minimum,
    )
    val end = value.endInclusive.coerceIn(
        start + minimum,
        minOf(start + maximum, valueRange.endInclusive),
    )
    return start..end
}

private fun constrainRangeChange(
    current: ClosedFloatingPointRange<Double>,
    requested: ClosedFloatingPointRange<Double>,
    valueRange: ClosedFloatingPointRange<Double>,
    minimum: Double,
    maximum: Double,
): ClosedFloatingPointRange<Double> {
    val startChanged = kotlin.math.abs(requested.start - current.start)
    val endChanged = kotlin.math.abs(requested.endInclusive - current.endInclusive)
    return if (startChanged > endChanged) {
        val start = requested.start.coerceIn(
            valueRange.start,
            valueRange.endInclusive - minimum,
        )
        val end = requested.endInclusive.coerceIn(
            start + minimum,
            minOf(start + maximum, valueRange.endInclusive),
        )
        start..end
    } else {
        val end = requested.endInclusive.coerceIn(
            valueRange.start + minimum,
            valueRange.endInclusive,
        )
        val start = requested.start.coerceIn(
            maxOf(valueRange.start, end - maximum),
            end - minimum,
        )
        start..end
    }
}
