package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRow as StyledSegmentedButtonRowApi

private val segmentedButtonBorderOverlap = 1.dp

@InternalComposeStyledApi
abstract class FoundationSegmentedButtonRow : StyledSegmentedButtonRowApi.Implementation {

    @Composable
    override fun <T : Any> Render(
        items: List<T>,
        selectedIndices: List<Int>,
        multiSelect: Boolean,
        modifier: Modifier,
        enabled: Boolean,
        showSelectedIcon: Boolean,
        itemShape: CornerBasedShape,
        itemContentPadding: PaddingValues,
        minItemWidth: Dp,
        customization: StyledButton.Customization?,
        content: @Composable (item: T, selected: Boolean) -> Unit,
        onItemClick: (index: Int) -> Unit,
    ) {
        BoxWithConstraints(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            val rowSizeModifier = if (constraints.minWidth == constraints.maxWidth) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.width(IntrinsicSize.Min)
            }
            Row(
                modifier = rowSizeModifier
                    .height(IntrinsicSize.Max)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(-segmentedButtonBorderOverlap),
            ) {
                items.forEachIndexed { index, item ->
                    val selected = index in selectedIndices
                    com.michaelflisar.composestyled.ui.components.StyledSegmentedButton(
                        selected = selected,
                        onClick = { onItemClick(index) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .defaultMinSize(
                                minWidth = minItemWidth,
                                minHeight = maxOf(StyledTheme.sizes.minimumInteractiveSize, 40.dp),
                            ),
                        enabled = enabled,
                        multiSelect = multiSelect,
                        showSelectedIcon = showSelectedIcon,
                        customization = customization,
                        itemShape = segmentShape(index, items.size, itemShape),
                        itemContentPadding = itemContentPadding,
                    ) {
                        content(item, selected)
                    }
                }
            }
        }
    }
}

private fun segmentShape(
    index: Int,
    count: Int,
    shape: CornerBasedShape,
) = when {
    count <= 1 -> shape
    index == 0 -> shape.copy(topEnd = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))
    index == count - 1 -> shape.copy(topStart = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    else -> shape.copy(
        topStart = CornerSize(0.dp),
        topEnd = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp),
    )
}
