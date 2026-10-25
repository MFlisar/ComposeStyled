package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledTopBar
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationTopBar
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

/** Compact, centered iOS-style navigation bar; not a native UIKit view. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoTopBar internal constructor() :
    StyledTopBar.Implementation by FoundationTopBar(44.dp, centerTitle = true) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoTopBar() }
    }
}
