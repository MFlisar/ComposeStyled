package com.michaelflisar.composestyled.ui.internal

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

internal val LocalStyledBottomNavigationScrollable = compositionLocalOf { false }

@InternalComposeStyledApi
@Immutable
class StyledBottomNavigationColors(
    val selectedIconColor: Color?,
    val selectedTextColor: Color?,
    val unselectedIconColor: Color?,
    val unselectedTextColor: Color?,
    val indicatorColor: Color?,
)

@InternalComposeStyledApi
val LocalStyledBottomNavigationColors =
    staticCompositionLocalOf<StyledBottomNavigationColors?> { null }
