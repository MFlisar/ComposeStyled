package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.internal.StyledPointerCursor
import com.michaelflisar.composestyled.ui.internal.styledPointerCursor

@InternalComposeStyledApi
class FloatingDialogEntry {
    var content: (@Composable () -> Unit)? by mutableStateOf(null)
    var zIndex by mutableIntStateOf(0)
}

@InternalComposeStyledApi
class FloatingDialogHostState {
    val entries = mutableStateListOf<FloatingDialogEntry>()
    var activePointerCursor: StyledPointerCursor? by mutableStateOf(null)
    private var nextZIndex = 0

    fun register(entry: FloatingDialogEntry) {
        if (entry !in entries) entries += entry
        bringToFront(entry)
    }

    fun unregister(entry: FloatingDialogEntry) {
        entries.remove(entry)
    }

    fun bringToFront(entry: FloatingDialogEntry) {
        entry.zIndex = ++nextZIndex
    }
}

@InternalComposeStyledApi
val LocalFloatingDialogHost = staticCompositionLocalOf<FloatingDialogHostState?> { null }

@InternalComposeStyledApi
val LocalFloatingDialogEntry = staticCompositionLocalOf<FloatingDialogEntry?> { null }

@InternalComposeStyledApi
class FullscreenDialogEntry {
    var content: (@Composable () -> Unit)? by mutableStateOf(null)
}

@InternalComposeStyledApi
class FullscreenDialogHostState {
    val entries = mutableStateListOf<FullscreenDialogEntry>()

    fun register(entry: FullscreenDialogEntry) {
        entries.remove(entry)
        entries.add(entry)
    }

    fun unregister(entry: FullscreenDialogEntry) {
        entries.remove(entry)
    }
}

@InternalComposeStyledApi
val LocalFullscreenDialogHost = staticCompositionLocalOf<FullscreenDialogHostState?> { null }

@InternalComposeStyledApi
val LocalFullscreenDialogEntry = staticCompositionLocalOf<FullscreenDialogEntry?> { null }

/**
 * Provides a bounded overlay area for non-modal [StyledDialog.Style.FloatingWindow] windows.
 * The closest host receives floating windows; without an explicit wrapper, `StyledTheme`
 * provides a host around its content.
 */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledFloatingHost(
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable () -> Unit,
) {
    val host = remember { FloatingDialogHostState() }
    val activePointerCursor = host.activePointerCursor
    val pointerIcon = activePointerCursor?.let { styledPointerCursor(it) } ?: PointerIcon.Default
    CompositionLocalProvider(LocalFloatingDialogHost provides host) {
        Box(
            modifier.pointerHoverIcon(
                pointerIcon,
                overrideDescendants = activePointerCursor != null,
            ),
        ) {
            content()
            BoxWithConstraints(Modifier.matchParentSize()) {
                host.entries.sortedBy { it.zIndex }.forEach { entry ->
                    CompositionLocalProvider(LocalFloatingDialogEntry provides entry) {
                        entry.content?.invoke()
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
internal fun StyledFullscreenDialogHost(
    content: @Composable () -> Unit,
) {
    val host = remember { FullscreenDialogHostState() }
    CompositionLocalProvider(LocalFullscreenDialogHost provides host) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            content()
            host.entries.forEach { entry ->
                Box(Modifier.matchParentSize()) {
                    CompositionLocalProvider(LocalFullscreenDialogEntry provides entry) {
                        entry.content?.invoke()
                    }
                }
            }
        }
    }
}
