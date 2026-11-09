package com.michaelflisar.composestyled.ui.internal

import androidx.compose.runtime.MutableState
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.utils.number.NumberUtil
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.round

@OptIn(InternalComposeStyledApi::class)
internal class SliderScale<T>(
    val range: ClosedRange<T>,
    stepSize: T?,
) where T : Number, T : Comparable<T> {
    private val sample = range.start
    private val integral = !NumberUtil.supportsDecimal(sample)
    private val start = sample.toDouble()
    private val span = range.endInclusive.toDouble() - start
    private val integerStart = sample.toLong()
    private val integerSpan = range.endInclusive.toLong().toULong() - integerStart.toULong()
    private val integerStep = (stepSize ?: NumberUtil.one(sample)).toLong().toULong()
    private val step = stepSize?.toDouble()
    val intervals: Int?
    val rendererRange: ClosedFloatingPointRange<Float>
    val steps: Int

    init {
        require(range.endInclusive::class == sample::class) { "Slider values must use the same number type." }
        require(range.start < range.endInclusive) { "valueRange must be increasing." }
        require(start.isFinite() && range.endInclusive.toDouble().isFinite()) {
            "valueRange endpoints must be finite."
        }
        require(stepSize == null || (stepSize::class == sample::class &&
            stepSize.toDouble().isFinite() && stepSize.toDouble() > 0.0)) {
            "stepSize must have the value type and be positive and finite."
        }
        intervals = if (integral) {
            require(integerStep > 0uL && integerSpan % integerStep == 0uL) {
                "stepSize must divide valueRange exactly."
            }
            val count = integerSpan / integerStep
            require(count in 1uL..16_777_216uL) {
                "The slider grid must have 1..16777216 intervals (Float renderer precision)."
            }
            count.toInt()
        } else {
            require(span.isFinite() && span > 0.0) { "valueRange span must be positive and finite." }
            if (step != null) {
                val count = span / step
                val rounded = round(count)
                val tolerance = if (sample is Float) 1e-6 else 1e-12
                require(rounded in 1.0..16_777_216.0 &&
                    abs(count - rounded) <= maxOf(1.0, count) * tolerance) {
                    "stepSize must divide valueRange and produce 1..16777216 intervals."
                }
                rounded.toInt()
            } else null
        }
        rendererRange = 0f..(intervals?.toFloat() ?: 1f)
        // A two-endpoint grid is snapped in Core because native steps=0 means continuous.
        steps = intervals?.let { it - 1 } ?: 0
    }

    fun position(value: T): Double {
        require(value::class == sample::class && value.toDouble().isFinite()) {
            "Slider values must be finite and have the valueRange type."
        }
        val clamped = value.coerceIn(range.start, range.endInclusive)
        return if (integral) {
            val offset = clamped.toLong().toULong() - integerStart.toULong()
            val quotient = offset / integerStep
            val remainder = offset % integerStep
            (quotient + if (remainder >= integerStep - remainder) 1uL else 0uL).toDouble()
                .coerceIn(0.0, intervals!!.toDouble())
        } else {
            if (step != null) snap((clamped.toDouble() - start) / step)
            else ((clamped.toDouble() - start) / span).coerceIn(0.0, 1.0)
        }
    }

    fun snap(position: Double): Double {
        require(position.isFinite()) { "Slider renderer emitted a non-finite value." }
        val bounded = position.coerceIn(0.0, intervals?.toDouble() ?: 1.0)
        return if (intervals != null) floor(bounded + 0.5) else bounded
    }

    fun value(position: Double): T {
        val snapped = snap(position)
        if (snapped == 0.0) return range.start
        if (snapped == (intervals?.toDouble() ?: 1.0)) return range.endInclusive
        val text = if (integral) {
            (integerStart.toULong() + snapped.toULong() * integerStep).toLong().toString()
        } else {
            (start + if (step != null) snapped * step else snapped * span).toString()
        }
        return requireNotNull(NumberUtil.parse(sample, text)) {
            "Slider result cannot be represented by ${sample::class}."
        }
    }

    fun window(size: T?, default: Double): Double {
        if (size == null) return default
        require(size::class == sample::class && size.toDouble().isFinite() && size.toDouble() >= 0.0) {
            "Window sizes must have the value type and be non-negative and finite."
        }
        if (integral) {
            val width = size.toLong().toULong()
            require(width % integerStep == 0uL) { "Window sizes must be multiples of stepSize." }
            return (width / integerStep).toDouble()
        }
        if (intervals != null) {
            val width = size.toDouble() / requireNotNull(step)
            val tolerance = if (sample is Float) 1e-6 else 1e-12
            require(abs(width - round(width)) <= maxOf(1.0, width) * tolerance) {
                "Window sizes must be multiples of stepSize."
            }
            return round(width)
        }
        return size.toDouble() / span
    }
}

// MutableState is invariant: retain the caller's concrete standard range type on writeback.
@OptIn(InternalComposeStyledApi::class)
@Suppress("UNCHECKED_CAST")
internal fun <T, R : ClosedRange<T>> rangeLike(template: R, start: T, end: T): R
    where T : Number, T : Comparable<T> {
    val result = NumberUtil.range(start, end)
    require(result::class == template::class) {
        "MutableState range must use IntRange, LongRange or a standard Float/Double range."
    }
    return result as R
}

internal fun <T, R : ClosedRange<T>> updateRangeState(
    state: MutableState<R>,
    start: T,
    end: T,
) where T : Number, T : Comparable<T> {
    state.value = rangeLike(state.value, start, end)
}
