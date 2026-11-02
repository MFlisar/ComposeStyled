package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationSlider
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSlider

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidSlider internal constructor() : StyledSlider.Implementation by FoundationSlider() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidSlider() }
    }
}
