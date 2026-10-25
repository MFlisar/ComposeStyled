package com.michaelflisar.composestyledcomponents.registry

import androidx.compose.runtime.Immutable

@Immutable
class StyledKey<T : Any>(
    val name: String,
) {
    override fun toString(): String = "StyledKey($name)"
}
