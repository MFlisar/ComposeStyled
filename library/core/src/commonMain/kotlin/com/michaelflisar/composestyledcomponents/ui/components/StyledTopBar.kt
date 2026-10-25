package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledTopBar {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledTopBar")

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Variant { Standard, CenterAligned }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            variant: Variant,
            modifier: Modifier,
            containerColor: Color,
            contentColor: Color,
            windowInsets: WindowInsets,
            navigationIcon: @Composable () -> Unit,
            actions: @Composable RowScope.() -> Unit,
            title: @Composable () -> Unit,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** The bar handles its own top and horizontal safe-area insets, as expected by StyledScaffold. */
@Composable
fun StyledTopBar(
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Top + WindowInsetsSides.Horizontal
    ),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
) {
    StyledTopBarImpl(
        variant = StyledTopBar.Variant.Standard,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        windowInsets = windowInsets,
        navigationIcon = navigationIcon,
        actions = actions,
        title = title,
    )
}

@Composable
fun StyledCenterAlignedTopBar(
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Top + WindowInsetsSides.Horizontal
    ),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
) = StyledTopBarImpl(
    variant = StyledTopBar.Variant.CenterAligned,
    modifier = modifier,
    containerColor = containerColor,
    contentColor = contentColor,
    windowInsets = windowInsets,
    navigationIcon = navigationIcon,
    actions = actions,
    title = title,
)

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledTopBarImpl(
    variant: StyledTopBar.Variant,
    modifier: Modifier,
    containerColor: Color,
    contentColor: Color,
    windowInsets: WindowInsets,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    title: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledTopBar.Key].Render(
        variant = variant,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        windowInsets = windowInsets,
        navigationIcon = navigationIcon,
        actions = actions,
        title = title,
    )
}
