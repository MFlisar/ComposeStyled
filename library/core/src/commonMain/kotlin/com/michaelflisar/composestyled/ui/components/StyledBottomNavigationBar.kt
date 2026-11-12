package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.internal.LocalStyledBottomNavigationScrollable
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey

object StyledBottomNavigationBar {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledBottomNavigationBar")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            modifier: Modifier,
            scrollable: Boolean,
            scrollState: ScrollState,
            showScrollbar: Boolean,
            containerColor: Color,
            contentColor: Color,
            indicatorTint: Color,
            windowInsets: WindowInsets,
            content: @Composable RowScope.() -> Unit,
        )

        @Composable
        fun RenderItem(
            rowScope: RowScope,
            scrollable: Boolean,
            selected: Boolean,
            onClick: () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            alwaysShowLabel: Boolean,
            interactionSource: MutableInteractionSource?,
            icon: @Composable () -> Unit,
            label: @Composable (() -> Unit)?,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** Equally weighted destinations, or finite-width destinations when horizontally scrollable. */
@Composable
fun StyledBottomNavigationBar(
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    scrollState: ScrollState = rememberScrollState(),
    showScrollbar: Boolean = StyledTheme.behavior.showScrollbars,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    indicatorTint: Color = StyledTheme.colorScheme.primary,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
    ),
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(LocalStyledBottomNavigationScrollable provides scrollable) {
        LocalStyledRegistry.current[StyledBottomNavigationBar.Key].Render(
            modifier = modifier,
            scrollable = scrollable,
            scrollState = scrollState,
            showScrollbar = showScrollbar,
            containerColor = containerColor,
            contentColor = contentColor,
            indicatorTint = indicatorTint,
            windowInsets = windowInsets,
            content = content,
        )
    }
}

/** Renders shared destinations; a null selectedKey leaves all items unselected. */
@Composable
fun <K> StyledBottomNavigationBar(
    items: List<StyledNavigationItem<K>>,
    selectedKey: K?,
    onItemClick: (StyledNavigationItem<K>) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    scrollState: ScrollState = rememberScrollState(),
    showScrollbar: Boolean = StyledTheme.behavior.showScrollbars,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    indicatorTint: Color = StyledTheme.colorScheme.primary,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
    ),
    alwaysShowLabel: Boolean = true,
) {
    StyledBottomNavigationBar(
        modifier = modifier,
        scrollable = scrollable,
        scrollState = scrollState,
        showScrollbar = showScrollbar,
        containerColor = containerColor,
        contentColor = contentColor,
        indicatorTint = indicatorTint,
        windowInsets = windowInsets,
    ) {
        items.forEach { item ->
            key(item.key) {
                StyledBottomNavigationBarItem(
                    selected = item.key == selectedKey,
                    onClick = { onItemClick(item) },
                    enabled = item.enabled,
                    alwaysShowLabel = alwaysShowLabel,
                    label = item.label,
                    icon = item.icon,
                )
            }
        }
    }
}

/** Selection and navigation are controlled by the caller. Use a label or an accessible icon. */
@Composable
fun RowScope.StyledBottomNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    icon: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledBottomNavigationBar.Key].RenderItem(
        rowScope = this,
        scrollable = LocalStyledBottomNavigationScrollable.current,
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        alwaysShowLabel = alwaysShowLabel,
        interactionSource = interactionSource,
        icon = icon,
        label = label,
    )
}
