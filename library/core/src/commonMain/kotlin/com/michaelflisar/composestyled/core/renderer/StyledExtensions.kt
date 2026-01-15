package com.michaelflisar.composestyled.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

interface ExtensionKey<T : Any>

interface ExtensionRenderer<T : Any> {
    val key: ExtensionKey<T>

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles()
}

class StyledExtensionsRegistry internal constructor(
    private val map: Map<ExtensionKey<*>, Any>,
) {
    fun get(key: ExtensionKey<*>): Any? = map[key]
}

val LocalStyledExtensions = staticCompositionLocalOf<StyledExtensionsRegistry?> { null }

@InternalComposeStyledApi
@Composable
inline fun <reified T : Any> getStyledExtensionRenderer(key: ExtensionKey<T>): T {
    val registry = LocalStyledExtensions.current
    val extension = registry?.get(key) ?: throw IllegalStateException("No StyledExtensionsRegistry found for key '$key' in the composition local. Did you forget to wrap your app in a StyledTheme?")
    if (extension is T) {
        return extension
    } else {
        throw IllegalStateException("Extension found but of wrong type. Key: ${key::class} => Found: ${extension::class}")
    }
}
