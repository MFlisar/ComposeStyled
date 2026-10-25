package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.ui.components.StyledSurface
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSurface
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2Surface internal constructor(
    override val defaults: StyledSurface.Defaults,
) : StyledSurface.Implementation by FoundationSurface(defaults) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(defaults: StyledSurface.Defaults = defaults()) =
            remember(defaults) { Fluent2Surface(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(shape: Shape = RectangleShape) = StyledSurface.Defaults(shape)
    }
}
