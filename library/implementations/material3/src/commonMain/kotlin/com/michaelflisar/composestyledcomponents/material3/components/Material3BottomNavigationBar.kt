package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

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
        containerColor: Color,
        contentColor: Color,
        windowInsets: WindowInsets,
        content: @Composable RowScope.() -> Unit,
    ) {
        ProvideCustomStyledContentLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.labelLarge,
        ) {
            NavigationBar(
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                windowInsets = windowInsets,
                content = content,
            )
        }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun RenderItem(
        rowScope: RowScope,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        alwaysShowLabel: Boolean,
        selectedContentColor: Color,
        unselectedContentColor: Color,
        indicatorColor: Color,
        interactionSource: MutableInteractionSource?,
        icon: @Composable () -> Unit,
        label: @Composable (() -> Unit)?,
    ) {
        val colors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectedContentColor,
            selectedTextColor = selectedContentColor,
            unselectedIconColor = unselectedContentColor,
            unselectedTextColor = unselectedContentColor,
            indicatorColor = indicatorColor,
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
                ProvideCustomStyledContentLocals(
                    contentColor = textColor,
                    textStyle = StyledTheme.typography.labelMedium,
                    content = label,
                )
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
                    ProvideCustomStyledContentLocals(contentColor = iconColor, content = icon)
                },
                label = styledLabel,
            )
        }
    }
}
