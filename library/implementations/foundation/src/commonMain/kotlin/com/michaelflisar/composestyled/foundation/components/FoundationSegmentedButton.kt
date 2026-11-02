package com.michaelflisar.composestyled.foundation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButton as StyledSegmentedButtonApi
import com.michaelflisar.composestyled.ui.icons.StyledIcons

private val segmentedButtonCheckIcon = StyledIcons.Check
private val segmentedButtonCheckSlotSize = 18.dp

@InternalComposeStyledApi
abstract class FoundationSegmentedButton : StyledSegmentedButtonApi.Implementation {

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
        SegmentedItem(
            selected = selected,
            multiSelect = multiSelect,
            showSelectedIcon = showSelectedIcon,
            enabled = enabled,
            itemShape = itemShape,
            itemContentPadding = itemContentPadding,
            customization = customization,
            modifier = modifier,
            content = content,
            onClick = onClick,
        )
    }
}

@Composable
private fun SegmentedButtonCheckIndicator(
    selected: Boolean,
) {
    AnimatedVisibility(
        visible = selected,
        enter = fadeIn(tween(150)) + scaleIn(
            animationSpec = tween(150),
            initialScale = 0f,
            transformOrigin = TransformOrigin(0f, 1f),
        ),
        exit = fadeOut(tween(100)) + scaleOut(
            animationSpec = tween(100),
            targetScale = 0f,
            transformOrigin = TransformOrigin(0f, 1f),
        ),
    ) {
        StyledIcon(
            icon = segmentedButtonCheckIcon,
            size = segmentedButtonCheckSlotSize,
        )
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun SegmentedItem(
    selected: Boolean,
    multiSelect: Boolean,
    showSelectedIcon: Boolean,
    enabled: Boolean,
    itemShape: Shape,
    itemContentPadding: PaddingValues,
    customization: StyledButton.Customization?,
    modifier: Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val colors = StyledTheme.colorScheme
    val activeContainer = customization?.colors?.filledBackground?.resolve() ?: colors.secondarySurface
    val activeContent = customization?.colors?.filledForeground?.resolve() ?: colors.onSecondarySurface
    val inactiveContent = customization?.colors?.textForeground?.resolve() ?: colors.onSurface
    val borderColor = customization?.border?.resolve() ?: colors.outline
    val containerAlpha = if (enabled) 1f else colors.disableFactors.default
    val contentAlpha = if (enabled) 1f else colors.disableFactors.content
    val outlineAlpha = if (enabled) 1f else colors.disableFactors.outline
    val foreground = (if (selected) activeContent else inactiveContent).let {
        it.copy(alpha = it.alpha * contentAlpha)
    }
    val contentOffset = animateDpAsState(
        targetValue = if (!showSelectedIcon || selected) {
            0.dp
        } else {
            -((segmentedButtonCheckSlotSize + StyledTheme.spacing.small) / 2f)
        },
        animationSpec = tween(150),
    ).value
    val segmentModifier = modifier
        .defaultMinSize(
            minWidth = StyledTheme.sizes.minimumInteractiveSize,
            minHeight = StyledTheme.sizes.minimumInteractiveSize,
        )
        .clip(itemShape)
        .background(
            color = if (selected) activeContainer.copy(alpha = activeContainer.alpha * containerAlpha) else Color.Transparent,
        )
        .border(
            width = 1.dp,
            color = borderColor.copy(alpha = borderColor.alpha * outlineAlpha),
            shape = itemShape,
        )
        .then(
            if (multiSelect) {
                Modifier.toggleable(
                    value = selected,
                    enabled = enabled,
                    role = Role.Checkbox,
                    onValueChange = { onClick() },
                )
            } else {
                Modifier.selectable(
                    selected = selected,
                    enabled = enabled,
                    role = Role.RadioButton,
                    onClick = onClick,
                )
            }
        )
        .padding(itemContentPadding)

    UpdateStyledThemeLocals(
        contentColor = foreground,
        textStyle = StyledTheme.typography.bodyMedium,
    ) {
        Row(
            modifier = segmentModifier,
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showSelectedIcon) {
                Box(
                    modifier = Modifier.size(segmentedButtonCheckSlotSize),
                    contentAlignment = Alignment.Center,
                ) {
                    SegmentedButtonCheckIndicator(selected = selected)
                }
            }
            Box(modifier = Modifier.offset(x = contentOffset)) {
                content()
            }
        }
    }
}
