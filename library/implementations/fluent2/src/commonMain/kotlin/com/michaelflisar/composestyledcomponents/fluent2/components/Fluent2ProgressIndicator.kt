package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationProgressIndicator
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledProgressIndicator

@OptIn(InternalComposeStyledApi::class)
class Fluent2ProgressIndicator internal constructor() :
    StyledProgressIndicator.Implementation by FoundationProgressIndicator() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2ProgressIndicator() }
    }
}
