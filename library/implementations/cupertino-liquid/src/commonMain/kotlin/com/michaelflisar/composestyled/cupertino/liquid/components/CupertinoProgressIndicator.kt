package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationProgressIndicator
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledProgressIndicator

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidProgressIndicator internal constructor() :
    StyledProgressIndicator.Implementation by FoundationProgressIndicator() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidProgressIndicator() }
    }
}
