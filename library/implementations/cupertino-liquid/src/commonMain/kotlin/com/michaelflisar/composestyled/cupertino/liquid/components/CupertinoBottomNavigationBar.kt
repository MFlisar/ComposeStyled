package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.foundation.components.FoundationBottomNavigationBar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidBottomNavigationBar internal constructor() : StyledBottomNavigationBar.Implementation {

    private val renderer = FoundationBottomNavigationBar(
        minimumHeight = 49.dp,
        showIndicator = false,
        addVerticalContentPadding = false,
    )

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
        renderer.Render(
            modifier = modifier.liquidGlass(
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                tint = containerColor,
            ),
            scrollable = scrollable,
            scrollState = scrollState,
            showScrollbar = showScrollbar,
            containerColor = Color.Transparent,
            contentColor = contentColor,
            customization = customization,
            windowInsets = windowInsets,
            content = content,
        )
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
        label: (@Composable () -> Unit)?,
    ) {
        renderer.RenderItem(
            rowScope = rowScope,
            scrollable = scrollable,
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

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidBottomNavigationBar() }
    }
}
