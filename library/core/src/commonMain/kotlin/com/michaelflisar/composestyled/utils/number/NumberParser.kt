package com.michaelflisar.composestyled.utils.number

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Parsing, normalization, formatting and stepping operations for one numeric type. */
@Stable
class NumberParser<T : Number>(
    val parse: (value: String) -> T?,
    val normalize: (value: T) -> T,
    val format: (value: T) -> String = { it.toString() },
    val decrement: (value: T) -> T,
    val increment: (value: T) -> T,
)

/**
 * Remembers parser operations for [sample]'s numeric type and [stepSize].
 *
 * Supports `Int`, `Long`, `Float` and `Double`; [commas] controls decimal rounding and defaults
 * to two places for decimal types and none for integral types.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Number> rememberNumberParser(
    sample: T,
    stepSize: T,
    commas: Int? = NumberUtil.defaultCommas(sample),
): NumberParser<T> {
    NumberUtil.requireSupported(sample)
    NumberUtil.requireSupported(stepSize)
    require(sample::class == stepSize::class) { "sample and stepSize must have the same number type." }
    return remember(sample, stepSize, commas) {
        NumberParser(
            parse = { NumberUtil.parse(sample, it, commas) },
            normalize = { if (commas != null) NumberUtil.round(it, commas) else it },
            format = {
                if (commas != null)
                    NumberUtil.roundToString(it, commas)
                else
                    it.toString()
            },
            decrement = { NumberUtil.minus(it, stepSize) },
            increment = { NumberUtil.plus(it, stepSize) }
        )
    }
}