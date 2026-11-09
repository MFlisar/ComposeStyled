package com.michaelflisar.composestyled.ui.specials

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.components.StyledClickable
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollContainer
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollbarLayout
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText

/** One label in a [StyledBreadcrumb]. */
@Immutable
data class StyledBreadcrumbItem(
    val label: String,
    val onClick: (() -> Unit)? = null,
)

/** Colors, typography and spacing used by [StyledBreadcrumb]. */
@Immutable
data class StyledBreadcrumbStyle(
    val itemColor: Color,
    val currentItemColor: Color,
    val separatorColor: Color,
    val itemTextStyle: TextStyle,
    val currentItemTextStyle: TextStyle,
    val itemSpacing: Dp,
    val separatorSize: Dp,
)

/** Creates theme-based styling for [StyledBreadcrumb]. */
@Composable
fun rememberStyledBreadcrumbStyle(
    itemColor: Color = StyledTheme.colorScheme.onSurfaceVariant,
    currentItemColor: Color = StyledTheme.colorScheme.onSurface,
    separatorColor: Color = StyledTheme.supportingOnColor(StyledTheme.colorScheme.surfaceVariant),
    itemTextStyle: TextStyle = StyledTheme.typography.bodyMedium,
    currentItemTextStyle: TextStyle = itemTextStyle.copy(fontWeight = FontWeight.Medium),
    itemSpacing: Dp = StyledTheme.spacing.small,
    separatorSize: Dp = StyledTheme.sizes.iconSize,
): StyledBreadcrumbStyle = remember(
    itemColor,
    currentItemColor,
    separatorColor,
    itemTextStyle,
    currentItemTextStyle,
    itemSpacing,
    separatorSize,
) {
    StyledBreadcrumbStyle(
        itemColor = itemColor,
        currentItemColor = currentItemColor,
        separatorColor = separatorColor,
        itemTextStyle = itemTextStyle,
        currentItemTextStyle = currentItemTextStyle,
        itemSpacing = itemSpacing,
        separatorSize = separatorSize,
    )
}

/**
 * Displays a horizontally scrollable path. Items before the last are clickable when they have an
 * [StyledBreadcrumbItem.onClick] action; the last item is always treated as the current location.
 */
@Composable
fun StyledBreadcrumb(
    items: List<StyledBreadcrumbItem>,
    modifier: Modifier = Modifier,
    style: StyledBreadcrumbStyle = rememberStyledBreadcrumbStyle(),
    separatorIcon: IconComposable = StyledIcons.NavigateForward,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    if (items.isEmpty()) return

    StyledRow(
        itemSpacing = style.itemSpacing,
        modifier = modifier.horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            val isCurrent = index == items.lastIndex
            if (index > 0) {
                StyledIcon(
                    icon = separatorIcon,
                    contentDescription = null,
                    size = style.separatorSize,
                    tint = style.separatorColor,
                )
            }

            val onClick = item.onClick.takeUnless { isCurrent }
            val itemColor = if (isCurrent) style.currentItemColor else style.itemColor
            val itemTextStyle = if (isCurrent) style.currentItemTextStyle else style.itemTextStyle

            StyledClickable(
                onClick = { onClick?.invoke() },
                enabled = onClick != null,
            ) {
                StyledText(
                    text = item.label,
                    color = itemColor,
                    style = itemTextStyle,
                    maxLines = maxLines,
                    overflow = overflow,
                )
            }
        }
    }
}
