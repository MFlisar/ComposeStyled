package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composepreferences.styled.layout.PreferenceStyledRow
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.components.StyledCheckbox
import com.michaelflisar.composestyled.ui.components.StyledSwitch
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior

/** Checkbox and switch presentations for [PreferenceBool]. */
object PreferenceBool {
    enum class Style {
        Checkbox,
        Switch,
    }
}

/** Boolean preference backed directly by [value]. */
@Composable
fun PreferenceBool(
    value: MutableState<Boolean>,
    title: String,
    modifier: Modifier = Modifier,
    style: PreferenceBool.Style = PreferenceBool.Style.Switch,
    clickBehavior: StyledToggleBehavior = StyledToggleBehavior.FullItem,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { true },
    isVisible: @Composable () -> Boolean = { true },
    tint: Color? = null,
    onTint: Color? = null,
    contentPadding: PaddingValues? = null,
    shape: Shape? = null,
) {
    PreferenceBool(
        value = value.value,
        onValueChange = { value.value = it },
        title = title,
        modifier = modifier,
        style = style,
        clickBehavior = clickBehavior,
        key = key,
        subtitle = subtitle,
        icon = icon,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        tint = tint,
        onTint = onTint,
        contentPadding = contentPadding,
        shape = shape,
    )
}

/** Controlled Boolean preference rendered with a switch or checkbox. */
@Composable
fun PreferenceBool(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    style: PreferenceBool.Style = PreferenceBool.Style.Switch,
    clickBehavior: StyledToggleBehavior = StyledToggleBehavior.FullItem,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { true },
    isVisible: @Composable () -> Boolean = { true },
    tint: Color? = null,
    onTint: Color? = null,
    contentPadding: PaddingValues? = null,
    shape: Shape? = null,
) {
    PreferenceEntry(
        title = title,
        subtitle = subtitle,
        filterTags = filterTags,
        key = key,
        isEnabled = isEnabled,
        isVisible = isVisible,
        breaksSectionShape = shape != null,
    ) { visuals ->
        val tintToUse = tint ?: StyledTheme.colorScheme.primary
        val onTintToUse = onTint ?: StyledTheme.contentColorFor(tintToUse)
        val rowClick = if (clickBehavior == StyledToggleBehavior.FullItem) {
            { onValueChange(!value) }
        } else {
            null
        }
        PreferenceStyledRow(
            title = title,
            subtitle = subtitle,
            icon = icon,
            onClick = rowClick,
            enabled = visuals.enabled,
            modifier = modifier.semantics(mergeDescendants = true) {
                role = if (style == PreferenceBool.Style.Switch) Role.Switch else Role.Checkbox
                toggleableState = if (value) ToggleableState.On else ToggleableState.Off
            },
            style = visuals.style,
            insideSection = visuals.insideSection,
            breadcrumb = visuals.breadcrumb,
            contentPadding = contentPadding,
            shape = shape,
            trailing = {
                when (style) {
                    PreferenceBool.Style.Switch -> StyledSwitch(
                        checked = value,
                        onCheckedChange = if (clickBehavior == StyledToggleBehavior.FullItem) {
                            null
                        } else {
                            onValueChange
                        },
                        enabled = visuals.enabled,
                        tint = tintToUse,
                        onTint = onTintToUse,
                        clickBehavior = StyledToggleBehavior.ControlOnly,
                    )

                    PreferenceBool.Style.Checkbox -> StyledCheckbox(
                        checked = value,
                        onCheckedChange = if (clickBehavior == StyledToggleBehavior.FullItem) {
                            null
                        } else {
                            onValueChange
                        },
                        enabled = visuals.enabled,
                        tint = tintToUse,
                        onTint = onTintToUse,
                        clickBehavior = StyledToggleBehavior.ControlOnly,
                    )
                }
            },
        )
    }
}
