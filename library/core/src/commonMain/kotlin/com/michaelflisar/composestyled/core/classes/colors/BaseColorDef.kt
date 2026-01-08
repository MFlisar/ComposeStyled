package com.michaelflisar.composestyled.core.classes.colors

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Verantwortung: kombiniert mehrere Color zu Rollen,
// keine Zustände, keine Disabled-Logik
@Immutable
data class BaseColorDef(
    val background: Color,
    val foreground: Color,
    val border: Color? = null,
)