package com.michaelflisar.composestyled.utils.number

import androidx.compose.runtime.saveable.Saver

/**
 * Creates a saver for standard numeric ranges of type [Int], [Long], [Float] or [Double].
 *
 * Restoring the endpoints through [NumberUtil.range] preserves the concrete range type, including
 * when the saver is used from a generic function. Unsupported number types fail during restoration.
 */
fun <T> numberRangeSaver(): Saver<ClosedRange<T>, List<Any>>
    where T : Number, T : Comparable<T> = Saver(
    save = { listOf(it.start, it.endInclusive) },
    restore = { NumberUtil.range(it[0] as T, it[1] as T) },
)
