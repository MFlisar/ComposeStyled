package com.michaelflisar.composestyled.ui.internal

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior

@Composable
internal fun StyledToggleItem(
    value: Boolean,
    onValueChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource?,
    behavior: StyledToggleBehavior,
    shape: Shape,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StyledTheme.padding.medium,
        vertical = StyledTheme.padding.small,
    ),
    role: Role,
    label: (@Composable () -> Unit)?,
    control: @Composable (Modifier, ((Boolean) -> Unit)?) -> Unit,
) {
    if (label == null) {
        control(modifier, onValueChange)
        return
    }

    val fullItemClick = behavior == StyledToggleBehavior.FullItem && onValueChange != null
    val minimumHeightModifier = modifier.defaultMinSize(
        minHeight = StyledTheme.sizes.minimumInteractiveSize,
    )
    val rowModifier = if (fullItemClick) {
        val clickableModifier = minimumHeightModifier
            .clip(shape)
        when (role) {
            Role.RadioButton -> clickableModifier.selectable(
                selected = value,
                enabled = enabled,
                role = role,
                interactionSource = interactionSource,
                onClick = { onValueChange(!value) },
            )

            else -> clickableModifier.toggleable(
                value = value,
                enabled = enabled,
                role = role,
                interactionSource = interactionSource,
                onValueChange = onValueChange,
            )
        }.padding(contentPadding)
    } else {
        minimumHeightModifier
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
    ) {
        control(Modifier, if (fullItemClick) null else onValueChange)
        label()
    }
}
