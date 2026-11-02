package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.tokens.minimumInteractiveSize
import com.michaelflisar.composestyled.ui.primitives.StyledInputAction
import com.michaelflisar.composestyled.ui.primitives.StyledText

object StyledPickerDefaults {
    const val clickOnIconOnly = true
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledPicker(
    value: String,
    onStartPicker: () -> Unit,
    icon: IconComposable,
    iconContentDescription: String? = null,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    clickOnIconOnly: Boolean = StyledPickerDefaults.clickOnIconOnly,
) = StyledPickerImpl(
    value = value,
    onStartPicker = onStartPicker,
    icon = icon,
    iconContentDescription = iconContentDescription,
    modifier = modifier,
    label = label,
    enabled = enabled,
    textStyle = textStyle,
    variant = StyledInput.Variant.Default,
    clickOnIconOnly = clickOnIconOnly,
)

@Composable
fun StyledAlternativePicker(
    value: String,
    onStartPicker: () -> Unit,
    icon: IconComposable,
    iconContentDescription: String? = null,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    clickOnIconOnly: Boolean = StyledPickerDefaults.clickOnIconOnly,
) {
    StyledPickerImpl(
        value = value,
        onStartPicker = onStartPicker,
        icon = icon,
        iconContentDescription = iconContentDescription,
        modifier = modifier,
        label = label,
        enabled = enabled,
        textStyle = textStyle,
        variant = StyledInput.Variant.Alternative,
        clickOnIconOnly = clickOnIconOnly,
    )
}

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledPickerImpl(
    value: String,
    onStartPicker: () -> Unit,
    icon: IconComposable,
    iconContentDescription: String? = null,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    variant: StyledInput.Variant = StyledInput.Variant.Default,
    clickOnIconOnly: Boolean = StyledPickerDefaults.clickOnIconOnly,
) {
    val trailingIcon: @Composable () -> Unit = if (clickOnIconOnly) {
        {
            StyledInputAction(
                onClick = onStartPicker,
                enabled = enabled,
                content = {
                    StyledIcon(
                        icon = icon,
                        contentDescription = iconContentDescription,
                        modifier = Modifier,
                    )
                },
            )
        }
    } else {
        {
            val color = LocalStyledContentColor.current
            StyledInputAction(
                onClick = onStartPicker,
                enabled = false,
                content = {
                    StyledIcon(
                        icon = icon,
                        contentDescription = iconContentDescription,
                        modifier = Modifier,
                        tint = color
                    )
                },
            )
        }
    }
    val input: @Composable (Modifier) -> Unit = { inputModifier ->
        val inputLabel: (@Composable () -> Unit)? =
            if (label.isNotEmpty()) ({ StyledText(label) }) else null
        if (variant == StyledInput.Variant.Alternative) {
            StyledAlternativeInput(
                value = value,
                onValueChange = {},
                modifier = inputModifier,
                enabled = enabled,
                readOnly = true,
                interactionEnabled = false,
                textStyle = textStyle,
                label = inputLabel,
                trailingIcon = trailingIcon,
            )
        } else {
            StyledInput(
                value = value,
                onValueChange = {},
                modifier = inputModifier,
                enabled = enabled,
                readOnly = true,
                interactionEnabled = false,
                textStyle = textStyle,
                label = inputLabel,
                trailingIcon = trailingIcon,
            )
        }
    }

    if (!clickOnIconOnly) {
        Box(modifier = modifier) {
            input(Modifier)
            Box(
                Modifier
                    .matchParentSize()
                    .padding(
                        top = if (label.isNotEmpty()) {
                            LocalStyledRegistry.current[StyledInput.Key].topInsetToBorder()
                        } else {
                            0.dp
                        }
                    )
                    .clip(StyledInputDefaults.shape)
                    .clickable(
                        enabled = enabled,
                        role = Role.Button,
                        onClick = onStartPicker,
                    )
            )
        }
    } else {
        input(modifier)
    }
}
