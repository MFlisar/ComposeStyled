package com.michaelflisar.composestyled.components.classes

import androidx.compose.runtime.Immutable

@Immutable
internal data class Insets<T>(
    val left: T,
    val top: T,
    val right: T,
    val bottom: T,
)

internal typealias InsetsPx = Insets<Int>