package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopupMenu
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationPopupMenu
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationDropdownMenuItem
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoPopupMenu internal constructor() :
    StyledPopupMenu.Implementation by FoundationPopupMenu() {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoPopupMenu() }
    }
}

@OptIn(InternalComposeStyledApi::class)
class CupertinoDropdownMenuItem internal constructor(defaults: StyledDropdownMenuItem.Defaults) :
    StyledDropdownMenuItem.Implementation by FoundationDropdownMenuItem(
        defaults = defaults,
        hoverAlpha = 0.06f, pressedAlpha = 0.14f,
    ) {
    companion object {
        @Composable
        fun create(
            defaults: StyledDropdownMenuItem.Defaults = defaults(),
        ) = remember(defaults) { CupertinoDropdownMenuItem(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            minHeight: Dp = StyledTheme.sizes.minimumInteractiveSize,
            itemPadding: PaddingValues = PaddingValues(horizontal = StyledTheme.padding.small),
            itemShape: Shape = StyledTheme.shapes.small,
        ) = StyledDropdownMenuItem.Defaults(minHeight, itemPadding, itemShape)
    }
}
