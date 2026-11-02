package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.foundation.components.FoundationTopBar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2TopBar internal constructor() :
    StyledTopBar.Implementation by FoundationTopBar(48.dp) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2TopBar() }
    }
}
