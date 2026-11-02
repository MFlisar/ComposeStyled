package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.foundation.components.FoundationTopBar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Compact, centered iOS-style navigation bar; not a native UIKit view. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatTopBar internal constructor() :
    StyledTopBar.Implementation by FoundationTopBar(44.dp, centerTitle = true) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatTopBar() }
    }
}
