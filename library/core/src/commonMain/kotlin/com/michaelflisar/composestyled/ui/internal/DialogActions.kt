package com.michaelflisar.composestyled.ui.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledTooltip
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.primitives.StyledText

@InternalComposeStyledApi
@Composable
fun DialogButton(
    button: StyledDialog.Button?,
    slot: StyledDialog.ActionSlot,
    context: StyledDialog.Context,
    modifier: Modifier = Modifier,
    autoPresentation: StyledDialog.ButtonPresentation = StyledDialog.ButtonPresentation.Text,
) {
    if (button == null) return
    CompositionLocalProvider(LocalStyledContext provides StyledDialog.ActionContext(context, slot)) {
        val icon = button.icon
        val useIcon = button.presentation == StyledDialog.ButtonPresentation.Icon ||
            (button.presentation == StyledDialog.ButtonPresentation.Auto &&
                autoPresentation == StyledDialog.ButtonPresentation.Icon && icon != null)
        if (useIcon) {
            val label = requireNotNull(button.label)
            val iconContent: @Composable () -> Unit = {
                StyledIcon(icon = requireNotNull(icon), contentDescription = label)
            }
            StyledTooltip(tooltip = { StyledText(label) }) {
                StyledIconButton(
                    onClick = button.onClick,
                    modifier = modifier,
                    enabled = button.enabled,
                    role = button.role.toIconButtonRole(),
                    content = iconContent,
                )
            }
        } else StyledButton(
            onClick = button.onClick,
            modifier = modifier,
            enabled = button.enabled,
            role = button.role,
            content = button.content,
        )
    }
}

private fun StyledButton.Role.toIconButtonRole() = when (this) {
    StyledButton.Role.Default -> StyledIconButton.Role.Default
    StyledButton.Role.Primary -> StyledIconButton.Role.Primary
    StyledButton.Role.Secondary -> StyledIconButton.Role.Secondary
    StyledButton.Role.Tertiary -> StyledIconButton.Role.Tertiary
}

@InternalComposeStyledApi
@Composable
fun DialogActions(
    buttons: StyledDialog.Buttons,
    context: StyledDialog.Context,
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
    positiveFirst: Boolean = false,
    itemSpacing: Dp = StyledTheme.spacing.small,
    rowSpacing: Dp = itemSpacing,
    stackPositiveFirst: Boolean = false,
) {
    if (buttons.isEmpty) return
    val reverseFlow = !vertical && !positiveFirst && stackPositiveFirst
    val actions = when {
        vertical || positiveFirst -> listOf(
            StyledDialog.ActionSlot.Positive to buttons.positive,
            StyledDialog.ActionSlot.Neutral to buttons.neutral,
            StyledDialog.ActionSlot.Negative to buttons.negative,
        )
        reverseFlow -> listOf(
            StyledDialog.ActionSlot.Positive to buttons.positive,
            StyledDialog.ActionSlot.Negative to buttons.negative,
            StyledDialog.ActionSlot.Neutral to buttons.neutral,
        )
        else -> listOf(
            StyledDialog.ActionSlot.Neutral to buttons.neutral,
            StyledDialog.ActionSlot.Negative to buttons.negative,
            StyledDialog.ActionSlot.Positive to buttons.positive,
        )
    }
    if (vertical) {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(rowSpacing)) {
            actions.forEach { (slot, button) ->
                DialogButton(button, slot, context, Modifier.fillMaxWidth())
            }
        }
    } else {
        val direction = LocalLayoutDirection.current
        val flowDirection = if (!reverseFlow) direction else when (direction) {
            LayoutDirection.Ltr -> LayoutDirection.Rtl
            LayoutDirection.Rtl -> LayoutDirection.Ltr
        }
        // Reverse the flow, not the content, so confirmation is trailing in a row and first when stacked.
        CompositionLocalProvider(LocalLayoutDirection provides flowDirection) {
            FlowRow(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    itemSpacing, if (reverseFlow) Alignment.Start else Alignment.End,
                ),
                verticalArrangement = Arrangement.spacedBy(rowSpacing),
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides direction) {
                    actions.forEach { (slot, button) ->
                        DialogButton(button, slot, context)
                    }
                }
            }
        }
    }
}
