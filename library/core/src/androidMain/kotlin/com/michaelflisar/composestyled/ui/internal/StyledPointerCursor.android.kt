package com.michaelflisar.composestyled.ui.internal

import android.view.PointerIcon as AndroidPointerIcon
import androidx.compose.ui.input.pointer.PointerIcon
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
@InternalComposeStyledApi
actual fun styledPointerCursor(cursor: StyledPointerCursor): PointerIcon = PointerIcon(
    when (cursor) {
        StyledPointerCursor.ResizeHorizontal -> AndroidPointerIcon.TYPE_HORIZONTAL_DOUBLE_ARROW
        StyledPointerCursor.ResizeVertical -> AndroidPointerIcon.TYPE_VERTICAL_DOUBLE_ARROW
        StyledPointerCursor.ResizeNorthWestSouthEast ->
            AndroidPointerIcon.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW
        StyledPointerCursor.ResizeNorthEastSouthWest ->
            AndroidPointerIcon.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW
    },
)
