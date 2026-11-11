package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationExpandableTopBar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar

@OptIn(InternalComposeStyledApi::class)
class Fluent2ExpandableTopBar internal constructor() :
    StyledExpandableTopBar.Implementation by FoundationExpandableTopBar() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2ExpandableTopBar() }
    }
}
