package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.Surface
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationSideNavigationCompactLabel
import com.michaelflisar.composestyled.foundation.components.FoundationSideNavigationItemFrame
import com.michaelflisar.composestyled.foundation.components.FoundationSideNavigationLabel
import com.michaelflisar.composestyled.foundation.components.FoundationSideNavigationLayout
import com.michaelflisar.composestyled.foundation.components.LocalSideNavigationLayout
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationDefaults
import com.michaelflisar.composestyled.ui.internal.LocalStyledSelectionColors

@OptIn(InternalComposeStyledApi::class)
class Material3SideNavigation internal constructor() : StyledSideNavigation.Implementation {

    override val defaults: StyledSideNavigation.Defaults
        @Composable get() = StyledSideNavigation.Defaults(compactWidth = 96.dp, expandedWidth = 280.dp)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3SideNavigation() }
    }

    @Composable
    override fun Render(
        modifier: Modifier,
        mode: StyledSideNavigation.Mode,
        expanded: Boolean,
        onExpandedChange: (Boolean) -> Unit,
        toggleIcon: @Composable (StyledSideNavigation.ToggleState) -> Unit,
        scrollable: Boolean,
        scrollState: ScrollState,
        showScrollbar: Boolean,
        containerColor: Color,
        contentColor: Color,
        customization: StyledSideNavigation.Customization?,
        title: @Composable (() -> Unit)?,
        header: @Composable (() -> Unit)?,
        footer: @Composable (() -> Unit)?,
        windowInsets: WindowInsets,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val resolvedContentColor = contentColor.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.contentColorFor(containerColor)
        // WideNavigationRail's own width animation cannot sequence external fixed slots.
        // Keep a shared non-modal frame and use the native expressive rail items.
        Surface(modifier = modifier, color = containerColor, contentColor = resolvedContentColor) {
            FoundationSideNavigationLayout(
                Modifier, mode, expanded, onExpandedChange, scrollable, scrollState, showScrollbar,
                containerColor, resolvedContentColor, customization, title, header, footer, windowInsets,
                toggleIcon = toggleIcon,
                titleStartPadding = 36.dp + StyledTheme.sizes.iconSize + 8.dp,
                toggleIconCenter = 36.dp + StyledTheme.sizes.iconSize / 2,
                content = content,
            )
        }
    }

    @Composable
    override fun RenderItem(
        expanded: Boolean,
        compactLabelVisibility: StyledSideNavigation.CompactLabelVisibility,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        icon: @Composable () -> Unit,
        label: @Composable (() -> Unit)?,
    ) {
        val showCompactLabel = when (compactLabelVisibility) {
            StyledSideNavigation.CompactLabelVisibility.Hidden -> false
            StyledSideNavigation.CompactLabelVisibility.Selected -> selected
            StyledSideNavigation.CompactLabelVisibility.Always -> true
        }
        val progress = LocalSideNavigationLayout.current?.expansionProgress ?: if (expanded) 1f else 0f
        val compactLabelVisible = showCompactLabel && progress == 0f
        val currentLabel by rememberUpdatedState(label)
        val labelContent = remember { movableContentOf { currentLabel?.invoke() } }
        val palette = LocalStyledSelectionColors.current
        val selectedContent = palette?.selectedContentColor?.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.onSecondarySurface
        val unselectedContent = palette?.unselectedContentColor?.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.onSurfaceVariant
        val colors = WideNavigationRailItemDefaults.colors(
            selectedIconColor = selectedContent,
            selectedTextColor = selectedContent,
            unselectedIconColor = unselectedContent,
            unselectedTextColor = unselectedContent,
            selectedIndicatorColor = palette?.indicatorColor?.takeUnless { it == Color.Unspecified }
                ?: StyledTheme.colorScheme.secondarySurface,
        )
        val styledContentColor = (if (selected) selectedContent else unselectedContent).let { color ->
            if (enabled) color else color.copy(
                alpha = color.alpha * StyledTheme.colorScheme.disableFactors.content,
            )
        }
        FoundationSideNavigationItemFrame(modifier) {
            // Keep the native icon position fixed: its independent position animation would
            // move the icon and desynchronize the label from the frame's width animation.
            WideNavigationRailItem(
                selected = selected,
                onClick = onClick,
                railExpanded = expanded,
                iconPosition = NavigationItemIconPosition.Start,
                modifier = Modifier.wrapContentWidth(Alignment.Start)
                    .padding(start = 20.dp)
                    .semantics(mergeDescendants = true) {},
                enabled = enabled,
                interactionSource = interactionSource,
                colors = colors,
                icon = {
                    ProvideStyledLocals(
                        contentColor = styledContentColor,
                        textStyle = StyledTheme.typography.labelLarge,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(
                                Modifier.width(StyledTheme.sizes.iconSize),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                icon()
                                if (compactLabelVisible && label != null) {
                                    FoundationSideNavigationCompactLabel(
                                        modifier = Modifier.requiredWidth(64.dp),
                                        content = labelContent,
                                    )
                                }
                            }
                            if (label != null) {
                                val layout = LocalSideNavigationLayout.current
                                FoundationSideNavigationLabel(
                                    expanded,
                                    labelWidth = ((layout?.expandedWidth ?: StyledSideNavigationDefaults.expandedWidth) -
                                        (layout?.compactWidth ?: StyledSideNavigationDefaults.compactWidth))
                                        .coerceAtLeast(0.dp) + 8.dp,
                                ) {
                                    Box(Modifier.padding(start = 8.dp)) {
                                        if (!compactLabelVisible) labelContent()
                                    }
                                }
                            }
                        }
                    }
                },
                // Native icon-only geometry has symmetric padding. Reveal the side label
                // inside it so no native icon-to-label gap remains when fully compact.
                label = null,
            )
        }
    }
}
