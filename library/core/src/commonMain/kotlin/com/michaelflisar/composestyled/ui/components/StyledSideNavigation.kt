package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.internal.LocalStyledSideNavigationContext
import com.michaelflisar.composestyled.ui.internal.SideNavigationContext
import com.michaelflisar.composestyled.ui.icons.StyledIcons

object StyledSideNavigation {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSideNavigation")
    val DefaultsKey = StyledKey<Defaults>("StyledSideNavigationDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Immutable
    data class Defaults(
        val compactWidth: Dp,
        val expandedWidth: Dp,
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Mode { Compact, Expandable, Expanded }

    enum class CompactLabelVisibility { Hidden, Selected, Always }

    /**
     * [targetExpanded] is the requested state, including while the animation is running.
     * [expansionProgress] follows the visible width, from compact (0f) to expanded (1f).
     */
    @Immutable
    data class ToggleState(
        val targetExpanded: Boolean,
        val expansionProgress: Float,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation> get() = Key
        override val defaultsKey: StyledKey<Defaults> get() = DefaultsKey

        @Composable
        fun Render(
            modifier: Modifier,
            mode: Mode,
            expanded: Boolean,
            onExpandedChange: (Boolean) -> Unit,
            toggleIcon: @Composable (ToggleState) -> Unit,
            scrollable: Boolean,
            scrollState: ScrollState,
            showScrollbar: Boolean,
            containerColor: Color,
            contentColor: Color,
            indicatorTint: Color,
            title: @Composable (() -> Unit)?,
            header: @Composable (() -> Unit)?,
            footer: @Composable (() -> Unit)?,
            windowInsets: WindowInsets,
            content: @Composable ColumnScope.() -> Unit,
        )

        @Composable
        fun RenderItem(
            expanded: Boolean,
            compactLabelVisibility: CompactLabelVisibility,
            selected: Boolean,
            onClick: () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            interactionSource: MutableInteractionSource?,
            icon: @Composable () -> Unit,
            label: @Composable (() -> Unit)?,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledSideNavigationDefaults {

    @Composable
    fun ToggleIcon(state: StyledSideNavigation.ToggleState) {
        StyledIcon(icon = StyledIcons.Menu, contentDescription = null)
    }

    val compactWidth: Dp
        @Composable get() = LocalStyledRegistry.current[StyledSideNavigation.DefaultsKey].compactWidth

    val expandedWidth: Dp
        @Composable get() = LocalStyledRegistry.current[StyledSideNavigation.DefaultsKey].expandedWidth
}

// -------------------------
// public API
// -------------------------

/**
 * Start-side navigation with its own vertical and start-side safe-area padding.
 * Header/footer are expanded-only and do not scroll with destinations.
 */
@Composable
fun StyledSideNavigation(
    modifier: Modifier = Modifier,
    mode: StyledSideNavigation.Mode = StyledSideNavigation.Mode.Compact,
    expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    compactLabelVisibility: StyledSideNavigation.CompactLabelVisibility = StyledSideNavigation.CompactLabelVisibility.Hidden,
    scrollable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    showScrollbar: Boolean = StyledTheme.behavior.showScrollbars,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    indicatorTint: Color = StyledTheme.colorScheme.primary,
    title: @Composable (() -> Unit)? = null,
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Vertical + WindowInsetsSides.Start
    ),
    toggleIcon: @Composable (StyledSideNavigation.ToggleState) -> Unit = { StyledSideNavigationDefaults.ToggleIcon(it) },
    content: @Composable ColumnScope.() -> Unit,
) = StyledSideNavigation(
    expanded = expanded.value,
    onExpandedChange = { expanded.value = it },
    modifier = modifier,
    mode = mode,
    compactLabelVisibility = compactLabelVisibility,
    scrollable = scrollable,
    scrollState = scrollState,
    showScrollbar = showScrollbar,
    containerColor = containerColor,
    contentColor = contentColor,
    indicatorTint = indicatorTint,
    title = title,
    header = header,
    footer = footer,
    windowInsets = windowInsets,
    toggleIcon = toggleIcon,
    content = content,
)

/** Fixed modes ignore [expanded] without changing the caller's remembered expandable state. */
@Composable
fun StyledSideNavigation(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    mode: StyledSideNavigation.Mode = StyledSideNavigation.Mode.Compact,
    compactLabelVisibility: StyledSideNavigation.CompactLabelVisibility = StyledSideNavigation.CompactLabelVisibility.Hidden,
    scrollable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    showScrollbar: Boolean = StyledTheme.behavior.showScrollbars,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    indicatorTint: Color = StyledTheme.colorScheme.primary,
    title: @Composable (() -> Unit)? = null,
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Vertical + WindowInsetsSides.Start
    ),
    toggleIcon: @Composable (StyledSideNavigation.ToggleState) -> Unit = { StyledSideNavigationDefaults.ToggleIcon(it) },
    content: @Composable ColumnScope.() -> Unit,
) {
    val effectiveExpanded = when (mode) {
        StyledSideNavigation.Mode.Compact -> false
        StyledSideNavigation.Mode.Expanded -> true
        StyledSideNavigation.Mode.Expandable -> expanded
    }
    CompositionLocalProvider(
        LocalStyledSideNavigationContext provides SideNavigationContext(effectiveExpanded, compactLabelVisibility),
    ) {
        LocalStyledRegistry.current[StyledSideNavigation.Key].Render(
            modifier = modifier,
            mode = mode,
            expanded = effectiveExpanded,
            onExpandedChange = { if (mode == StyledSideNavigation.Mode.Expandable) onExpandedChange(it) },
            toggleIcon = toggleIcon,
            scrollable = scrollable,
            scrollState = scrollState,
            showScrollbar = showScrollbar,
            containerColor = containerColor,
            contentColor = contentColor,
            indicatorTint = indicatorTint,
            title = title,
            header = header,
            footer = footer,
            windowInsets = windowInsets,
            content = content,
        )
    }
}

/** Shared destinations with saveable, caller-replaceable expansion state. */
@Composable
fun <K> StyledSideNavigation(
    items: List<StyledNavigationItem<K>>,
    selectedKey: K?,
    onItemClick: (StyledNavigationItem<K>) -> Unit,
    modifier: Modifier = Modifier,
    mode: StyledSideNavigation.Mode = StyledSideNavigation.Mode.Compact,
    expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    compactLabelVisibility: StyledSideNavigation.CompactLabelVisibility = StyledSideNavigation.CompactLabelVisibility.Hidden,
    scrollable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    showScrollbar: Boolean = StyledTheme.behavior.showScrollbars,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    indicatorTint: Color = StyledTheme.colorScheme.primary,
    title: @Composable (() -> Unit)? = null,
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Vertical + WindowInsetsSides.Start
    ),
    toggleIcon: @Composable (StyledSideNavigation.ToggleState) -> Unit = { StyledSideNavigationDefaults.ToggleIcon(it) },
) = StyledSideNavigation(
    items = items,
    selectedKey = selectedKey,
    onItemClick = onItemClick,
    expanded = expanded.value,
    onExpandedChange = { expanded.value = it },
    modifier = modifier,
    mode = mode,
    compactLabelVisibility = compactLabelVisibility,
    scrollable = scrollable,
    scrollState = scrollState,
    showScrollbar = showScrollbar,
    containerColor = containerColor,
    contentColor = contentColor,
    indicatorTint = indicatorTint,
    title = title,
    header = header,
    footer = footer,
    windowInsets = windowInsets,
    toggleIcon = toggleIcon,
)

/** Selection remains caller-owned; a null [selectedKey] selects no destination. */
@Composable
fun <K> StyledSideNavigation(
    items: List<StyledNavigationItem<K>>,
    selectedKey: K?,
    onItemClick: (StyledNavigationItem<K>) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    mode: StyledSideNavigation.Mode = StyledSideNavigation.Mode.Compact,
    compactLabelVisibility: StyledSideNavigation.CompactLabelVisibility = StyledSideNavigation.CompactLabelVisibility.Hidden,
    scrollable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    showScrollbar: Boolean = StyledTheme.behavior.showScrollbars,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    indicatorTint: Color = StyledTheme.colorScheme.primary,
    title: @Composable (() -> Unit)? = null,
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Vertical + WindowInsetsSides.Start
    ),
    toggleIcon: @Composable (StyledSideNavigation.ToggleState) -> Unit = { StyledSideNavigationDefaults.ToggleIcon(it) },
) {
    StyledSideNavigation(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        mode = mode,
        compactLabelVisibility = compactLabelVisibility,
        scrollable = scrollable,
        scrollState = scrollState,
        showScrollbar = showScrollbar,
        containerColor = containerColor,
        contentColor = contentColor,
        indicatorTint = indicatorTint,
        title = title,
        header = header,
        footer = footer,
        windowInsets = windowInsets,
        toggleIcon = toggleIcon,
    ) {
        items.forEach { item ->
            key(item.key) {
                StyledSideNavigationItem(
                    selected = item.key == selectedKey,
                    onClick = { onItemClick(item) },
                    enabled = item.enabled,
                    icon = item.icon,
                    label = item.label,
                )
            }
        }
    }
}

/** Labels should provide an accessible name even when their visual presentation is hidden. */
@Composable
fun StyledSideNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    icon: @Composable () -> Unit,
) {
    val context = LocalStyledSideNavigationContext.current
    LocalStyledRegistry.current[StyledSideNavigation.Key].RenderItem(
        expanded = context.expanded,
        compactLabelVisibility = context.compactLabelVisibility,
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        icon = icon,
        label = label,
    )
}
