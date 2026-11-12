package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationDefaults
import com.michaelflisar.composestyled.ui.internal.LocalStyledSelectionColors

@InternalComposeStyledApi
abstract class FoundationSideNavigation(
    private val showSelectionBackground: Boolean = true,
) : StyledSideNavigation.Implementation {

    protected open val itemHorizontalPadding: Dp
        @Composable get() = StyledTheme.padding.small

    protected open val itemContentPadding: Dp
        @Composable get() = StyledTheme.padding.small

    protected open val iconColumnWidth: Dp
        @Composable get() = (StyledSideNavigationDefaults.compactWidth -
            itemHorizontalPadding * 2 - itemContentPadding * 2).coerceAtLeast(0.dp)

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
        indicatorTint: Color,
        title: @Composable (() -> Unit)?,
        header: @Composable (() -> Unit)?,
        footer: @Composable (() -> Unit)?,
        windowInsets: WindowInsets,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        FoundationSideNavigationLayout(
            modifier, mode, expanded, onExpandedChange, scrollable, scrollState, showScrollbar,
            containerColor, contentColor, indicatorTint, title, header, footer, windowInsets,
            toggleIcon = toggleIcon,
            titleStartPadding = itemHorizontalPadding + itemContentPadding + iconColumnWidth,
            toggleIconCenter = itemHorizontalPadding + itemContentPadding + iconColumnWidth / 2,
            content = content,
        )
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
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val pressed by source.collectIsPressedAsState()
        val hovered by source.collectIsHoveredAsState()
        val keyboardFocus = focused && LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val colors = LocalStyledSelectionColors.current
        val indicator = colors?.indicatorColor?.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.primary
        val base = if (selected && showSelectionBackground) {
            StyledTheme.contentColorFor(indicator)
        } else {
            LocalStyledContentColor.current
        }
        val foreground = if (enabled) base else base.copy(
            alpha = base.alpha * StyledTheme.colorScheme.disableFactors.content,
        )
        val shape = RoundedCornerShape(8.dp)
        val selectionBackground = if (selected && showSelectionBackground) {
            if (enabled) indicator else indicator.copy(
                alpha = indicator.alpha * StyledTheme.colorScheme.disableFactors.default,
            )
        } else Color.Transparent
        val background = when {
            enabled && pressed -> base.copy(alpha = 0.12f).compositeOver(selectionBackground)
            enabled && hovered -> base.copy(alpha = 0.08f).compositeOver(selectionBackground)
            else -> selectionBackground
        }
        val itemModifier = modifier.fillMaxWidth()
            .sizeIn(minHeight = StyledTheme.sizes.minimumInteractiveSize)
            .padding(horizontal = itemHorizontalPadding)
            .clip(shape)
            .selectable(
                selected = selected, enabled = enabled, role = Role.Tab,
                interactionSource = source, indication = null, onClick = onClick,
            )
            .semantics(mergeDescendants = true) {}
            .then(if (enabled && keyboardFocus) Modifier.border(2.dp, foreground, shape) else Modifier)
            .background(background)
        val labelStart = itemHorizontalPadding + itemContentPadding + iconColumnWidth
        val expandedWidth = LocalSideNavigationLayout.current?.expandedWidth ?: StyledSideNavigationDefaults.expandedWidth
        UpdateStyledThemeLocals(
            contentColor = foreground,
            textStyle = StyledTheme.typography.labelMedium,
        ) {
            Box(itemModifier, contentAlignment = Alignment.CenterStart) {
                Row(
                    Modifier.padding(itemContentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        Modifier.width(iconColumnWidth),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        icon()
                        if (compactLabelVisible && label != null) {
                            FoundationSideNavigationCompactLabel(
                                modifier = Modifier.requiredWidth(iconColumnWidth + itemContentPadding * 2),
                                content = labelContent,
                            )
                        }
                    }
                    if (label != null) {
                        FoundationSideNavigationLabel(
                            expanded,
                            labelWidth = (expandedWidth - labelStart - itemHorizontalPadding - itemContentPadding).coerceAtLeast(0.dp),
                        ) {
                            if (!compactLabelVisible) labelContent()
                        }
                    }
                }
                SelectionIndicator(selected, enabled, Modifier.matchParentSize())
            }
        }
    }

    @Composable
    protected open fun SelectionIndicator(selected: Boolean, enabled: Boolean, modifier: Modifier) = Unit
}

/** Keep arbitrary label semantics without drawing or allocating visual label space. */
@InternalComposeStyledApi
@Composable
fun FoundationNavigationHiddenLabel(label: @Composable () -> Unit) {
    Layout(content = label) { measurables, constraints ->
        val children = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }
        layout(0, 0) {
            children.forEach { it.placeWithLayer(0, 0) { alpha = 0f } }
        }
    }
}
