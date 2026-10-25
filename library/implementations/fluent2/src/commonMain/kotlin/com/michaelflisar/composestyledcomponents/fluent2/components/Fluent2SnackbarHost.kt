package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSnackbarHost
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarHost

@OptIn(InternalComposeStyledApi::class)
class Fluent2SnackbarHost internal constructor() :
    StyledSnackbarHost.Implementation by FoundationSnackbarHost() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2SnackbarHost() }
    }
}
