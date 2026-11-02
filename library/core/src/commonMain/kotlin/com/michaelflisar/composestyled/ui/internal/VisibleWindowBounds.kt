package com.michaelflisar.composestyled.ui.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntRect

@Composable
internal expect fun rememberVisibleWindowBoundsOnScreen(): IntRect?
