package com.michaelflisar.composestyled.ui.specials

import kotlin.math.abs
import kotlin.math.pow

private const val STICKY_OVERSCROLL_CURVE_STRENGTH = 4f
private const val STICKY_OVERSCROLL_CURVE_EXPONENT = 0.25f

/** Controls how a swipe continues beyond its normal action distance. */
enum class StyledSwipeOverscrollBehavior {
    /** Stops at the trigger threshold or action width. */
    None,

    /** Continues at the pointer's speed up to the box width. */
    Free,

    /** Continues with increasing resistance, approaching the box width. */
    Sticky,
}

internal fun resolveSwipeOverscrollOffset(
    rawOffset: Float,
    minimumOffset: Float,
    maximumOffset: Float,
    maximumDistance: Float,
    behavior: StyledSwipeOverscrollBehavior,
): Float {
    val distanceLimit = maximumDistance.coerceAtLeast(0f)
    val minimum = minimumOffset.coerceAtLeast(-distanceLimit).coerceAtMost(0f)
    val maximum = maximumOffset.coerceAtLeast(0f).coerceAtMost(distanceLimit)

    return when (behavior) {
        StyledSwipeOverscrollBehavior.None ->
            rawOffset.coerceIn(minimum, maximum)

        StyledSwipeOverscrollBehavior.Free -> {
            val lowerBound = if (minimum < 0f) -distanceLimit else 0f
            val upperBound = if (maximum > 0f) distanceLimit else 0f
            rawOffset.coerceIn(lowerBound, upperBound)
        }

        StyledSwipeOverscrollBehavior.Sticky -> when {
            rawOffset > maximum && maximum > 0f ->
                resistOverscroll(rawOffset, maximum, distanceLimit)
            rawOffset < minimum && minimum < 0f ->
                -resistOverscroll(abs(rawOffset), abs(minimum), distanceLimit)
            else -> rawOffset.coerceIn(minimum, maximum)
        }
    }
}

private fun resistOverscroll(
    distance: Float,
    limit: Float,
    maximumDistance: Float,
): Float {
    val availableDistance = (maximumDistance - limit).coerceAtLeast(0f)
    if (availableDistance == 0f) return maximumDistance

    val overscrollDistance = distance - limit
    val normalizedOverscroll = overscrollDistance / availableDistance
    val resistedProgress = 1f -
        (1f + normalizedOverscroll * STICKY_OVERSCROLL_CURVE_STRENGTH)
            .pow(-STICKY_OVERSCROLL_CURVE_EXPONENT)
    val resistedDistance = resistedProgress * availableDistance
    return limit + resistedDistance
}
