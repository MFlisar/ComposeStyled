package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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

    @Immutable
    data class SideNavigationLayout(
        val topBehavior: TopBehavior = TopBehavior.BelowTopBar,
        val bottomBehavior: BottomBehavior = BottomBehavior.AboveBottomBar,
    ) {
        enum class TopBehavior {
            BelowTopBar,
            BesideTopBar,
        }

        enum class BottomBehavior {
            AboveBottomBar,
            BesideBottomBar,
        }
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
            sideNavigationLayout: SideNavigationLayout,
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
 * The scaffold reserves space for its bars and side navigation and includes the remaining
 * [contentWindowInsets] in the content padding; apply and consume that padding on the content
 * root to protect nested layouts. [StyledTopBar] and [StyledBottomNavigationBar] handle their
 * own insets by default; custom bar slots must handle their insets themselves.
 * [SideNavigationLayout] configures the top and bottom edges independently. The defaults,
 * [SideNavigationLayout.TopBehavior.BelowTopBar] and
 * [SideNavigationLayout.BottomBehavior.AboveBottomBar], place side navigation between the bars.
 * The corresponding `Beside` behaviors extend it to that scaffold edge and place that bar in the
 * remaining width.
 *
 * A typical scaffold with a top bar and bottom navigation can style both system bars from the
 * colors drawn behind them:
 * ```
 * val scaffoldColor = StyledTheme.colorScheme.surface
 * val topBarColor = StyledTheme.colorScheme.primarySurface
 * val bottomBarColor = StyledTheme.colorScheme.surfaceVariant
 *
 * SystemBarStyleEffectIfNeeded(
 *     statusBarColor = topBarColor,
 *     navigationBarColor = bottomBarColor,
 *     backgroundColor = scaffoldColor,
 * )
 *
 * StyledScaffold(
 *     containerColor = scaffoldColor,
 *     topBar = {
 *         StyledTopBar(
 *             containerColor = topBarColor,
 *             title = { StyledText("My app") },
 *         )
 *     },
 *     bottomBar = {
 *         StyledBottomNavigationBar(containerColor = bottomBarColor) {
 *             // Navigation bar items
 *         }
 *     },
 * ) { paddingValues ->
 *     StyledText(
 *         text = "Content",
 *         modifier = Modifier.padding(paddingValues).consumeWindowInsets(paddingValues),
 *     )
 * }
 * ```
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
    sideNavigationLayout: StyledScaffold.SideNavigationLayout =
        StyledScaffold.SideNavigationLayout(),
    content: @Composable (PaddingValues) -> Unit,
) {
    LocalStyledRegistry.current[StyledScaffold.Key].Render(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        sideNavigation = sideNavigation,
        sideNavigationLayout = sideNavigationLayout,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}
