package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollbarContainer
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.internal.LocalStyledBottomNavigationColors
import com.michaelflisar.composestyled.ui.internal.StyledBottomNavigationColors
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

class Material3BottomNavigationBar internal constructor() : StyledBottomNavigationBar.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3BottomNavigationBar() }
    }

    @OptIn(InternalComposeStyledApi::class)
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
                if (scrollable) {
                    Surface(
                        modifier = modifier.fillMaxWidth(),
                        color = containerColor,
                        contentColor = resolvedContentColor,
                    ) {
                        StyledHorizontalScrollbarContainer(
                            modifier = Modifier.windowInsetsPadding(windowInsets),
                            scrollState = scrollState,
                            showScrollbar = showScrollbar,
                        ) {
                            Row(
                                modifier = Modifier.heightIn(min = 80.dp).selectableGroup(),
                                verticalAlignment = Alignment.CenterVertically,
                                content = content,
                            )
                        }
                    }
                } else {
                    NavigationBar(
                        modifier = modifier,
                        containerColor = containerColor,
                        contentColor = resolvedContentColor,
                        windowInsets = windowInsets,
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = StyledTheme.padding.small),
                                content = content,
                            )
                        },
                    )
                }
            }
        }
    }

    @OptIn(InternalComposeStyledApi::class)
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
        val selectionColors = LocalStyledBottomNavigationColors.current
        val barContentColor = StyledTheme.currentContentColor
        val resolvedIndicatorColor = selectionColors?.indicatorColor
            ?.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.primarySurface
        val colors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectionColors?.selectedIconColor
                ?.takeUnless { it == Color.Unspecified }
                ?: StyledTheme.contentColorFor(resolvedIndicatorColor),
            selectedTextColor = selectionColors?.selectedTextColor
                ?.takeUnless { it == Color.Unspecified }
                ?: barContentColor,
            unselectedIconColor = selectionColors?.unselectedIconColor
                ?.takeUnless { it == Color.Unspecified }
                ?: barContentColor,
            unselectedTextColor = selectionColors?.unselectedTextColor
                ?.takeUnless { it == Color.Unspecified }
                ?: barContentColor,
            indicatorColor = resolvedIndicatorColor,
        )
        val iconColor = when {
            !enabled -> colors.disabledIconColor
            selected -> colors.selectedIconColor
            else -> colors.unselectedIconColor
        }
        val textColor = when {
            !enabled -> colors.disabledTextColor
            selected -> colors.selectedTextColor
            else -> colors.unselectedTextColor
        }
        val styledLabel: (@Composable () -> Unit)? = if (label != null) {
            {
                UpdateStyledThemeLocals(
                    contentColor = textColor,
                    textStyle = StyledTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                ) {
                    CompositionLocalProvider(
                        LocalTextStyle provides LocalTextStyle.current.copy(textAlign = TextAlign.Center),
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
        } else null
        with(rowScope) {
            NavigationBarItem(
                selected = selected,
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                alwaysShowLabel = alwaysShowLabel,
                colors = colors,
                interactionSource = interactionSource,
                icon = {
                    UpdateStyledThemeLocals(contentColor = iconColor, content = icon)
                },
                label = styledLabel,
            )
        }
    }
}
