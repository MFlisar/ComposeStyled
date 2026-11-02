package com.michaelflisar.composestyled.ui.internal

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
@Immutable
class StyledSelectionColors(
    val selectedContentColor: Color?,
    val unselectedContentColor: Color?,
    val indicatorColor: Color?,
)

@InternalComposeStyledApi
val LocalStyledSelectionColors = staticCompositionLocalOf<StyledSelectionColors?> { null }
