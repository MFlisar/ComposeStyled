package com.michaelflisar.composestyled.core.classes.colors

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Verantwortung: nur finale Farben, keine Logik, direkt nutzbar im UI
@Immutable
data class BaseColor(
    val background: Color,
    val foreground: Color,
    val border: Color? = null
)