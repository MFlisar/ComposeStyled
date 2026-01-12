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
) {
    fun customise(
        background: Color? = null,
        foreground: Color? = null,
        border: Color? = null,
    ) = BaseColorDef(
        background = background ?: this.background,
        foreground = foreground ?: this.foreground,
        border = border ?: this.border,
    )}