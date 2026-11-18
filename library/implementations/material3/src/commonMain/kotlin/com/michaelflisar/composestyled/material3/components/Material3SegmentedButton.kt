package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.layout.PaddingValues
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
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButton as StyledSegmentedButtonApi

class Material3SegmentedButton internal constructor() : StyledSegmentedButtonApi.Implementation {

    override val defaults: StyledSegmentedButtonApi.Defaults
        @Composable get() = StyledSegmentedButtonApi.Defaults(
            itemShape = MaterialTheme.shapes.small,
            itemContentPadding = SegmentedButtonDefaults.ContentPadding,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): Material3SegmentedButton = remember { Material3SegmentedButton() }
    }

    @Composable
    override fun Render(
        selected: Boolean,
        multiSelect: Boolean,
        showSelectedIcon: Boolean,
        modifier: Modifier,
        enabled: Boolean,
        itemShape: Shape,
        itemContentPadding: PaddingValues,
        customization: StyledButton.Customization?,
        content: @Composable () -> Unit,
        onClick: () -> Unit,
    ) {
        val colors = segmentedButtonColors(customization)
        val borderThickness = customization?.borderThickness ?: SegmentedButtonDefaults.BorderWidth
        val borderColor = when {
            enabled && selected -> colors.activeBorderColor
            enabled -> colors.inactiveBorderColor
            selected -> colors.disabledActiveBorderColor
            else -> colors.disabledInactiveBorderColor
        }
        val border = SegmentedButtonDefaults.borderStroke(borderColor, borderThickness)
        if (multiSelect) {
            MultiChoiceSegmentedButtonRow(modifier = modifier, space = borderThickness) {
                SegmentedButton(
                    checked = selected,
                    onCheckedChange = { onClick() },
                    enabled = enabled,
                    shape = itemShape,
                    colors = colors,
                    border = border,
                    icon = {
                        if (showSelectedIcon) {
                            SegmentedButtonDefaults.Icon(active = selected)
                        }
                    },
                    contentPadding = itemContentPadding,
                ) {
                    renderContent(content, selected, enabled, customization)
                }
            }
        } else {
            SingleChoiceSegmentedButtonRow(modifier = modifier, space = borderThickness) {
                SegmentedButton(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    shape = itemShape,
                    colors = colors,
                    border = border,
                    icon = {
                        if (showSelectedIcon) {
                            SegmentedButtonDefaults.Icon(active = selected)
                        }
                    },
                    contentPadding = itemContentPadding,
                ) {
                    renderContent(content, selected, enabled, customization)
                }
            }
        }
    }
}

@Composable
private fun renderContent(
    content: @Composable () -> Unit,
    selected: Boolean,
    enabled: Boolean,
    customization: StyledButton.Customization?,
) {
    val selectedColor = customization?.colors?.filledForeground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.colorScheme.onSecondarySurface
    val unselectedColor = customization?.colors?.textForeground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.colorScheme.onSurface
    val disabledColor = if (selected) {
        customization?.disabledColors?.filledForeground?.resolve()
    } else {
        customization?.disabledColors?.textForeground?.resolve()
    }?.takeUnless { it == Color.Unspecified }
    val contentColor = if (!enabled && disabledColor != null) {
        disabledColor
    } else {
        (if (selected) selectedColor else unselectedColor).let { color ->
            if (enabled) color else color.copy(
                alpha = color.alpha * StyledTheme.colorScheme.disabledAlphaFactors.content,
            )
        }
    }
    ProvideStyledLocals(
        contentColor = contentColor,
        textStyle = StyledTheme.typography.labelLarge,
    ) {
        content()
    }
}

@Composable
private fun segmentedButtonColors(
    customization: StyledButton.Customization?,
) = SegmentedButtonDefaults.colors(
    activeContainerColor = customization?.colors?.filledBackground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.colorScheme.secondarySurface,
    activeContentColor = customization?.colors?.filledForeground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.colorScheme.onSecondarySurface,
    activeBorderColor = customization?.border?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.colorScheme.outline,
    inactiveContentColor = customization?.colors?.textForeground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.colorScheme.onSurface,
    inactiveBorderColor = customization?.border?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.colorScheme.outline,
    disabledActiveContainerColor = customization?.disabledColors?.filledBackground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: Color.Unspecified,
    disabledActiveContentColor = customization?.disabledColors?.filledForeground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: Color.Unspecified,
    disabledInactiveContentColor = customization?.disabledColors?.textForeground?.resolve()
        ?.takeUnless { it == Color.Unspecified }
        ?: Color.Unspecified,
)
