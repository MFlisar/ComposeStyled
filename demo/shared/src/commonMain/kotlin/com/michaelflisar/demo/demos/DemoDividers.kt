package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledHorizontalDivider
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledVerticalDivider
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.ExpandState

@Composable
internal fun DemoDividers(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Dividers",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Horizontal")
        StyledHorizontalDivider()
        StyledHorizontalDivider(
            modifier = Modifier.padding(
                start = StyledTheme.padding.content.calculateStartPadding(LocalLayoutDirection.current),
                end = StyledTheme.padding.content.calculateEndPadding(LocalLayoutDirection.current),
            ),
            thickness = 2.dp,
            color = StyledTheme.colorScheme.primary,
        )
        StyledHorizontalDivider(thickness = Dp.Hairline)

        SubTitle("Vertical")
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StyledText("Left")
            StyledVerticalDivider()
            StyledText("Middle")
            StyledVerticalDivider(thickness = 2.dp, color = StyledTheme.colorScheme.primary)
            StyledText("Right")
        }
    }
}
