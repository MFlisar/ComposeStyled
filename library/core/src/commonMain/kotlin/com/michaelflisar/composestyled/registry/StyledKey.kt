package com.michaelflisar.composestyled.registry

import androidx.compose.runtime.Immutable

/**
 * Typed identity for a component implementation or its defaults in a [StyledRegistry].
 *
 * Registry lookup uses the key instance, not [name]; expose and reuse one key per entry.
 */
@Immutable
class StyledKey<T : Any>(
    val name: String,
) {
    override fun toString(): String = "StyledKey($name)"
}
