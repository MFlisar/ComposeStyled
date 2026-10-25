package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButtonRow as StyledSegmentedButtonRowApi

private val segmentedButtonBorderOverlap = 1.dp

@InternalComposeStyledApi
class FoundationSegmentedButtonRow internal constructor(
    override val defaults: StyledSegmentedButtonRowApi.Defaults,
) : StyledSegmentedButtonRowApi.Implementation {

    companion object {

        @Composable
        fun create(
            defaults: StyledSegmentedButtonRowApi.Defaults = defaults(),
        ) = remember(defaults) { FoundationSegmentedButtonRow(defaults) }

        @Composable
        fun defaults(
            itemShape: CornerBasedShape = StyledTheme.shapes.small,
            itemContentPadding: PaddingValues = PaddingValues(
                horizontal = StyledTheme.padding.medium,
                vertical = StyledTheme.padding.small,
            ),
            minItemWidth: Dp = 58.dp,
        ) = StyledSegmentedButtonRowApi.Defaults(
            itemShape = itemShape,
            itemContentPadding = itemContentPadding,
            minItemWidth = minItemWidth,
        )
    }

    @Composable
    override fun <T : Any> Render(
        items: List<T>,
        selectedIndices: List<Int>,
        multiSelect: Boolean,
        modifier: Modifier,
        enabled: Boolean,
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
                modifier = rowSizeModifier.selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(-segmentedButtonBorderOverlap),
            ) {
                items.forEachIndexed { index, item ->
                    val selected = index in selectedIndices
                    com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButton(
                        selected = selected,
                        onClick = { onItemClick(index) },
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(
                                minWidth = minItemWidth,
                                minHeight = maxOf(StyledTheme.sizes.minimumInteractiveSize, 40.dp),
                            ),
                        enabled = enabled,
                        multiSelect = multiSelect,
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
