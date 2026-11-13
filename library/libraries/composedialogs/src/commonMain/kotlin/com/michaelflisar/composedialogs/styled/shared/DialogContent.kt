package com.michaelflisar.composedialogs.styled.shared

import com.michaelflisar.composestyled.ui.layout.StyledDialog

internal fun StyledDialog.Buttons.withPositiveButtonEnabled(enabled: Boolean): StyledDialog.Buttons {
    val positive = positive ?: return this
    val updated = when (val label = positive.label) {
        null -> StyledDialog.Button(
            onClick = positive.onClick,
            variant = positive.variant,
            enabled = positive.enabled && enabled,
            dismissOnClick = positive.dismissOnClick,
            content = positive.content,
        )

        else -> StyledDialog.Button(
            onClick = positive.onClick,
            label = label,
            icon = positive.icon,
            presentation = positive.presentation,
            enabled = positive.enabled && enabled,
            dismissOnClick = positive.dismissOnClick,
            variant = positive.variant,
        )
    }
    return copy(positive = updated)
}
