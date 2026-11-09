package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.Modifier
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.icons.StyledIcons

/** A preference row whose content and padding are fully caller-defined. */
@Composable
fun StyledPreferenceCustom(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    key: String? = null,
    icon: IconComposable? = null,
    iconContentDescription: String? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    contentPadding: PaddingValues? = null,
    shape: Shape? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    PreferenceEntry(
        title = title,
        subtitle = subtitle,
        filterTags = filterTags,
        key = key,
        isEnabled = isEnabled,
        isVisible = isVisible,
    ) { visuals ->
        val colors = visuals.style.item.colors
        val padding = contentPadding ?: visuals.style.item.contentPadding
        val background = containerColor ?: colors.containerColor
        val foreground = contentColor ?: colors.contentColor
        val leadingIcon = icon ?: StyledIcons.Empty.takeIf {
            visuals.style.reserveLeadingIconSpace
        }
        StyledListItem(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            enabled = visuals.enabled,
            shape = shape
                ?: if (visuals.insideSection) RectangleShape else visuals.style.item.shape,
            minHeight = visuals.style.item.minimumItemHeight,
            contentPadding = padding,
            containerColor = background,
            contentColor = foreground,
            supportingColor = colors.supportingColor,
            leading = leadingIcon?.let {
                {
                    StyledIcon(
                        icon = it,
                        contentDescription = iconContentDescription.takeIf { icon != null },
                    )
                }
            },
        ) {
            content()
        }
    }
}
