package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSegmentedButtonRow
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButtonRow

@OptIn(InternalComposeStyledApi::class)
class Fluent2SegmentedButtonRow internal constructor(
    override val defaults: StyledSegmentedButtonRow.Defaults,
    renderer: StyledSegmentedButtonRow.Implementation,
) : StyledSegmentedButtonRow.Implementation by renderer {

    companion object {
        @Composable
        fun create(defaults: StyledSegmentedButtonRow.Defaults = defaults()): Fluent2SegmentedButtonRow {
            val renderer = FoundationSegmentedButtonRow.create(defaults)
            return remember(defaults) { Fluent2SegmentedButtonRow(defaults, renderer) }
        }

        @Composable
        fun defaults(
            itemShape: CornerBasedShape = StyledTheme.shapes.small,
            itemContentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            minItemWidth: Dp = 58.dp,
        ) = StyledSegmentedButtonRow.Defaults(itemShape, itemContentPadding, minItemWidth)
    }
}
