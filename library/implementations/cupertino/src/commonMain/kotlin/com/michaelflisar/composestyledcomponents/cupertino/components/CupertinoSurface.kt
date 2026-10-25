package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.ui.components.StyledSurface
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSurface
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoSurface internal constructor(
    override val defaults: StyledSurface.Defaults,
) : StyledSurface.Implementation by FoundationSurface(defaults) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(defaults: StyledSurface.Defaults = defaults()) =
            remember(defaults) { CupertinoSurface(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(shape: Shape = RectangleShape) = StyledSurface.Defaults(shape)
    }
}
