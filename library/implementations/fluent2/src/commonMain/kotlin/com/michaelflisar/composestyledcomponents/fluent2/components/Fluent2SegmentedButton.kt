package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSegmentedButton
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButton

@OptIn(InternalComposeStyledApi::class)
class Fluent2SegmentedButton internal constructor(
    override val defaults: StyledSegmentedButton.Defaults,
    renderer: StyledSegmentedButton.Implementation,
) : StyledSegmentedButton.Implementation by renderer {

    companion object {
        @Composable
        fun create(defaults: StyledSegmentedButton.Defaults = defaults()): Fluent2SegmentedButton {
            val renderer = FoundationSegmentedButton.create(defaults)
            return remember(defaults) { Fluent2SegmentedButton(defaults, renderer) }
        }

        @Composable
        fun defaults(
            itemShape: Shape = StyledTheme.shapes.small,
            itemContentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        ) = StyledSegmentedButton.Defaults(itemShape, itemContentPadding)
    }
}
