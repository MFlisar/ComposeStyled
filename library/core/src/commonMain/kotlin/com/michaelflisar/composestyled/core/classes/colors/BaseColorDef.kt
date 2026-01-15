package com.michaelflisar.composestyled.core.classes.colors

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Verantwortung: kombiniert mehrere Color zu Rollen,
// keine Zustände, keine Disabled-Logik
@Immutable
data class BaseColorDef(
    val background: ColorRef,
    val foreground: ColorRef,
    val border: ColorRef? = null,
) {
    constructor(
        background: Color,
        foreground: Color,
        border: Color? = null,
    ) : this(
        background = background.asColorRef(),
        foreground = foreground.asColorRef(),
        border = border?.asColorRef(),
    )
    fun customise(
        background: ColorRef? = null,
        foreground: ColorRef? = null,
        border: ColorRef? = null,
    ) = BaseColorDef(
        background = background ?: this.background,
        foreground = foreground ?: this.foreground,
        border = border ?: this.border,
    )
}
