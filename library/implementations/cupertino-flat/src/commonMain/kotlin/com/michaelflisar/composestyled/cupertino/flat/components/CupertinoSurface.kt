package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RectangleShape
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.foundation.components.FoundationSurface
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatSurface internal constructor() : FoundationSurface() {

    override val defaults: StyledSurface.Defaults
        @Composable get() = StyledSurface.Defaults(shape = RectangleShape)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatSurface() }
    }
}
