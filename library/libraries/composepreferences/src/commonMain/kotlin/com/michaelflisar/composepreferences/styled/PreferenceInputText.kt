package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composepreferences.styled.internal.BaseDialogPreference
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState

/** Opens a text editor dialog from a preference row. */
@Composable
fun PreferenceInputText(
    value: MutableState<String>,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    variant: StyledInput.Variant = StyledInput.Variant.Auto,
    customization: StyledInput.Customization? = null,
    shape: Shape? = null,
) {
    PreferenceInputText(
        value = value.value,
        onValueChange = { value.value = it },
        title = title,
        modifier = modifier,
        key = key,
        subtitle = subtitle,
        icon = icon,
        enabled = enabled,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        variant = variant,
        customization = customization,
        shape = shape,
    )
}

/** Controlled text editor dialog launched from a preference row. */
@Composable
fun PreferenceInputText(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    variant: StyledInput.Variant = StyledInput.Variant.Auto,
    customization: StyledInput.Customization? = null,
    shape: Shape? = null,
) {
    val dialog = rememberDialogState()
    val strings = LocalPreferenceStrings.current
    var draft by rememberSaveable { mutableStateOf(value) }
    LaunchedEffect(value, dialog.visible) {
        if (dialog.visible) draft = value
    }
    BaseDialogPreference(
        data = draft,
        title = title,
        subtitle = subtitle ?: value,
        displaySubtitle = subtitle,
        dialog = dialog,
        modifier = modifier,
        key = key,
        icon = icon,
        filterTags = filterTags,
        enabled = enabled,
        isEnabled = isEnabled,
        isVisible = isVisible,
        shape = shape,
        buttons = {
            StyledDialog.Buttons(
                positive = StyledDialog.Button(
                    onClick = { onValueChange(draft) },
                    label = strings.save,
                ),
                negative = StyledDialog.Button(
                    onClick = {},
                    label = strings.cancel,
                ),
            )
        },
        trailing = { StyledText(value) },
        onOpen = { draft = value },
    ) { data, _, padding, visuals ->
        StyledInput(
            value = data,
            onValueChange = { draft = it },
            modifier = Modifier.fillMaxWidth().padding(padding),
            enabled = visuals.enabled,
            variant = variant,
            customization = customization,
            label = { StyledText(title) },
        )
    }
}
