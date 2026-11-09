package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledChip
import com.michaelflisar.composestyled.ui.components.StyledIcon

@InternalComposeStyledApi
class FoundationChip internal constructor(
    private val shape: Shape,
) : StyledChip.Implementation {

    companion object {

        @Composable
        fun create(
            shape: Shape = StyledTheme.shapes.small,
        ) = remember(shape) { FoundationChip(shape) }
    }

    @Composable
    override fun Render(
        variant: StyledChip.Variant,
        selectable: Boolean,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        customization: StyledChip.Customization?,
        leadingIcon: IconComposable?,
        trailingIcon: IconComposable?,
        content: @Composable () -> Unit,
    ) {
        val colors = StyledTheme.colorScheme
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val active = selectable && selected
        val filled = variant == StyledChip.Variant.Filled ||
            (variant == StyledChip.Variant.Auto && !selectable)
        val unselectedContainer = if (selectable) colors.surfaceVariant else colors.secondarySurface
        val unselectedContent = if (selectable) colors.onSurfaceVariant else colors.onSecondarySurface
        val customContainer = customization?.container?.resolve()
        val customBorder = customization?.border?.resolve()
        val contentColor = when {
            active -> customization?.selectedContent?.resolve() ?: colors.onSecondarySurface
            filled -> customization?.content?.resolve() ?: unselectedContent
            else -> customization?.content?.resolve() ?: colors.onSurface
        }
        val containerColor = when {
            active -> customization?.selectedContainer?.resolve() ?: colors.secondarySurface
            customContainer != null -> customContainer
            filled -> unselectedContainer
            else -> Color.Transparent
        }
        val contentAlpha = if (enabled) 1f else colors.disableFactors.content
        val containerAlpha = if (enabled) 1f else colors.disableFactors.default
        val outlineAlpha = if (enabled) 1f else colors.disableFactors.outline
        val borderColor = when {
            active -> customization?.selectedBorder?.resolve()
            customBorder != null -> customBorder
            filled -> null
            else -> colors.outline
        }
        val itemModifier = modifier
            .defaultMinSize(minHeight = 32.dp)
            .clip(shape)
            .background(containerColor.copy(alpha = containerColor.alpha * containerAlpha))
            .then(
                if (borderColor != null) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor.copy(alpha = borderColor.alpha * outlineAlpha),
                        shape = shape,
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (selectable) {
                    Modifier.selectable(
                        selected = selected,
                        enabled = enabled,
                        role = Role.Checkbox,
                        interactionSource = source,
                        onClick = onClick,
                    )
                } else {
                    Modifier.clickable(
                        enabled = enabled,
                        interactionSource = source,
                        onClick = onClick,
                    )
                }
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)

        UpdateStyledThemeLocals(
            contentColor = contentColor.copy(alpha = contentColor.alpha * contentAlpha),
            textStyle = StyledTheme.typography.labelLarge,
        ) {
            Row(
                modifier = itemModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.let { icon ->
                    StyledIcon(
                        icon = icon,
                        size = 18.dp,
                    )
                }
                content()
                trailingIcon?.let { icon ->
                    StyledIcon(
                        icon = icon,
                        size = 18.dp,
                    )
                }
            }
        }
    }
}
