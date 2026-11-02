package com.michaelflisar.composestyled.registry

import androidx.compose.runtime.Composable

/** A theme-specific renderer registered under a stable [key]. */
interface StyledComponent<T : Any> {
    val key: StyledKey<T>
}

/**
 * A component implementation that ships fixed theme defaults. They are registered together with
 * the component by [StyledRegistry.Builder.register] and can be changed via
 * [StyledRegistry.Builder.update].
 */
interface StyledComponentWithDefaults<D : Any> {
    /** Key used to read or update this component's defaults in a [StyledRegistry]. */
    val defaultsKey: StyledKey<D>

    /** Composable so that fixed defaults can still resolve theme tokens when the registry is built. */
    val defaults: D
        @Composable get
}
