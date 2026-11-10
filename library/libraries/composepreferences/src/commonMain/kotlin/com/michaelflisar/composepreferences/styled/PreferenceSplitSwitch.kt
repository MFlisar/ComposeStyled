package com.michaelflisar.composepreferences.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composepreferences.styled.internal.PreferenceSplit
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledSwitch

/** Split destination and switch actions with independent hit targets. */
@Composable
fun PreferenceSplitSwitch(
    value: MutableState<Boolean>,
    title: String,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    tint: Color? = null,
    onTint: Color? = null,
) {
    PreferenceSplitSwitch(
        value = value.value,
        onValueChange = { value.value = it },
        title = title,
        onOpen = onOpen,
        modifier = modifier,
        key = key,
        subtitle = subtitle,
        icon = icon,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        tint = tint,
        onTint = onTint,
    )
}

/** Controlled split destination and switch actions. */
@Composable
fun PreferenceSplitSwitch(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    title: String,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    tint: Color? = null,
    onTint: Color? = null,
) {
    PreferenceSplit(
        title = title,
        onOpen = onOpen,
        modifier = modifier,
        key = key,
        subtitle = subtitle,
        icon = icon,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        control = { visuals ->
            val tintToUse = tint ?: StyledTheme.colorScheme.primary
            val onTintToUse = onTint ?: StyledTheme.contentColorFor(tintToUse)
            StyledSwitch(
                checked = value,
                onCheckedChange = onValueChange,
                enabled = visuals.enabled,
                tint = tintToUse,
                onTint = onTintToUse,
            )
        },
    )
}
