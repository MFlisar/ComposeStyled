package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationIcon
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2Icon internal constructor() : StyledIcon.Implementation by FoundationIcon() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2Icon() }
    }
}
