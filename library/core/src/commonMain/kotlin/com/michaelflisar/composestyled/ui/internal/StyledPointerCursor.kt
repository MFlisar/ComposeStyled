package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.input.pointer.PointerIcon
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
enum class StyledPointerCursor {
    ResizeHorizontal,
    ResizeVertical,
    ResizeNorthWestSouthEast,
    ResizeNorthEastSouthWest,
}

@InternalComposeStyledApi
expect fun styledPointerCursor(cursor: StyledPointerCursor): PointerIcon
