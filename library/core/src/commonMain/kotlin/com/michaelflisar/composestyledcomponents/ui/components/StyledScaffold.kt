package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledScaffold {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledScaffold")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class FloatingActionButtonPosition {
        Start,
        Center,
        End,
    }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun Render(
            modifier: Modifier,
            topBar: @Composable () -> Unit,
            bottomBar: @Composable () -> Unit,
            navigationRail: @Composable () -> Unit,
            snackbarHost: @Composable () -> Unit,
            floatingActionButton: @Composable () -> Unit,
            floatingActionButtonPosition: FloatingActionButtonPosition,
            containerColor: Color,
            contentColor: Color,
            contentWindowInsets: WindowInsets,
            content: @Composable (PaddingValues) -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledScaffoldDefaults {
    val contentWindowInsets: WindowInsets
        @Composable get() = LocalStyledRegistry.current[StyledScaffold.Key].defaults.contentWindowInsets
}

// -------------------------
// public API
// -------------------------

/**
 * Slots are composed directly by the renderer. Keep state that must survive renderer changes
 * above this Scaffold; remembered slot state is not transferred between implementations.
 */
@Composable
fun StyledScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition = StyledScaffold.FloatingActionButtonPosition.End,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = StyledScaffoldDefaults.contentWindowInsets,
    navigationRail: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    LocalStyledRegistry.current[StyledScaffold.Key].Render(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        navigationRail = navigationRail,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}
