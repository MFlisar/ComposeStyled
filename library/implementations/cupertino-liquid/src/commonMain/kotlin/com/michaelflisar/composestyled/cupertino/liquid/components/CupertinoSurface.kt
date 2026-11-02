package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.foundation.components.FoundationSurface
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidSurface internal constructor() : FoundationSurface() {

    override val defaults: StyledSurface.Defaults
        @Composable get() = StyledSurface.Defaults(shape = RoundedCornerShape(20.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidSurface() }
    }
}
