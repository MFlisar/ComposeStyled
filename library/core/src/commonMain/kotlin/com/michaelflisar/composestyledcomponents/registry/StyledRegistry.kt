package com.michaelflisar.composestyledcomponents.registry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class StyledRegistry(
    private val parent: StyledRegistry? = null,
    private val entries: MutableMap<StyledKey<*>, Any> = mutableStateMapOf(),
) {
    companion object {

        fun create(
            parent: StyledRegistry? = null,
            builder: Builder.() -> Unit,
        ): StyledRegistry {
            val componentsBuilder = Builder()
            componentsBuilder.builder()
            val entries = componentsBuilder.snapshot()
            return StyledRegistry(
                parent = parent,
                entries = mutableStateMapOf<StyledKey<*>, Any>().apply { putAll(entries) }
            )
        }

        /**
         * Creates and remembers a registry from registrations that may call composable factories.
         */
        @Composable
        fun createComposable(
            parent: StyledRegistry? = null,
            builder: @Composable Builder.() -> Unit,
        ): StyledRegistry {
            val componentsBuilder = Builder()
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


    class Builder internal constructor() {

        private val entries = mutableMapOf<StyledKey<*>, Any>()

        fun register(component: StyledComponent<*>) {
            entries[component.key] = component
        }

        internal fun snapshot(): Map<StyledKey<*>, Any> = entries.toMap()
    }
}

val LocalStyledRegistry = staticCompositionLocalOf { StyledRegistry() }

@Composable
fun UpdateStyledRegistry(
    update: @Composable StyledRegistry.Builder.() -> Unit,
    content: @Composable () -> Unit,
) {
    val parentRegistry = LocalStyledRegistry.current
    val childRegistry = StyledRegistry.createComposable(parentRegistry, update)

    CompositionLocalProvider(
        LocalStyledRegistry provides childRegistry,
    ) {
        content()
    }
}
