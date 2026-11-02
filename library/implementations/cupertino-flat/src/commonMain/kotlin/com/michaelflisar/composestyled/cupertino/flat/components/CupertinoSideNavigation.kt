package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.foundation.components.FoundationSideNavigation
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Adaptive sidebar, not a native UIKit rail. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatSideNavigation internal constructor() : FoundationSideNavigation() {

    override val defaults: StyledSideNavigation.Defaults
        @Composable get() = StyledSideNavigation.Defaults(compactWidth = 80.dp, expandedWidth = 260.dp)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatSideNavigation() }
    }
}
