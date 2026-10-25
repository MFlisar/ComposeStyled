package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationRail
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
class FoundationNavigationRail(
    showIndicator: Boolean = true,
) : StyledNavigationRail.Implementation {
    private val items = FoundationBottomNavigationBar(56.dp, showIndicator)

    @Composable
    override fun Render(
        modifier: Modifier,
        containerColor: Color,
        contentColor: Color,
        header: @Composable (() -> Unit)?,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        ProvideCustomStyledContentLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.labelMedium,
        ) {
            Column(
                modifier = modifier.width(96.dp).fillMaxHeight()
                    .background(containerColor).selectableGroup()
                    .padding(vertical = StyledTheme.padding.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
            ) {
                header?.invoke()
                content()
            }
        }
    }

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
        Row(modifier) {
            items.RenderItem(
                rowScope = this,
                selected = selected,
                onClick = onClick,
                modifier = Modifier,
                enabled = enabled,
                alwaysShowLabel = alwaysShowLabel,
                selectedContentColor = selectedContentColor,
                unselectedContentColor = unselectedContentColor,
                indicatorColor = indicatorColor,
                interactionSource = interactionSource,
                icon = icon,
                label = label,
            )
        }
    }
}
