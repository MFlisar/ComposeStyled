package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.input.pointer.PointerIcon
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import java.awt.Cursor

@OptIn(InternalComposeStyledApi::class)
@InternalComposeStyledApi
actual fun styledPointerCursor(cursor: StyledPointerCursor): PointerIcon = PointerIcon(
    Cursor.getPredefinedCursor(
        when (cursor) {
            StyledPointerCursor.ResizeHorizontal -> Cursor.E_RESIZE_CURSOR
            StyledPointerCursor.ResizeVertical -> Cursor.N_RESIZE_CURSOR
            StyledPointerCursor.ResizeNorthWestSouthEast -> Cursor.NW_RESIZE_CURSOR
            StyledPointerCursor.ResizeNorthEastSouthWest -> Cursor.NE_RESIZE_CURSOR
        },
    ),
)
