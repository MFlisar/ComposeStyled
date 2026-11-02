package com.michaelflisar.composestyled.registry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Hierarchical registry of component implementations and defaults.
 *
 * Entries in this registry take precedence over its parent; missing entries are inherited.
 */
@Stable
class StyledRegistry internal constructor(
    private val parent: StyledRegistry? = null,
    private val entries: MutableMap<StyledKey<*>, Any> = mutableStateMapOf(),
) {
    companion object {

        /**
         * Creates and remembers a registry from registrations that may call composable factories.
         */
        @Composable
        fun createComposable(
            parent: StyledRegistry? = null,
            builder: @Composable Builder.() -> Unit,
        ): StyledRegistry {
            val componentsBuilder = Builder(parent)
            componentsBuilder.builder()
            val entries = componentsBuilder.snapshot()
            return remember(parent, entries) {
                StyledRegistry(
                    parent = parent,
                    entries = mutableStateMapOf<StyledKey<*>, Any>().apply { putAll(entries) }
                )
            }
        }
    }

    operator fun <T : Any> get(key: StyledKey<T>): T =
        getOrNull(key)
            ?: error("No implementation registered for ${key.name}")

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getOrNull(key: StyledKey<T>): T? =
        (entries[key] as? T) ?: parent?.getOrNull(key)


    class Builder internal constructor(
        private val parent: StyledRegistry? = null,
    ) {

        private val entries = mutableMapOf<StyledKey<*>, Any>()

        /** Registers a component and, when provided, its defaults under their respective keys. */
        @Composable
        fun register(component: StyledComponent<*>) {
            entries[component.key] = component
            if (component is StyledComponentWithDefaults<*>) {
                entries[component.defaultsKey] = component.defaults
            }
        }

        /**
         * Replaces the current defaults for [key] (registered earlier in this builder or inherited
         * from the parent registry) with the result of [update].
         */
        @Suppress("UNCHECKED_CAST")
        fun <D : Any> update(key: StyledKey<D>, update: D.() -> D) {
            val current = (entries[key] as? D)
                ?: parent?.get(key)
                ?: error("No defaults registered for ${key.name}")
            entries[key] = current.update()
        }

        internal fun snapshot(): Map<StyledKey<*>, Any> = entries.toMap()
    }
}

val LocalStyledRegistry = staticCompositionLocalOf { StyledRegistry() }
