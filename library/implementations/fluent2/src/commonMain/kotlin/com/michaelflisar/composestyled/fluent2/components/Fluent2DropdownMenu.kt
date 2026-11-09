package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuItemRenderer
import com.michaelflisar.composestyled.foundation.components.FoundationPopupMenu
import com.michaelflisar.composestyled.foundation.components.FoundationDropdownMenu
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2PopupMenu internal constructor() :
    StyledPopupMenu.Implementation by FoundationPopupMenu() {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2PopupMenu() }
    }
}

@OptIn(InternalComposeStyledApi::class)
class Fluent2DropdownMenu internal constructor() :
    FoundationDropdownMenu(hoverAlpha = 0.08f, pressedAlpha = 0.12f) {

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
        fun create() = remember { Fluent2DropdownMenu() }
    }
}
