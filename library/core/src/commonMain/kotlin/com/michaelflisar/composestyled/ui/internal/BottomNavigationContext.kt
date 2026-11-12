package com.michaelflisar.composestyled.ui.internal

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

internal val LocalStyledBottomNavigationScrollable = compositionLocalOf { false }

@InternalComposeStyledApi
val LocalStyledBottomNavigationIndicatorTint =
    staticCompositionLocalOf { Color.Unspecified }
