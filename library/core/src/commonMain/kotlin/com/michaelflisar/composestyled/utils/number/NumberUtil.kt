package com.michaelflisar.composestyled.utils.number

import kotlin.math.pow
import kotlin.math.roundToLong

internal object NumberUtil {

    @Suppress("UNCHECKED_CAST")
    fun <T> range(start: T, endInclusive: T): ClosedRange<T>
        where T : Number, T : Comparable<T> {
        requireSupported(start)
        require(endInclusive::class == start::class) { "Range endpoints must have the same number type." }
        return when (start) {
            is Int -> start..endInclusive.toInt()
            is Long -> start..endInclusive.toLong()
            is Float -> start..endInclusive.toFloat()
            is Double -> start..endInclusive.toDouble()
            else -> throw IllegalArgumentException("Unsupported number type: ${start::class}.")
        } as ClosedRange<T>
    }

    private val DECIMAL_SEPARATORS = listOf('.', ',')

    fun requireSupported(value: Number) {
        require(value is Int || value is Long || value is Float || value is Double) {
            "Unsupported number type: ${value::class}. Supported types are Int, Long, Float and Double."
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> zero(value: T): T {
        return when (value::class) {
            Int::class -> 0 as T
            Double::class -> 0.0 as T
            Float::class -> 0f as T
            Long::class -> 0L as T
            else -> throw IllegalArgumentException("Unsupported number type: ${value::class}.")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> one(value: T): T {
        return when (value::class) {
            Int::class -> 1 as T
            Double::class -> 1.0 as T
            Float::class -> 1f as T
            Long::class -> 1L as T
            else -> throw IllegalArgumentException("Unsupported number type: ${value::class}.")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> plus(value1: T, value2: T): T {
        return when (value1::class) {
            Int::class -> (value1 as Int) + (value2 as Int)
            Double::class -> (value1 as Double) + (value2 as Double)
            Float::class -> (value1 as Float) + (value2 as Float)
            Long::class -> (value1 as Long) + (value2 as Long)
            else -> throw IllegalArgumentException("Unsupported number type: ${value1::class}.")
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> minus(value1: T, value2: T): T {
        return when (value1::class) {
            Int::class -> (value1 as Int) - (value2 as Int)
            Double::class -> (value1 as Double) - (value2 as Double)
            Float::class -> (value1 as Float) - (value2 as Float)
            Long::class -> (value1 as Long) - (value2 as Long)
            else -> throw IllegalArgumentException("Unsupported number type: ${value1::class}.")
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> parse(instance: T, value: String, commas: Int? = null): T? {
        val cleaned = value.replace(',', '.')
        return when (instance) {
            is Int -> cleaned.toIntOrNull()
            is Double -> cleaned.toDoubleOrNull()
                ?.let { if (commas != null) it.toRoundedString(commas).toDouble() else it }

            is Float -> cleaned.toFloatOrNull()
            is Long -> cleaned.toLongOrNull()
            else -> throw IllegalArgumentException("Unsupported number type: ${instance::class}.")
        } as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> round(value: T, commas: Int): T {
        return when (value) {
            is Int -> value
            is Double -> value.toRoundedString(commas).toDouble()
            is Float -> value.toRoundedString(commas).toFloat()
            is Long -> value
            else -> throw IllegalArgumentException("Unsupported number type: ${value::class}.")
        } as T
    }

    fun <T : Number> roundToString(value: T, commas: Int): String {
        return when (value) {
            is Int -> value.toString()
            is Double -> value.toRoundedString(commas)
            is Float -> value.toRoundedString(commas)
            is Long -> value.toString()
            else -> throw IllegalArgumentException("Unsupported number type: ${value::class}.")
        }
    }

    fun defaultCommas(value: Number): Int = if (supportsDecimal(value)) 2 else 0

    fun supportsDecimal(value: Number): Boolean {
        requireSupported(value)
        return value is Float || value is Double
    }

    fun clearText(text: String, supportsDecimal: Boolean): String {
        return text
            .filter { c -> c.isDigit() || (supportsDecimal && c in DECIMAL_SEPARATORS) }
            .let { str ->
                // Nur das erste Vorkommen eines Dezimaltrennzeichens behalten

                if (supportsDecimal) {
                    val firstSeparatorIndex = str.indexOfFirst { it in DECIMAL_SEPARATORS }
                    if (firstSeparatorIndex != -1) {
                        val sep = str[firstSeparatorIndex]
                        str.substring(0, firstSeparatorIndex + 1) +
                                str.substring(firstSeparatorIndex + 1)
                                    .filter { it != sep && it !in DECIMAL_SEPARATORS }
                    } else str
                } else str
            }
    }
}

internal fun Double.toRoundedString(commas: Int): String {
    require(commas >= 0) { "commas must be non-negative" }

    val scale = 10.0.pow(commas.toDouble()).toLong()
    val scaledNumber = (kotlin.math.abs(this) * scale).roundToLong()
    val sign = if (this < 0.0 && scaledNumber != 0L) "-" else ""
    val integerPart = scaledNumber / scale
    val fractionalPart = scaledNumber % scale

    return if (fractionalPart == 0L) {
        "$sign$integerPart"
    } else {
        val fractionalPartString = fractionalPart.toString().padStart(commas, '0')
        "$sign$integerPart.${fractionalPartString.trimEnd('0')}"
    }
}

internal fun Float.toRoundedString(commas: Int): String {
    return this.toDouble().toRoundedString(commas)
}