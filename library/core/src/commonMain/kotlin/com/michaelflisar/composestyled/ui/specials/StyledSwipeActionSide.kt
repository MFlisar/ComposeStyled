package com.michaelflisar.composestyled.ui.specials

import androidx.compose.ui.unit.LayoutDirection

enum class StyledSwipeActionSide {
    Start,
    End,
}

internal fun swipeActionSideForOffset(
    offsetX: Float,
    layoutDirection: LayoutDirection,
): StyledSwipeActionSide? = when {
    offsetX > 0f -> if (layoutDirection == LayoutDirection.Ltr) {
        StyledSwipeActionSide.Start
    } else {
        StyledSwipeActionSide.End
    }

    offsetX < 0f -> if (layoutDirection == LayoutDirection.Ltr) {
        StyledSwipeActionSide.End
    } else {
        StyledSwipeActionSide.Start
    }

    else -> null
}

internal fun swipeOffsetForSide(
    side: StyledSwipeActionSide,
    width: Float,
    layoutDirection: LayoutDirection,
): Float = when (side) {
    StyledSwipeActionSide.Start ->
        if (layoutDirection == LayoutDirection.Ltr) width else -width

    StyledSwipeActionSide.End ->
        if (layoutDirection == LayoutDirection.Ltr) -width else width
}
