package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationRail
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3NavigationRail internal constructor() : StyledNavigationRail.Implementation {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3NavigationRail() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        modifier: Modifier,
        containerColor: Color,
        contentColor: Color,
        header: @Composable (() -> Unit)?,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val railHeader: (@Composable ColumnScope.() -> Unit)? =
            if (header != null) { { header() } } else null
        ProvideCustomStyledContentLocals(contentColor = contentColor) {
            NavigationRail(
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                header = railHeader,
                windowInsets = WindowInsets(0, 0, 0, 0),
                content = content,
            )
        }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun RenderItem(
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
        val colors = NavigationRailItemDefaults.colors(
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
        NavigationRailItem(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            alwaysShowLabel = alwaysShowLabel,
            interactionSource = interactionSource,
            colors = colors,
            icon = { ProvideCustomStyledContentLocals(contentColor = iconColor, content = icon) },
            label = styledLabel,
        )
    }
}
