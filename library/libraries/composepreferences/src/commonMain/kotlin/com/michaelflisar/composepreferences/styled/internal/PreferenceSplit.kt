package com.michaelflisar.composepreferences.styled.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composepreferences.styled.layout.PreferenceStyledRow
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.icons.StyledIcons

@Composable
internal fun PreferenceSplit(
    title: String,
    onOpen: () -> Unit,
    modifier: Modifier,
    key: String?,
    subtitle: String?,
    icon: IconComposable?,
    filterTags: List<String>,
    isEnabled: @Composable () -> Boolean,
    isVisible: @Composable () -> Boolean,
    control: @Composable (PreferenceEntryVisuals) -> Unit,
) {
    PreferenceEntry(
        title = title,
        subtitle = subtitle,
        filterTags = filterTags,
        key = key,
        isEnabled = isEnabled,
        isVisible = isVisible,
    ) { visuals ->
        PreferenceStyledRow(
            title = title,
            subtitle = subtitle,
            icon = icon,
            onClick = onOpen,
            enabled = visuals.enabled,
            modifier = modifier,
            style = visuals.style,
            insideSection = visuals.insideSection,
            breadcrumb = visuals.breadcrumb,
            trailing = {
                StyledIcon(
                    icon = StyledIcons.Submenu,
                    contentDescription = null,
                )
                Box(
                    Modifier
                        .padding(horizontal = 4.dp)
                        .width(1.dp)
                        .height(32.dp)
                        .background(visuals.style.item.colors.supportingColor.copy(alpha = 0.4f))
                )
                control(visuals)
            },
        )
    }
}
