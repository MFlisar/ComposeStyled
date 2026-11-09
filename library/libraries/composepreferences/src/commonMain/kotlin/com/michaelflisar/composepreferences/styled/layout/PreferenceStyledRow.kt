package com.michaelflisar.composepreferences.styled.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composepreferences.styled.PreferenceRowColors
import com.michaelflisar.composepreferences.styled.PreferenceStyle
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.components.StyledListItemDefaults
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.UpdateStyledThemeLocals

@OptIn(InternalComposeStyledApi::class)
@Composable
internal fun PreferenceStyledRow(
    title: String,
    subtitle: String?,
    modifier: Modifier = Modifier,
    icon: IconComposable? = null,
    iconContentDescription: String? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    inlineContent: (@Composable ColumnScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    style: PreferenceStyle,
    insideSection: Boolean = false,
    breadcrumb: String? = null,
    contentPadding: PaddingValues? = null,
    shape: Shape? = null,
    minHeight: Dp? = null,
    colors: PreferenceRowColors = style.item.colors,
    containerColor: Color? = null,
    contentColor: Color? = null,
    supportingColor: Color? = null,
    titleStyle: TextStyle? = null,
    subtitleStyle: TextStyle? = null,
) {
    val resolvedContentColor = contentColor ?: colors.contentColor
    val leadingIcon = icon ?: StyledIcons.Empty.takeIf { style.reserveLeadingIconSpace }
    StyledListItem(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = shape ?: if (insideSection) RectangleShape else style.item.shape,
        minHeight = minHeight ?: style.item.minimumItemHeight,
        contentPadding = contentPadding ?: style.item.contentPadding,
        containerColor = containerColor ?: colors.containerColor,
        contentColor = resolvedContentColor,
        supportingColor = supportingColor ?: colors.supportingColor,
        leading = leadingIcon?.let {
            {
                StyledIcon(
                    icon = it,
                    contentDescription = iconContentDescription.takeIf { icon != null },
                )
            }
        },
        actions = trailing?.takeIf { inlineContent == null }?.let { content ->
            {
                UpdateStyledThemeLocals(
                    contentColor = style.trailingStyle.contentColor ?: resolvedContentColor,
                    textStyle = style.trailingStyle.textStyle,
                ) {
                    PreferenceTrailingContent(
                        maxWidthFraction = style.trailingStyle.maxWidthFraction,
                        content = content,
                    )
                }
            }
        },
    ) {
        if (inlineContent != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(StyledListItemDefaults.spacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    StyledText(
                        text = title,
                        style = titleStyle ?: style.item.titleTextStyle,
                    )
                    if (subtitle != null) {
                        StyledText(
                            text = subtitle,
                            color = supportingColor ?: colors.supportingColor,
                            style = subtitleStyle ?: style.item.subtitleTextStyle,
                        )
                    }
                }
                trailing?.let { content ->
                    UpdateStyledThemeLocals(
                        contentColor = style.trailingStyle.contentColor ?: resolvedContentColor,
                        textStyle = style.trailingStyle.textStyle,
                    ) {
                        PreferenceTrailingContent(
                            maxWidthFraction = style.trailingStyle.maxWidthFraction,
                            content = content,
                        )
                    }
                }
            }
            inlineContent.invoke(this)
        } else {
            StyledText(
                text = title,
                style = titleStyle ?: style.item.titleTextStyle,
            )
            if (subtitle != null) {
                StyledText(
                    text = subtitle,
                    color = supportingColor ?: colors.supportingColor,
                    style = subtitleStyle ?: style.item.subtitleTextStyle,
                )
            }
        }
    }
}

@Composable
private fun PreferenceTrailingContent(
    maxWidthFraction: Float,
    content: @Composable RowScope.() -> Unit,
) {
    BoxWithConstraints {
        Row(
            modifier = Modifier.widthIn(max = maxWidth * maxWidthFraction),
            horizontalArrangement = Arrangement.spacedBy(
                StyledListItemDefaults.spacing,
                Alignment.End,
            ),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
