package com.michaelflisar.composestyled.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey

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
            containerColor: Color,
            contentColor: Color,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** A fixed-width primary tab row with an indicator for the selected tab. */
@Composable
fun StyledPrimaryTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Primary,
        scrollable = false,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

/** A horizontally scrollable primary tab row for more tabs than fit in the viewport. */
@Composable
fun StyledPrimaryScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Primary,
        scrollable = true,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

/** A secondary tab row for switching between related content within a primary destination. */
@Composable
fun StyledSecondaryTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Secondary,
        scrollable = false,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

/** A horizontally scrollable secondary tab row for a larger set of related tabs. */
@Composable
fun StyledSecondaryScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    StyledTabRowImpl(
        variant = StyledTabRow.Variant.Secondary,
        scrollable = true,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
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
    containerColor: Color,
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledTabRow.Key].Render(
        variant = variant,
        scrollable = scrollable,
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}
