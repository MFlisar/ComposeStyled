package com.michaelflisar.composestyled.registry

import androidx.compose.runtime.Composable

interface StyledComponent<T : Any> {
    val key: StyledKey<T>
}

/**
 * A component implementation that ships fixed theme defaults. They are registered together with
 * the component by [StyledRegistry.Builder.register] and can be changed via
 * [StyledRegistry.Builder.update].
 */
interface StyledComponentWithDefaults<D : Any> {
    val defaultsKey: StyledKey<D>

    /** Composable so that fixed defaults can still resolve theme tokens when the registry is built. */
    val defaults: D
        @Composable get
}
