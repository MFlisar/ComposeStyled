package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationRangeSlider
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledRangeSlider

@OptIn(InternalComposeStyledApi::class)
class Fluent2RangeSlider internal constructor() :
    StyledRangeSlider.Implementation by FoundationRangeSlider() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2RangeSlider() }
    }
}
