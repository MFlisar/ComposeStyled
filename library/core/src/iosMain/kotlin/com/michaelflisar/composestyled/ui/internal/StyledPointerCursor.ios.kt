package com.michaelflisar.composestyled.ui.internal

import androidx.compose.ui.input.pointer.PointerIcon
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
@InternalComposeStyledApi
actual fun styledPointerCursor(cursor: StyledPointerCursor): PointerIcon = PointerIcon.Default
