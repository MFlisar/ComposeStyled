package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.ui.state.StyledExpandableTopBarScrollBehavior

object StyledExpandableTopBar {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledExpandableTopBar")

    // -------------
    // 2) defaults
    // -------------

    val DefaultExpandedHeight = 152.dp

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            behavior: StyledExpandableTopBarScrollBehavior,
            modifier: Modifier,
            expandedTitlePadding: Dp,
            expandedHeight: Dp?,
            containerColor: Color,
            contentColor: Color,
            expandedContainerColor: Color,
            expandedContentColor: Color,
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

/**
 * A scroll-driven top bar whose title smoothly transitions between expanded and compact states.
 *
 * Attach [behavior]'s nested-scroll connection to an ancestor of the scrolling content, typically
 * the [com.michaelflisar.composestyled.ui.layout.StyledScaffold] modifier. The title remains the
 * same composition throughout the transition, using the active theme's expanded-title alignment.
 * [expandedHeight] optionally sets the total expanded height, including the regular top bar and
 * window insets. If omitted, the height is derived from the expanded title and
 * [expandedTitlePadding]. [expandedTitlePadding] controls the title's bottom breathing room.
 *
 * [expandedContainerColor] and [expandedContentColor] animate to [containerColor] and
 * [contentColor] as the bar collapses; they default to the corresponding compact colors.
 */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledExpandableTopBar(
    behavior: StyledExpandableTopBarScrollBehavior,
    modifier: Modifier = Modifier,
    expandedTitlePadding: Dp = StyledTheme.spacing.medium,
    expandedHeight: Dp? = null,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    expandedContainerColor: Color = containerColor,
    expandedContentColor: Color = contentColor,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Top + WindowInsetsSides.Horizontal
    ),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
) {
    val scopedNavigationIcon: @Composable () -> Unit = {
        CompositionLocalProvider(
            LocalStyledContext provides StyledTopBar.Context.NavigationIcon,
            content = navigationIcon,
        )
    }
    val scopedActions: @Composable RowScope.() -> Unit = {
        CompositionLocalProvider(
            LocalStyledContext provides StyledTopBar.Context.Action,
            content = { actions() },
        )
    }
    LocalStyledRegistry.current[StyledExpandableTopBar.Key].Render(
        behavior = behavior,
        modifier = modifier,
        expandedTitlePadding = expandedTitlePadding,
        expandedHeight = expandedHeight,
        containerColor = containerColor,
        contentColor = contentColor,
        expandedContainerColor = expandedContainerColor,
        expandedContentColor = expandedContentColor,
        windowInsets = windowInsets,
        navigationIcon = scopedNavigationIcon,
        actions = scopedActions,
        title = title,
    )
}
