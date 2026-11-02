package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationSegmentedButtonRow
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRow

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatSegmentedButtonRow internal constructor() : FoundationSegmentedButtonRow() {

    override val defaults: StyledSegmentedButtonRow.Defaults
        @Composable get() = StyledSegmentedButtonRow.Defaults(
            itemShape = StyledTheme.shapes.small,
            itemContentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            minItemWidth = 58.dp,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): CupertinoFlatSegmentedButtonRow = remember { CupertinoFlatSegmentedButtonRow() }
    }
}
