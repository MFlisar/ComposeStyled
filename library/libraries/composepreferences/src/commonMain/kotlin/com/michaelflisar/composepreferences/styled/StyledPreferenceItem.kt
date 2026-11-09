package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composepreferences.styled.layout.PreferenceStyledRow
import com.michaelflisar.composestyled.classes.IconComposable

/** A standard preference row with a leading icon, title, supporting text and optional content below the text. */
@Composable
fun StyledPreferenceItem(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    key: String? = null,
    icon: IconComposable? = null,
    iconContentDescription: String? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    inlineContent: (@Composable ColumnScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    contentPadding: PaddingValues? = null,
    shape: Shape? = null,
    minHeight: Dp? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    supportingColor: Color? = null,
    titleStyle: TextStyle? = null,
    subtitleStyle: TextStyle? = null,
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
            iconContentDescription = iconContentDescription,
            trailing = trailing,
            inlineContent = inlineContent,
            onClick = onClick,
            enabled = visuals.enabled,
            modifier = modifier,
            style = visuals.style,
            insideSection = visuals.insideSection,
            breadcrumb = visuals.breadcrumb,
            contentPadding = contentPadding,
            shape = shape,
            minHeight = minHeight,
            containerColor = containerColor,
            contentColor = contentColor,
            supportingColor = supportingColor,
            titleStyle = titleStyle,
        )
    }
}
