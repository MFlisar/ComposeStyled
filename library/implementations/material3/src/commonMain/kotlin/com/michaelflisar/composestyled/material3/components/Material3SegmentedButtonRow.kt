package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRow as StyledSegmentedButtonRowApi

class Material3SegmentedButtonRow internal constructor() : StyledSegmentedButtonRowApi.Implementation {

    override val defaults: StyledSegmentedButtonRowApi.Defaults
        @Composable get() = StyledSegmentedButtonRowApi.Defaults(
            itemShape = MaterialTheme.shapes.small,
            itemContentPadding = SegmentedButtonDefaults.ContentPadding,
            minItemWidth = ButtonDefaults.MinWidth,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): Material3SegmentedButtonRow = remember { Material3SegmentedButtonRow() }
    }

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
        val colors = segmentedButtonColors(customization)
        if (multiSelect) {
            MultiChoiceSegmentedButtonRow(modifier = modifier.height(IntrinsicSize.Max)) {
                items.forEachIndexed { index, item ->
                    val selected = index in selectedIndices
                    SegmentedButton(
                        checked = selected,
                        onCheckedChange = { onItemClick(index) },
                        modifier = Modifier
                            .fillMaxHeight()
                            .defaultMinSize(minWidth = minItemWidth),
                        enabled = enabled,
                        shape = SegmentedButtonDefaults.itemShape(index, items.size, itemShape),
                        colors = colors,
                        icon = {
                            if (showSelectedIcon) {
                                SegmentedButtonDefaults.Icon(active = selected)
                            }
                        },
                        contentPadding = itemContentPadding,
                    ) {
                        renderContent { content(item, selected) }
                    }
                }
            }
        } else {
            SingleChoiceSegmentedButtonRow(modifier = modifier.height(IntrinsicSize.Max)) {
                items.forEachIndexed { index, item ->
                    val selected = index in selectedIndices
                    SegmentedButton(
                        selected = selected,
                        onClick = { onItemClick(index) },
                        modifier = Modifier
                            .fillMaxHeight()
                            .defaultMinSize(minWidth = minItemWidth),
                        enabled = enabled,
                        shape = SegmentedButtonDefaults.itemShape(index, items.size, itemShape),
                        colors = colors,
                        icon = {
                            if (showSelectedIcon) {
                                SegmentedButtonDefaults.Icon(active = selected)
                            }
                        },
                        contentPadding = itemContentPadding,
                    ) {
                        renderContent { content(item, selected) }
                    }
                }
            }
        }
    }
}

@Composable
private fun renderContent(
    content: @Composable () -> Unit,
) {
    CopyMaterialThemeLocals(
        contentColor = LocalContentColor.current,
    ) {
        content()
    }
}

@Composable
private fun segmentedButtonColors(
    customization: StyledButton.Customization?,
) = SegmentedButtonDefaults.colors(
    activeContainerColor = customization?.colors?.filledBackground?.resolve() ?: Color.Unspecified,
    activeContentColor = customization?.colors?.filledForeground?.resolve() ?: Color.Unspecified,
    activeBorderColor = customization?.border?.resolve() ?: Color.Unspecified,
    inactiveContentColor = customization?.colors?.textForeground?.resolve() ?: Color.Unspecified,
    inactiveBorderColor = customization?.border?.resolve() ?: Color.Unspecified,
)
