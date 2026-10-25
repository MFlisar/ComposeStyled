package com.michaelflisar.composestyledcomponents.fluent2.components

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
class Fluent2DropdownMenuItem internal constructor(defaults: StyledDropdownMenuItem.Defaults) :
    StyledDropdownMenuItem.Implementation by FoundationDropdownMenuItem(
        defaults = defaults,
        hoverAlpha = 0.08f, pressedAlpha = 0.12f,
    ) {
    companion object {
        @Composable
        fun create(
            defaults: StyledDropdownMenuItem.Defaults = defaults(),
        ) = remember(defaults) { Fluent2DropdownMenuItem(defaults) }

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
