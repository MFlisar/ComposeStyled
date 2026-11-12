package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.foundation.components.FoundationSideNavigation
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Adaptive sidebar, not a native UIKit rail. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidSideNavigation internal constructor() : FoundationSideNavigation() {

    override val defaults: StyledSideNavigation.Defaults
        @Composable get() = StyledSideNavigation.Defaults(compactWidth = 80.dp, expandedWidth = 260.dp)

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
        super.Render(
            modifier = modifier.liquidGlass(
                shape = RoundedCornerShape(topEnd = 26.dp, bottomEnd = 26.dp),
                tint = containerColor,
            ),
            mode = mode,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            toggleIcon = toggleIcon,
            scrollable = scrollable,
            scrollState = scrollState,
            showScrollbar = showScrollbar,
            containerColor = Color.Transparent,
            contentColor = contentColor,
            indicatorTint = indicatorTint,
            title = title,
            header = header,
            footer = footer,
            windowInsets = windowInsets,
            content = content,
        )
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidSideNavigation() }
    }
}
