package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.foundation.components.FoundationFloatingActionButton
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Adaptive floating action extension using Fluent-style corners. */
@OptIn(InternalComposeStyledApi::class)
class Fluent2FloatingActionButton internal constructor() : FoundationFloatingActionButton() {

    override val defaults: StyledFloatingActionButton.Defaults
        @Composable get() = StyledFloatingActionButton.Defaults(shape = RoundedCornerShape(8.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2FloatingActionButton() }
    }
}
