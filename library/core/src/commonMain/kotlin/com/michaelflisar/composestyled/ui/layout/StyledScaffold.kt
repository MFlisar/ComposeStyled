package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey

object StyledScaffold {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledScaffold")
    val DefaultsKey = StyledKey<Defaults>("StyledScaffoldDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
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

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun Render(
            modifier: Modifier,
            topBar: @Composable () -> Unit,
            bottomBar: @Composable () -> Unit,
            sideNavigation: @Composable () -> Unit,
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
        @Composable get() = LocalStyledRegistry.current[StyledScaffold.DefaultsKey].contentWindowInsets
}

// -------------------------
// public API
// -------------------------

/**
 * Slots are composed directly by the renderer. Keep state that must survive renderer changes
 * above this Scaffold; remembered slot state is not transferred between implementations.
 *
 * Bars handle their own insets. Content receives padding for bars, navigation and remaining
 * [contentWindowInsets]; apply it and consume it on the content root to protect nested layouts.
 * Side navigation is positioned at the start edge and handles its start safe-area inset itself,
 * while the scaffold consumes the top and bottom padding for the bars.
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
    sideNavigation: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    LocalStyledRegistry.current[StyledScaffold.Key].Render(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        sideNavigation = sideNavigation,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}
