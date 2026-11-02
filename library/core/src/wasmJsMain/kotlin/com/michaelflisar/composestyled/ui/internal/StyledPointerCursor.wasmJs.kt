package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.fromKeyword
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(ExperimentalComposeUiApi::class, InternalComposeStyledApi::class)
@InternalComposeStyledApi
actual fun styledPointerCursor(cursor: StyledPointerCursor): PointerIcon =
    PointerIcon.fromKeyword(
        when (cursor) {
            StyledPointerCursor.ResizeHorizontal -> "ew-resize"
            StyledPointerCursor.ResizeVertical -> "ns-resize"
            StyledPointerCursor.ResizeNorthWestSouthEast -> "nwse-resize"
            StyledPointerCursor.ResizeNorthEastSouthWest -> "nesw-resize"
        },
    )
