package com.michaelflisar.composestyled.components.classes

import androidx.compose.runtime.Immutable

@Immutable
data class InsetsPx(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)