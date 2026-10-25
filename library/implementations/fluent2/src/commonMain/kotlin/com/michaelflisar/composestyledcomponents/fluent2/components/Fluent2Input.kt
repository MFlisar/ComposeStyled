package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledInput
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationInput
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2Input internal constructor(
    override val defaults: StyledInput.Defaults,
) : StyledInput.Implementation by FoundationInput(defaults) {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(defaults: StyledInput.Defaults = defaults()) =
            remember(defaults) { Fluent2Input(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = RoundedCornerShape(4.dp),
            actionIconButtonShape: Shape = CircleShape,
        ) = StyledInput.Defaults(shape = shape, actionIconButtonShape = actionIconButtonShape)
    }
}
