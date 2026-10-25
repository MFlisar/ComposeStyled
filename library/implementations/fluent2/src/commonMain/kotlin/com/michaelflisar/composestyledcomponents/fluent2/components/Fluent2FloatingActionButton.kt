package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationFloatingActionButton
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

/** Adaptive floating action extension using Fluent-style corners. */
@OptIn(InternalComposeStyledApi::class)
class Fluent2FloatingActionButton internal constructor(
    override val defaults: StyledFloatingActionButton.Defaults,
) : StyledFloatingActionButton.Implementation by FoundationFloatingActionButton(defaults) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            defaults: StyledFloatingActionButton.Defaults = defaults(),
        ) = remember(defaults) { Fluent2FloatingActionButton(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = RoundedCornerShape(8.dp),
        ) = StyledFloatingActionButton.Defaults(shape)
    }
}
