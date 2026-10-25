package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButton as StyledSegmentedButtonApi

class Material3SegmentedButton internal constructor(
    override val defaults: StyledSegmentedButtonApi.Defaults,
) : StyledSegmentedButtonApi.Implementation {

    companion object {

        @Composable
        fun create(
            defaults: StyledSegmentedButtonApi.Defaults = defaults(),
        ) = remember(defaults) { Material3SegmentedButton(defaults) }

        @Composable
        fun defaults(
            itemShape: Shape = MaterialTheme.shapes.small,
            itemContentPadding: PaddingValues = SegmentedButtonDefaults.ContentPadding,
        ) = StyledSegmentedButtonApi.Defaults(
            itemShape = itemShape,
            itemContentPadding = itemContentPadding,
        )
    }

    @Composable
    override fun Render(
        selected: Boolean,
        multiSelect: Boolean,
        modifier: Modifier,
        enabled: Boolean,
        itemShape: Shape,
        itemContentPadding: PaddingValues,
        customization: StyledButton.Customization?,
        content: @Composable () -> Unit,
        onClick: () -> Unit,
    ) {
        val colors = segmentedButtonColors(customization)
        if (multiSelect) {
            MultiChoiceSegmentedButtonRow(modifier = modifier) {
                SegmentedButton(
                    checked = selected,
                    onCheckedChange = { onClick() },
                    enabled = enabled,
                    shape = itemShape,
                    colors = colors,
                    icon = { SegmentedButtonDefaults.Icon(active = selected) },
                    contentPadding = itemContentPadding,
                ) {
                    renderContent(content)
                }
            }
        } else {
            SingleChoiceSegmentedButtonRow(modifier = modifier) {
                SegmentedButton(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    shape = itemShape,
                    colors = colors,
                    icon = { SegmentedButtonDefaults.Icon(active = selected) },
                    contentPadding = itemContentPadding,
                ) {
                    renderContent(content)
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
