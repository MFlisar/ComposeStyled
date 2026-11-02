package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyled.foundation.components.FoundationPopupMenu
import com.michaelflisar.composestyled.foundation.components.FoundationDropdownMenu
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatPopupMenu internal constructor() :
    StyledPopupMenu.Implementation by FoundationPopupMenu() {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatPopupMenu() }
    }
}

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatDropdownMenu internal constructor() :
    FoundationDropdownMenu(hoverAlpha = 0.06f, pressedAlpha = 0.14f) {

    override val defaults: StyledDropdownMenuItem.Defaults
        @Composable get() = StyledDropdownMenuItem.Defaults(
            minHeight = StyledTheme.sizes.minimumInteractiveSize,
            itemPadding = PaddingValues(horizontal = StyledTheme.padding.small),
            itemShape = StyledTheme.shapes.small,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatDropdownMenu() }
    }
}
