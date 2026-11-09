package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuItemRenderer
import com.michaelflisar.composestyled.foundation.components.FoundationPopupMenu
import com.michaelflisar.composestyled.foundation.components.FoundationDropdownMenu
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidPopupMenu internal constructor() : StyledPopupMenu.Implementation {

    private val renderer = FoundationPopupMenu()

    @Composable
    override fun Render(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier,
        offset: DpOffset,
        placement: StyledPopupMenu.Placement,
        horizontalAlignment: StyledPopupMenu.HorizontalAlignment,
        properties: PopupProperties,
        scrollState: ScrollState,
        style: com.michaelflisar.composestyled.ui.components.StyledPopup.Style,
        contentPadding: PaddingValues,
        header: (@Composable () -> Unit)?,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        renderer.Render(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier.liquidGlass(style.shape, style.containerColor),
            offset = offset,
            placement = placement,
            horizontalAlignment = horizontalAlignment,
            properties = properties,
            scrollState = scrollState,
            style = style.copy(
                containerColor = Color.Transparent,
                shadowElevation = 0.dp,
            ),
            contentPadding = contentPadding,
            header = header,
            content = content,
        )
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidPopupMenu() }
    }
}

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidDropdownMenu internal constructor() :
    FoundationDropdownMenu(hoverAlpha = 0.06f, pressedAlpha = 0.14f) {

    override val defaults: StyledPopupMenuItemRenderer.Defaults
        @Composable get() = StyledPopupMenuItemRenderer.Defaults(
            minHeight = StyledTheme.sizes.minimumInteractiveSize,
            itemPadding = PaddingValues(horizontal = StyledTheme.padding.small),
            itemShape = StyledTheme.shapes.small,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidDropdownMenu() }
    }
}
