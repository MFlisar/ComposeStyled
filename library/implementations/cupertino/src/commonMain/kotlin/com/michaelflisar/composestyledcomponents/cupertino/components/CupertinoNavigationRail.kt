package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationRail
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationNavigationRail
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

/** Adaptive sidebar, not a native UIKit rail. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoNavigationRail internal constructor() :
    StyledNavigationRail.Implementation by FoundationNavigationRail(showIndicator = false) {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoNavigationRail() }
    }
}
