package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationSegmentedButton
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButton

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatSegmentedButton internal constructor() : FoundationSegmentedButton() {

    override val defaults: StyledSegmentedButton.Defaults
        @Composable get() = StyledSegmentedButton.Defaults(
            itemShape = StyledTheme.shapes.small,
            itemContentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): CupertinoFlatSegmentedButton = remember { CupertinoFlatSegmentedButton() }
    }
}
