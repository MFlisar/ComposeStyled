package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.internal.LocalStyledBottomNavigationColors
import com.michaelflisar.composestyled.ui.internal.StyledBottomNavigationColors
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollbarContainer

@InternalComposeStyledApi
class FoundationBottomNavigationBar(
    private val minimumHeight: Dp,
    private val showIndicator: Boolean = true,
    private val addVerticalContentPadding: Boolean = true,
) : StyledBottomNavigationBar.Implementation {

    @Composable
    override fun Render(
        modifier: Modifier,
        scrollable: Boolean,
        scrollState: ScrollState,
        showScrollbar: Boolean,
        containerColor: Color,
        contentColor: Color,
        customization: StyledBottomNavigationBar.Customization?,
        windowInsets: WindowInsets,
        content: @Composable RowScope.() -> Unit,
    ) {
        val resolvedContentColor = if (contentColor == Color.Unspecified) {
            StyledTheme.contentColorFor(containerColor)
        } else {
            contentColor
        }
        UpdateStyledThemeLocals(
            contentColor = resolvedContentColor,
            textStyle = StyledTheme.typography.labelLarge,
        ) {
            val selectedItemIcon = customization?.selectedItemIconColor?.resolve()
            val selectedItemText = customization?.selectedItemTextColor?.resolve()
            val unselectedItemIcon = customization?.unselectedItemIconColor?.resolve()
            val unselectedItemText = customization?.unselectedItemTextColor?.resolve()
            val selectedItemIndicator = customization?.selectedItemIndicatorColor?.resolve()
            val selectionColors = remember(
                selectedItemIcon,
                selectedItemText,
                unselectedItemIcon,
                unselectedItemText,
                selectedItemIndicator,
            ) {
                StyledBottomNavigationColors(
                    selectedIconColor = selectedItemIcon,
                    selectedTextColor = selectedItemText,
                    unselectedIconColor = unselectedItemIcon,
                    unselectedTextColor = unselectedItemText,
                    indicatorColor = selectedItemIndicator,
                )
            }
            CompositionLocalProvider(LocalStyledBottomNavigationColors provides selectionColors) {
                val items: @Composable () -> Unit = {
                    val verticalContentPadding = if (addVerticalContentPadding) {
                        StyledTheme.padding.small
                    } else {
                        0.dp
                    }
                    Row(
                        modifier = (if (scrollable) Modifier else Modifier.fillMaxWidth())
                            .selectableGroup()
                            .heightIn(min = minimumHeight)
                            .padding(
                                start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
                                end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
                                top = verticalContentPadding,
                                bottom = verticalContentPadding,
                            ),
                        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
                        verticalAlignment = Alignment.CenterVertically,
                        content = content,
                    )
                }
                Box(modifier.fillMaxWidth().background(containerColor).windowInsetsPadding(windowInsets)) {
                    if (scrollable) {
                        StyledHorizontalScrollbarContainer(
                            scrollState = scrollState,
                            showScrollbar = showScrollbar,
                        ) { items() }
                    } else items()
                }
            }
        }
    }

    @Composable
    override fun RenderItem(
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
    ) {
        if (scrollable) {
            Row(modifier.width(112.dp)) {
                RenderItem(
                    rowScope = this,
                    scrollable = false,
                    selected = selected,
                    onClick = onClick,
                    modifier = Modifier,
                    enabled = enabled,
                    alwaysShowLabel = alwaysShowLabel,
                    interactionSource = interactionSource,
                    icon = icon,
                    label = label,
                )
            }
            return
        }
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val pressed by source.collectIsPressedAsState()
        val keyboardFocus = enabled && focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val selectionColors = LocalStyledBottomNavigationColors.current
        val barContentColor = StyledTheme.currentContentColor
        val resolvedIndicatorColor =
            selectionColors?.indicatorColor
                ?.takeUnless { it == Color.Unspecified }
                ?: StyledTheme.colorScheme.primarySurface
        val baseIconColor = if (selected) {
            selectionColors?.selectedIconColor
                ?.takeUnless { it == Color.Unspecified }
                ?: if (showIndicator) StyledTheme.contentColorFor(resolvedIndicatorColor)
                else StyledTheme.colorScheme.primary
        } else {
            selectionColors?.unselectedIconColor
                ?.takeUnless { it == Color.Unspecified }
                ?: barContentColor
        }
        val baseTextColor = if (selected) {
            selectionColors?.selectedTextColor
                ?.takeUnless { it == Color.Unspecified }
                ?: if (showIndicator) barContentColor else StyledTheme.colorScheme.primary
        } else {
            selectionColors?.unselectedTextColor
                ?.takeUnless { it == Color.Unspecified }
                ?: barContentColor
        }
        val iconColor = if (enabled) baseIconColor else baseIconColor.copy(
            alpha = baseIconColor.alpha * StyledTheme.colorScheme.disableFactors.content
        )
        val textColor = if (enabled) baseTextColor else baseTextColor.copy(
            alpha = baseTextColor.alpha * StyledTheme.colorScheme.disableFactors.content
        )
        val shape = RoundedCornerShape(8.dp)
        UpdateStyledThemeLocals(
            contentColor = textColor,
            textStyle = StyledTheme.typography.labelMedium,
        ) {
            with(rowScope) {
                Column(
                    modifier = modifier
                        .weight(1f)
                        .sizeIn(
                            minWidth = StyledTheme.sizes.minimumInteractiveSize,
                            minHeight = maxOf(minimumHeight, StyledTheme.sizes.minimumInteractiveSize),
                        )
                        .clip(shape)
                        .selectable(
                            selected = selected,
                            enabled = enabled,
                            role = Role.Tab,
                            interactionSource = source,
                            indication = null,
                            onClick = onClick,
                        )
                        .then(
                            if (keyboardFocus) Modifier.border(2.dp, textColor, shape) else Modifier
                        )
                        .background(
                            if (enabled && pressed) baseTextColor.copy(alpha = 0.12f) else Color.Transparent
                        )
                        .padding(StyledTheme.padding.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        StyledTheme.spacing.small, Alignment.CenterVertically,
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (selected && showIndicator) {
                                    if (enabled) resolvedIndicatorColor else resolvedIndicatorColor.copy(
                                        alpha = resolvedIndicatorColor.alpha * StyledTheme.colorScheme.disableFactors.default
                                    )
                                } else Color.Transparent,
                                shape,
                            )
                            .padding(
                                horizontal = StyledTheme.padding.medium,
                                vertical = StyledTheme.padding.small,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        UpdateStyledThemeLocals(contentColor = iconColor, content = icon)
                    }
                    if (label != null && (alwaysShowLabel || selected)) {
                        UpdateStyledThemeLocals(
                            contentColor = textColor,
                            textStyle = StyledTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                label()
                            }
                        }
                    }
                }
            }
        }
    }
}
