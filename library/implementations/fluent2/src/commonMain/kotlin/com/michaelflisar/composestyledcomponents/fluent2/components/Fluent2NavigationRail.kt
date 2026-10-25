package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationRail
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationNavigationRail
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2NavigationRail internal constructor() :
    StyledNavigationRail.Implementation by FoundationNavigationRail() {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2NavigationRail() }
    }
}
