package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider

object StyledTabRow {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledTabRow")

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Variant {
        Primary,
        Secondary,
    }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            variant: Variant,
            scrollable: Boolean,
            selectedTabIndex: Int,
            modifier: Modifier,
            scrollState: ScrollState?,
            containerColor: Color,
            contentColor: Color,
            edgePadding: Dp,
            minTabWidth: Dp,
            divider: @Composable () -> Unit,
            tabs: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledTabRowDefaults {
    val ScrollableTabRowEdgeStartPadding: Dp = 52.dp
    val ScrollableTabRowMinTabWidth: Dp = 90.dp
}

// -------------------------
// public API
// -------------------------

/** A fixed-width primary tab row with an indicator for the selected tab. */
@Composable
fun StyledPrimaryTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.primarySurface,
    contentColor: Color = StyledTheme.colorScheme.onPrimarySurface,
    divider: @Composable () -> Unit = @Composable { StyledHorizontalDivider() },
    tabs: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Primary,
        scrollable = false,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        scrollState = null,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = StyledTabRowDefaults.ScrollableTabRowEdgeStartPadding,
        minTabWidth = StyledTabRowDefaults.ScrollableTabRowMinTabWidth,
        divider = divider,
        tabs = tabs,
    )
}

/** A horizontally scrollable primary tab row for more tabs than fit in the viewport. */
@Composable
fun StyledPrimaryScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    containerColor: Color = StyledTheme.colorScheme.primarySurface,
    contentColor: Color = StyledTheme.colorScheme.onPrimarySurface,
    edgePadding: Dp = StyledTabRowDefaults.ScrollableTabRowEdgeStartPadding,
    divider: @Composable () -> Unit = @Composable { StyledHorizontalDivider() },
    minTabWidth: Dp = StyledTabRowDefaults.ScrollableTabRowMinTabWidth,
    tabs: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Primary,
        scrollable = true,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        scrollState = scrollState,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = edgePadding,
        minTabWidth = minTabWidth,
        divider = divider,
        tabs = tabs,
    )
}

/** A secondary tab row for switching between related content within a primary destination. */
@Composable
fun StyledSecondaryTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.secondarySurface,
    contentColor: Color = StyledTheme.colorScheme.onSecondarySurface,
    divider: @Composable () -> Unit = @Composable { StyledHorizontalDivider() },
    tabs: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Secondary,
        scrollable = false,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        scrollState = null,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = StyledTabRowDefaults.ScrollableTabRowEdgeStartPadding,
        minTabWidth = StyledTabRowDefaults.ScrollableTabRowMinTabWidth,
        divider = divider,
        tabs = tabs,
    )
}

/** A horizontally scrollable secondary tab row for a larger set of related tabs. */
@Composable
fun StyledSecondaryScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    containerColor: Color = StyledTheme.colorScheme.secondarySurface,
    contentColor: Color = StyledTheme.colorScheme.onSecondarySurface,
    edgePadding: Dp = StyledTabRowDefaults.ScrollableTabRowEdgeStartPadding,
    divider: @Composable () -> Unit = @Composable { StyledHorizontalDivider() },
    minTabWidth: Dp = StyledTabRowDefaults.ScrollableTabRowMinTabWidth,
    tabs: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Secondary,
        scrollable = true,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        scrollState = scrollState,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = edgePadding,
        minTabWidth = minTabWidth,
        divider = divider,
        tabs = tabs,
    )
}

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledTabRowImpl(
    variant: StyledTabRow.Variant,
    scrollable: Boolean,
    selectedTabIndex: Int,
    modifier: Modifier,
    scrollState: ScrollState?,
    containerColor: Color,
    contentColor: Color,
    edgePadding: Dp,
    minTabWidth: Dp,
    divider: @Composable () -> Unit,
    tabs: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledTabRow.Key].Render(
        variant = variant,
        scrollable = scrollable,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        scrollState = scrollState,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = edgePadding,
        minTabWidth = minTabWidth,
        divider = divider,
        tabs = tabs,
    )
}
