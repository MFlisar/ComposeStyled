package com.michaelflisar.composestyledcomponents.utils.number

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
class NumberParser<T : Number>(
    val parse: (value: String) -> T?,
    val normalize: (value: T) -> T,
    val format: (value: T) -> String = { it.toString() },
    val decrement: (value: T) -> T,
    val increment: (value: T) -> T,
)

@Composable
fun <T : Number> rememberNumberParser(
    sample: T,
    stepSize: T,
    commas: Int? = NumberUtil.defaultCommas(sample),
): NumberParser<T> {
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