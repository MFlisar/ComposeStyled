package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.ui.components.StyledScaffold
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationScaffold
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2Scaffold internal constructor(
    override val defaults: StyledScaffold.Defaults,
) : StyledScaffold.Implementation by FoundationScaffold(defaults) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            defaults: StyledScaffold.Defaults = defaults(),
        ) = remember(defaults) {
            Fluent2Scaffold(defaults)
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults() = StyledScaffold.Defaults()
    }
}
